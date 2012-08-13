package org.dynjs.runtime;

import java.util.ArrayList;
import java.util.List;

import org.dynjs.compiler.JSCompiler;
import org.dynjs.exception.TypeError;
import org.dynjs.parser.statement.FunctionDeclaration;
import org.dynjs.parser.statement.VariableDeclaration;
import org.dynjs.parser.statement.VariableDeclarationStatement;
import org.dynjs.runtime.BlockManager.Entry;

public class ExecutionContext {

    public static ExecutionContext createGlobalExecutionContext(JSEngine engine) {
        // 10.4.1.1
        LexicalEnvironment env = LexicalEnvironment.newGlobalEnvironment( engine );
        ExecutionContext context = new ExecutionContext( env, env, env.getGlobalObject(), false );
        return context;
    }

    public static ExecutionContext createEvalExecutionContext(JSEngine engine) {
        // 10.4.2 (no caller)
        return createGlobalExecutionContext( engine );
    }

    private LexicalEnvironment lexicalEnvironment;
    private LexicalEnvironment variableEnvironment;
    private JSObject thisBinding;
    private boolean strict;

    public ExecutionContext(LexicalEnvironment lexicalEnvironment, LexicalEnvironment variableEnvironment, JSObject thisBinding, boolean strict) {
        this.lexicalEnvironment = lexicalEnvironment;
        this.variableEnvironment = variableEnvironment;
        this.thisBinding = thisBinding;
        this.strict = strict;
    }

    public LexicalEnvironment getLexicalEnvironment() {
        return this.lexicalEnvironment;
    }

    public LexicalEnvironment getVariableEnvironment() {
        return this.variableEnvironment;
    }

    public JSObject getThisBinding() {
        return this.thisBinding;
    }

    public boolean isStrict() {
        return this.strict;
    }

    void setStrict(boolean strict) {
        this.strict = strict;
    }

    public Reference resolve(String name) {
        return this.lexicalEnvironment.getIdentifierReference( this, name, isStrict() );
    }

    // ----------------------------------------------------------------------

    public void execute(JSProgram program) {
        setStrict( program.isStrict() );
        performDeclarationBindingInstantiation( program );
        program.execute( this );
    }

    public Object eval(JSEval eval) {
        ExecutionContext evalContext = createEvalExecutionContext( eval );
        return eval.evaluate( evalContext );
    }

    public Object call(JSFunction function, Object self, Object... args) {
        // 13.2.1
        ExecutionContext fnContext = createFunctionExecutionContext( function, self, args );
        Object result = function.call( fnContext, args );
        if (result == null) {
            return Types.UNDEFINED;
        }

        return result;
    }

    public JSObject construct(JSFunction function, Object... args) {
        // 13.2.2
        DynObject obj = new DynObject();
        obj.setClassName( "Object" );
        obj.setExtensible( true );
        JSObject proto = function.getPrototype();
        obj.setPrototype( proto );
        call( function, obj, args );
        return obj;
    }

    // ----------------------------------------------------------------------

    public ExecutionContext createEvalExecutionContext(JSEval eval) {
        // 10.4.2 (with caller)
        ExecutionContext context = null;
        if (!eval.isStrict()) {
            context = new ExecutionContext( this.getLexicalEnvironment(), this.getVariableEnvironment(), this.getThisBinding(), false );
        } else {
            LexicalEnvironment strictVarEnv = LexicalEnvironment.newDeclarativeEnvironment( this.getLexicalEnvironment() );
            context = new ExecutionContext( strictVarEnv, strictVarEnv, this.getThisBinding(), true );
        }
        context.performFunctionDeclarationBindings( eval, true );
        context.performVariableDeclarationBindings( eval, true );
        return context;
    }

    public ExecutionContext createFunctionExecutionContext(JSFunction function, Object thisArg, Object... arguments) {
        // 10.4.3
        if (thisArg == null || thisArg == Types.NULL || thisArg == Types.UNDEFINED) {
            thisArg = this.getLexicalEnvironment().getGlobalObject();
        }

        JSObject thisBinding = Types.toObject( thisArg );

        LexicalEnvironment scope = function.getScope();
        LexicalEnvironment localEnv = LexicalEnvironment.newDeclarativeEnvironment( scope );

        ExecutionContext context = new ExecutionContext( localEnv, localEnv, thisBinding, function.isStrict() );
        context.performDeclarationBindingInstantiation( function, arguments );
        return context;
    }

    public Completion executeCatch(BasicBlock block, String identifier, Object thrown) {
        // 12.14
        LexicalEnvironment oldEnv = this.lexicalEnvironment;
        LexicalEnvironment catchEnv = LexicalEnvironment.newDeclarativeEnvironment( oldEnv );
        catchEnv.getRecord().createMutableBinding( this, identifier, false );
        catchEnv.getRecord().setMutableBinding( this, identifier, thrown, false );
        this.lexicalEnvironment = catchEnv;
        try {
            return block.invoke( this );
        } finally {
            this.lexicalEnvironment = oldEnv;
        }
    }

    private void performDeclarationBindingInstantiation(JSProgram program) {
        performFunctionDeclarationBindings( program, false );
        performVariableDeclarationBindings( program, false );
    }

