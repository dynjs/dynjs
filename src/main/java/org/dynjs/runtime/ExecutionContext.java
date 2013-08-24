package org.dynjs.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.dynjs.Clock;
import org.dynjs.Config;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.exception.ThrowException;
import org.dynjs.parser.ast.FunctionDeclaration;
import org.dynjs.parser.ast.VariableDeclaration;
import org.dynjs.runtime.BlockManager.Entry;
import org.dynjs.runtime.PropertyDescriptor.Names;

public class ExecutionContext {

    public static ExecutionContext createGlobalExecutionContext(DynJS runtime) {
        // 10.4.1.1
        LexicalEnvironment env = LexicalEnvironment.newGlobalEnvironment(runtime);
        ExecutionContext context = new ExecutionContext(null, env, env, env.getGlobalObject(), false);
        context.clock = runtime.getConfig().getClock();
        context.timeZone = runtime.getConfig().getTimeZone();
        context.locale = runtime.getConfig().getLocale();
        return context;
    }
    
    public static ExecutionContext createGlobalExecutionContext(DynJS runtime, InitializationListener listener) {
        ExecutionContext context = ExecutionContext.createEvalExecutionContext(runtime);
        listener.initialize(context);
        return context;
    }

    public static ExecutionContext createEvalExecutionContext(DynJS runtime) {
        // 10.4.2 (no caller)
        return createGlobalExecutionContext(runtime);
    }

    private ExecutionContext parent;
    private LexicalEnvironment lexicalEnvironment;
    private LexicalEnvironment variableEnvironment;
    private Object thisBinding;
    private boolean strict;

    private int lineNumber;
    private String fileName;
    private String debugContext = "<eval>";

    private Clock clock;
    private TimeZone timeZone;
    private Locale locale;
    private Object functionReference;

    public ExecutionContext(ExecutionContext parent, LexicalEnvironment lexicalEnvironment, LexicalEnvironment variableEnvironment, Object thisBinding, boolean strict) {
        this.parent = parent;
        this.lexicalEnvironment = lexicalEnvironment;
        this.variableEnvironment = variableEnvironment;
        this.thisBinding = thisBinding;
        this.strict = strict;
    }

    public Object getFunctionReference() {
        return this.functionReference;
    }

    public ExecutionContext getParent() {
        return this.parent;
    }

    public LexicalEnvironment getLexicalEnvironment() {
        return this.lexicalEnvironment;
    }

    public LexicalEnvironment getVariableEnvironment() {
        return this.variableEnvironment;
    }

    public Object getThisBinding() {
        return this.thisBinding;
    }

    public boolean isStrict() {
        return this.strict;
    }

    public Clock getClock() {
        if (this.parent != null) {
            return this.parent.getClock();
        }
        return this.clock;
    }

    public TimeZone getTimeZone() {
        if (this.parent != null) {
            return this.parent.getTimeZone();
        }

        return this.timeZone;
    }

    public Locale getLocale() {
        if (this.parent != null) {
            return this.parent.getLocale();
        }

        return this.locale;
    }

    void setStrict(boolean strict) {
        this.strict = strict;
    }

    public Reference resolve(String name) {
        Reference result = this.lexicalEnvironment.getIdentifierReference(this, name, isStrict());
        return result;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public String getFileName() {
        return this.fileName;
    }

    // ----------------------------------------------------------------------

    public Completion execute(JSProgram program) {
        try {
            ThreadContextManager.pushContext(this);
            setStrict(program.isStrict());
            this.fileName = program.getFileName();
            performDeclarationBindingInstantiation(program);
            try {
                return program.execute(this);
            } catch (ThrowException e) {
                throw e;
            } catch (Throwable t) {
                throw new ThrowException(this, t);
            }
        } finally {
            ThreadContextManager.popContext();
        }
    }

    public Object eval(JSProgram eval, boolean direct) {
        try {
            ExecutionContext evalContext = createEvalExecutionContext(eval, direct);
            ThreadContextManager.pushContext(evalContext);
            Completion result = eval.execute(evalContext);
            return result.value;
        } finally {
            ThreadContextManager.popContext();
        }
    }

    public Object call(JSFunction function, Object self, Object... args) {
        return call(null, function, self, args);
    }

    public Object call(Object functionReference, JSFunction function, Object self, Object... args) {
        // 13.2.1
        return internalCall(functionReference, function, self, args);
    }

    public Object construct(Reference reference, Object... args) {
        Object value = reference.getValue(this);
        if (value instanceof JSFunction) {
            return construct(reference, (JSFunction) value, args);
        }
        return null;

    }

    public Object construct(JSFunction function, Object... args) {
        if (!function.isConstructor()) {
            throw new ThrowException(this, createTypeError("not a constructor"));
        }

        // 13.2.2
        // 1. create the new object
        JSObject obj = function.createNewObject(this);
        // 2. set internal methods per 8.12 [DynObject]
        // 3. set class name [DynObject subclass (defaults true)]
        // 4. Set Extensible [DynObject subclass (defaults true)]
        // 5. Get the function's prototype
        // 6. If prototype is an object make that the new object's prototype
        // 7. If prototype is not an object set to the standard builtin object prototype 15.2.4 [AbstractJavascriptFunction]
        // [AbstractJavascriptFunction] handles #7, subclasses may handle #6 if necessary (see BuiltinArray#createNewObject)
        Object p = function.get(this, "prototype");
        if (p != Types.UNDEFINED && p instanceof JSObject) {
            obj.setPrototype((JSObject) p);
        } else {
            JSObject defaultObjectProto = getPrototypeFor("Object");
            obj.setPrototype(defaultObjectProto);
        }

        // 8. Call the function with obj as self
        Object result = internalCall(null, function, obj, args);
        // 9. If result is a JSObject return it
        if (result instanceof JSObject) {
            return (JSObject) result;
        }
        // Otherwise return obj
        return obj;
    }

    public Object internalCall(Object functionReference, JSFunction function, Object self, Object... args) {
        // 13.2.1
        try {
            ExecutionContext fnContext = createFunctionExecutionContext(functionReference, function, self, args);
            ThreadContextManager.pushContext(fnContext);
            try {
                Object value = function.call(fnContext);
                if (value == null) {
                    return Types.NULL;
                }
                return value;
            } catch (ThrowException e) {
                throw e;
            } catch (Throwable e) {
                throw new ThrowException(fnContext, e);
            }
        } finally {
            ThreadContextManager.popContext();
        }
    }

    // ----------------------------------------------------------------------

    public ExecutionContext createEvalExecutionContext(JSProgram eval, boolean direct) {
        // 10.4.2 (with caller)
        ExecutionContext context = null;

        Object evalThisBinding = null;
        LexicalEnvironment evalLexEnv = null;
        LexicalEnvironment evalVarEnv = null;

        if (!direct) {
            evalThisBinding = getGlobalObject();
            evalLexEnv = LexicalEnvironment.newGlobalEnvironment(getGlobalObject());
            evalVarEnv = LexicalEnvironment.newGlobalEnvironment(getGlobalObject());
        } else {
            evalThisBinding = this.thisBinding;
            evalLexEnv = this.getLexicalEnvironment();
            evalVarEnv = this.getVariableEnvironment();
        }

        if (eval.isStrict()) {
            LexicalEnvironment strictVarEnv = LexicalEnvironment.newDeclarativeEnvironment(this.getLexicalEnvironment());
            evalLexEnv = strictVarEnv;
            evalVarEnv = strictVarEnv;
        }

        context = new ExecutionContext(this, evalLexEnv, evalVarEnv, evalThisBinding, eval.isStrict());
        context.performFunctionDeclarationBindings(eval, true);
        context.performVariableDeclarationBindings(eval, true);
        context.fileName = eval.getFileName();
        return context;
    }

    public ExecutionContext createFunctionExecutionContext(Object functionReference, JSFunction function, Object thisArg, Object... arguments) {
        // 10.4.3
        Object thisBinding = null;
        if (function.isStrict()) {
            thisBinding = thisArg;
        } else {
            if (thisArg == null || thisArg == Types.NULL || thisArg == Types.UNDEFINED) {
                thisBinding = this.getLexicalEnvironment().getGlobalObject();
            } else if (!(thisArg instanceof JSObject)) {
                // thisBinding = Types.toObject(this, thisArg);
                thisBinding = Types.toThisObject(this, thisArg);
            } else {
                thisBinding = thisArg;
            }
        }

        LexicalEnvironment scope = function.getScope();
        LexicalEnvironment localEnv = LexicalEnvironment.newDeclarativeEnvironment(scope);

        ExecutionContext context = new ExecutionContext(this, localEnv, localEnv, thisBinding, function.isStrict());
        context.performDeclarationBindingInstantiation(function, arguments);
        context.fileName = function.getFileName();
        // System.err.println( "debug null: " + ( function.getDebugContext() == null ? function : "not null") );
        context.debugContext = function.getDebugContext();
        context.functionReference = functionReference;
        return context;
    }

    public Completion executeCatch(BasicBlock block, String identifier, Object thrown) {
        // 12.14
        LexicalEnvironment oldEnv = this.lexicalEnvironment;
        LexicalEnvironment catchEnv = LexicalEnvironment.newDeclarativeEnvironment(oldEnv);
        catchEnv.getRecord().createMutableBinding(this, identifier, false);
        catchEnv.getRecord().setMutableBinding(this, identifier, thrown, false);
        try {
            this.lexicalEnvironment = catchEnv;
            return block.call(this);
        } catch (ThrowException e) {
            throw e;
        } catch (Throwable t) {
            throw new ThrowException(this, t);
        } finally {
            this.lexicalEnvironment = oldEnv;
        }
    }

    public Completion executeWith(JSObject withObj, BasicBlock block) {
        LexicalEnvironment oldEnv = this.lexicalEnvironment;
        LexicalEnvironment withEnv = LexicalEnvironment.newObjectEnvironment(withObj, true, oldEnv);
        try {
            this.lexicalEnvironment = withEnv;
            return block.call(this);
        } finally {
            this.lexicalEnvironment = oldEnv;
        }
    }

    private void performDeclarationBindingInstantiation(JSProgram program) {
        performFunctionDeclarationBindings(program, false);
        performVariableDeclarationBindings(program, false);
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

            if (!env.hasBinding(this, names[i])) {
                env.createMutableBinding(this, names[i], false);
            }

            env.setMutableBinding(this, names[i], v, function.isStrict());
        }

        // * 5
        performFunctionDeclarationBindings(function, false);

        // * 6
        if (!env.hasBinding(this, "arguments")) {
            // * 7
            Arguments argsObj = createArgumentsObject(function, arguments);

            if (function.isStrict()) {
                env.createImmutableBinding("arguments");
                env.initializeImmutableBinding("arguments", argsObj);
            } else {
                env.createMutableBinding(this, "arguments", false);
                env.setMutableBinding(this, "arguments", argsObj, false);
            }
        }

        // * 8
        performVariableDeclarationBindings(function, false);
    }