    private void performDeclarationBindingInstantiation(JSFunction function, Object[] arguments) {
        // 10.5 (Functions)
        String[] names = function.getFormalParameters();

        Object v = null;

        DeclarativeEnvironmentRecord env = (DeclarativeEnvironmentRecord) this.variableEnvironment.getRecord();

        // * 4
        for (int i = 0; i < names.length; ++i) {
            if ((i + 1) > arguments.length) {
                v = Types.UNDEFINED;
            } else {
                v = arguments[i];
            }

            if (!env.hasBinding( this, names[i] )) {
                env.createMutableBinding( this, names[i], false );
            }

            env.setMutableBinding( this, names[i], v, function.isStrict() );
        }

        // * 5
        performFunctionDeclarationBindings( function, false );

        // * 6
        if (!env.hasBinding( this, "arguments" )) {
            // * 7
            Arguments argsObj = createArgumentsObject( function, arguments );

            if (function.isStrict()) {
                env.createImmutableBinding( "arguments" );
                env.initializeImmutableBinding( "arguments", argsObj );
            } else {
                env.createMutableBinding( this, "arguments", false );
                env.setMutableBinding( this, "arguments", argsObj, false );
            }
        }

        // * 8
        performVariableDeclarationBindings( function, false );
    }

    private Arguments createArgumentsObject(final JSFunction function, final Object[] arguments) {
        // 10.6

        Arguments obj = new Arguments();
        PropertyDescriptor desc = new PropertyDescriptor() {
            {
                set( "Value", arguments.length );
                set( "Writable", true );
                set( "Enumerable", false );
                set( "Configurable", true );
            }
        };
        obj.defineOwnProperty( this, "length", desc, false );

        String[] names = function.getFormalParameters();

        DynObject map = new DynObject();
        List<String> mappedNames = new ArrayList<>();

        final LexicalEnvironment env = getVariableEnvironment();

        for (int i = arguments.length - 1; i >= 0; --i) {
            final Object val = arguments[i];
            desc = new PropertyDescriptor() {
                {
                    set( "Value", val );
                    set( "Writable", true );
                    set( "Enumerable", false );
                    set( "Configurable", true );
                }
            };

            obj.defineOwnProperty( this, "" + i, desc, false );

            if (!function.isStrict()) {
                final String name = names[i];
                if (i < names.length) {
                    if (!mappedNames.contains( name )) {
                        mappedNames.add( name );

                        desc = new PropertyDescriptor() {
                            {
                                set( "Set", new ArgSetter( env, name ) );
                                set( "Get", new ArgGetter( env, name ) );
                                set( "Configurable", true );
                            }
                        };
                        map.defineOwnProperty( this, "" + i, desc, false );
                    }
                }
            }
        }

        if (!mappedNames.isEmpty()) {
            obj.setParameterMap( map );
        }

        return obj;
    }

    private void performFunctionDeclarationBindings(final JSCode code, final boolean configurableBindings) {
        // 10.5 Function Declaration Binding
        List<FunctionDeclaration> decls = code.getFunctionDeclarations();

        EnvironmentRecord env = this.variableEnvironment.getRecord();
        for (FunctionDeclaration each : decls) {
            String identifier = each.getIdentifier();
            if (!env.hasBinding( this, identifier )) {
                env.createMutableBinding( this, identifier, configurableBindings );
            } else if (env.isGlobal()) {
                JSObject globalObject = ((ObjectEnvironmentRecord) env).getBindingObject();
                PropertyDescriptor existingProp = (PropertyDescriptor) globalObject.getProperty( this, identifier );
                if (existingProp.isConfigurable()) {
                    PropertyDescriptor newProp = new PropertyDescriptor() {
                        {
                            set( "Value", Types.UNDEFINED );
                            set( "Writable", true );
                            set( "Enumerable", true );
                            set( "Configurable", configurableBindings );
                        }
                    };
                    globalObject.defineOwnProperty( this, identifier, newProp, true );
                } else if (existingProp.isAccessorDescriptor() || (!existingProp.isWritable() && !existingProp.isEnumerable())) {
                    throw new TypeError();
                }
            }
            JSFunction function = getCompiler().compileFunction( this, code.isStrict(), each.getFormalParameters(), each.getBlock() );
            env.setMutableBinding( this, identifier, function, code.isStrict() );
        }
    }

    private void performVariableDeclarationBindings(final JSCode code, final boolean configurableBindings) {
        List<VariableDeclaration> decls = code.getVariableDeclarations();

        EnvironmentRecord env = this.variableEnvironment.getRecord();
        for (VariableDeclaration decl : decls ) {
            String identifier = decl.getIdentifier();
            if (!env.hasBinding( this, identifier )) {
                env.createMutableBinding( this, identifier, configurableBindings );
                env.setMutableBinding( this, identifier, Types.UNDEFINED, code.isStrict() );
            }
        }
    }

    public GlobalObject getGlobalObject() {
        return this.lexicalEnvironment.getGlobalObject();
    }

    public JSCompiler getCompiler() {
        return getGlobalObject().getCompiler();
    }

    public BlockManager getBlockManager() {
        return getGlobalObject().getBlockManager();
    }

    public Reference createPropertyReference(Object base, String propertyName) {
        return new Reference( getGlobalObject(), propertyName, base, isStrict() );
    }

    public Entry retrieveBlockEntry(int statementNumber) {
        return this.lexicalEnvironment.getGlobalObject().retrieveBlockEntry( statementNumber );
    }

}