    private Arguments createArgumentsObject(final JSFunction function, final Object[] arguments) {
        // 10.6

        Arguments obj = new Arguments(getGlobalObject());
        PropertyDescriptor desc = new PropertyDescriptor() {
            {
                set(Names.VALUE, arguments.length);
                set(Names.WRITABLE, true);
                set(Names.ENUMERABLE, false);
                set(Names.CONFIGURABLE, true);
            }
        };
        obj.defineOwnProperty(this, "length", desc, false);

        String[] names = function.getFormalParameters();

        JSObject map = new DynObject(getGlobalObject());
        List<String> mappedNames = new ArrayList<>();

        final LexicalEnvironment env = getVariableEnvironment();

        for (int i = 0; i < arguments.length; ++i) {
            final Object val = arguments[i];
            desc = new PropertyDescriptor() {
                {
                    set(Names.VALUE, val);
                    set(Names.WRITABLE, true);
                    set(Names.ENUMERABLE, true);
                    set(Names.CONFIGURABLE, true);
                }
            };

            obj.defineOwnProperty(this, "" + i, desc, false);

            if (i < names.length) {
                if (!function.isStrict()) {
                    final String name = names[i];
                    if (i < names.length) {
                        if (!mappedNames.contains(name)) {
                            mappedNames.add(name);

                            desc = new PropertyDescriptor() {
                                {
                                    set(Names.SET, new ArgSetter(env, name));
                                    set(Names.GET, new ArgGetter(env, name));
                                    set(Names.CONFIGURABLE, true);
                                }
                            };
                            map.defineOwnProperty(this, "" + i, desc, false);
                        }
                    }
                }
            }
        }

        if (!mappedNames.isEmpty()) {
            obj.setParameterMap(map);
        }

        if (function.isStrict()) {
            final JSFunction thrower = (JSFunction) getGlobalObject().get(this, "__throwTypeError");

            obj.defineOwnProperty(this, "caller", new PropertyDescriptor() {
                {
                    set(Names.GET, thrower);
                    set(Names.SET, thrower);
                    set(Names.ENUMERABLE, false);
                    set(Names.CONFIGURABLE, false);
                }
            }, false);

            obj.defineOwnProperty(this, "callee", new PropertyDescriptor() {
                {
                    set(Names.GET, thrower);
                    set(Names.SET, thrower);
                    set(Names.ENUMERABLE, false);
                    set(Names.CONFIGURABLE, false);
                }
            }, false);

        } else {
            obj.defineOwnProperty(this, "callee", new PropertyDescriptor() {
                {
                    set(Names.VALUE, function);
                    set(Names.WRITABLE, true);
                    set(Names.ENUMERABLE, false);
                    set(Names.CONFIGURABLE, true);
                }
            }, false);

        }

        return obj;
    }

    private void performFunctionDeclarationBindings(final JSCode code, final boolean configurableBindings) {
        // 10.5 Function Declaration Binding
        List<FunctionDeclaration> decls = code.getFunctionDeclarations();

        EnvironmentRecord env = this.variableEnvironment.getRecord();
        for (FunctionDeclaration each : decls) {
            String identifier = each.getIdentifier();
            if (!env.hasBinding(this, identifier)) {
                env.createMutableBinding(this, identifier, configurableBindings);
            } else if (env.isGlobal()) {
                JSObject globalObject = ((ObjectEnvironmentRecord) env).getBindingObject();
                PropertyDescriptor existingProp = (PropertyDescriptor) globalObject.getProperty(this, identifier, false);
                if (existingProp.isConfigurable()) {
                    PropertyDescriptor newProp = new PropertyDescriptor() {
                        {
                            set(Names.VALUE, Types.UNDEFINED);
                            set(Names.WRITABLE, true);
                            set(Names.ENUMERABLE, true);
                            set(Names.CONFIGURABLE, configurableBindings);
                        }
                    };
                    globalObject.defineOwnProperty(this, identifier, newProp, true);
                } else if (existingProp.isAccessorDescriptor() || (!existingProp.isWritable() && !existingProp.isEnumerable())) {
                    throw new ThrowException(this, createTypeError("unable to bind function '" + identifier + "'"));
                }
            }
            JSFunction function = getCompiler().compileFunction(this, identifier, each.getFormalParameters(), each.getBlock(), each.isStrict());
            function.setDebugContext(identifier);
            env.setMutableBinding(this, identifier, function, code.isStrict());
        }
    }

    private void performVariableDeclarationBindings(final JSCode code, final boolean configurableBindings) {
        List<VariableDeclaration> decls = code.getVariableDeclarations();

        EnvironmentRecord env = this.variableEnvironment.getRecord();
        for (VariableDeclaration decl : decls) {
            String identifier = decl.getIdentifier();
            if (!env.hasBinding(this, identifier)) {
                env.createMutableBinding(this, identifier, configurableBindings);
                env.setMutableBinding(this, identifier, Types.UNDEFINED, code.isStrict());
            }
        }
    }

    public Config getConfig() {
        return getGlobalObject().getConfig();
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
        return new Reference(getGlobalObject(), propertyName, base, isStrict());
    }

    public Entry retrieveBlockEntry(int statementNumber) {
        return this.lexicalEnvironment.getGlobalObject().retrieveBlockEntry(statementNumber);
    }

    public JSObject createTypeError(String message) {
        return createError("TypeError", message);
    }

    public JSObject createReferenceError(String message) {
        return createError("ReferenceError", message);
    }

    public JSObject createRangeError(String message) {
        return createError("RangeError", message);
    }

    public JSObject createSyntaxError(String message) {
        return createError("SyntaxError", message);
    }

    public JSObject createUriError(String message) {
        return createError("URIError", message);
    }

    public JSObject createError(String type, String message) {
        JSFunction func = (JSFunction) getGlobalObject().get(this, type);
        JSObject err = null;
        if (message == null) {
            err = (JSObject) construct(func);
        } else {
            err = (JSObject) construct(func, message);
        }
        return err;

    }

    public void collectStackElements(List<StackElement> elements) {
        elements.add(new StackElement(this.fileName, this.lineNumber, this.debugContext));
        if (parent != null) {
            parent.collectStackElements(elements);
        }
    }

    public JSObject getPrototypeFor(String type) {
        return getGlobalObject().getPrototypeFor(type);
    }
    
    public String toString() {
        return "ExecutionContext: " + System.identityHashCode( this ) + "; parent=" + this.parent;
    }

}
