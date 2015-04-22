package org.dynjs.runtime;

import org.dynjs.Clock;
import org.dynjs.Config;
import org.dynjs.compiler.CompilationContext;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.debugger.Debugger;
import org.dynjs.debugger.js.DebuggerAPI;
import org.dynjs.exception.ThrowException;
import org.dynjs.ir.IRJSFunction;
import org.dynjs.ir.JITCompiler;
import org.dynjs.parser.Statement;
import org.dynjs.parser.ast.FunctionDeclaration;
import org.dynjs.parser.ast.VariableDeclaration;
import org.dynjs.runtime.BlockManager.Entry;
import org.dynjs.runtime.builtins.types.error.StackElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ExecutionContext implements CompilationContext {


    public static ExecutionContext createGlobalExecutionContext(DynJS runtime) {
        // 10.4.1.1
        LexicalEnvironment env = LexicalEnvironment.newGlobalEnvironment(runtime);
        return new ExecutionContext(runtime, null, env, env, runtime.getGlobalContext().getObject(), false);
    }

    public static ExecutionContext createDefaultGlobalExecutionContext(DynJS runtime) {
        // 10.4.1.1
        LexicalEnvironment env = LexicalEnvironment.newGlobalEnvironment(runtime);
        ExecutionContext context = new ExecutionContext(runtime, null, env, env, runtime.getGlobalContext().getObject(), false);
        context.blockManager = new BlockManager();
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

    private SourceProvider source;

    private DynJS runtime;
    private ExecutionContext parent;
    private LexicalEnvironment lexicalEnvironment;
    private LexicalEnvironment variableEnvironment;
    private Object thisBinding;
    private boolean strict;
    private boolean inEval;
    private ResourceQuota.WatchDog resourceWatchDog;

    private int lineNumber;
    private int columnNumber;
    private String fileName;
    private String debugContext = "<eval>";
    private VariableValues vars;

    // Just stash functions passed into this context with no processing.  IRScope will detect things like 'attributes' and
    // generate those on a case-by-case basis.
    private Object[] functionParameters;

    private Object functionReference;
    private JSFunction function;
    private boolean isConstructor;

    private List<StackElement> throwStack;

    private BlockManager blockManager;
    private Debugger debugger;

    public ExecutionContext(DynJS runtime, ExecutionContext parent, LexicalEnvironment lexicalEnvironment, LexicalEnvironment variableEnvironment, Object thisBinding, boolean strict) {
        this.runtime = runtime;
        this.parent = parent;
        this.lexicalEnvironment = lexicalEnvironment;
        this.variableEnvironment = variableEnvironment;
        this.thisBinding = thisBinding;
        this.strict = strict;

        if (parent != null) {
            this.resourceWatchDog = parent.resourceWatchDog;
        }
    }

    public void setFunctionParameters(Object[] args) {
        this.functionParameters = args;
    }

    public Object[] getFunctionParameters() {
        return functionParameters;
    }

    public Object getFunctionParameter(int offset) {
        return getFunctionParameters()[offset];
    }

    public JSFunction getFunction() {
        return this.function;
    }

    public boolean isConstructor() {
        return this.isConstructor;
    }

    public VariableValues getVars() {
        return vars;
    }

    public VariableValues allocVars(int size, VariableValues parent) {
        vars = new VariableValues(size, parent);

        return vars;
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

    public boolean inEval() {
        return this.inEval;
    }

    public boolean isDebug() {
        return getDebugger() != null;
    }

    public void debug(Statement statement, Statement previousStatement) {
        Debugger d = getDebugger();
        if (d != null) {
            try {
                d.debug(this, statement, previousStatement);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Clock getClock() {
        return this.runtime.getConfig().getClock();
    }

    public TimeZone getTimeZone() {
        return this.runtime.getConfig().getTimeZone();
    }

    public Locale getLocale() {
        return this.runtime.getConfig().getLocale();
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

    private void setupDebugger(Debugger debugger) {
        if (debugger == null) {
            return;
        }

        this.debugger = debugger;
        JSObject globalObject = getGlobalContext().getObject();
        String debuggerName = getRuntime().getConfig().getExposeDebugAs();
        Object currentDebugger = globalObject.get(this, debuggerName);
        if (!(currentDebugger instanceof DebuggerAPI)) {
            DebuggerAPI api = new DebuggerAPI(getGlobalContext(), debugger);
            globalObject.put(this, debuggerName, api, false);
        }
    }

    public Debugger getDebugger() {
        if (this.debugger != null) {
            return this.debugger;
        }
        if (this.parent != null) {
            return this.parent.getDebugger();
        }
        return null;
    }

    public SourceProvider getSource() {
        if ( this.source != null ) {
            return this.source;
        }

        if ( this.parent != null ) {
            return this.parent.getSource();
        }
        return null;
    }

    public Completion execute(JSProgram program) {
        return execute(program, null);
    }

    public Completion execute(JSProgram program, Debugger debugger) {
        this.source = program.getSource();
        this.fileName = program.getFileName();
        //System.err.println( this.fileName + " >> " + this.source );
        BlockManager originalBlockManager = this.blockManager;
        try {
            setupDebugger(debugger);
            this.blockManager = program.getBlockManager();
            ThreadContextManager.pushContext(this);
            setStrict(program.isStrict());
            performDeclarationBindingInstantiation(program);
            try {
                return program.execute(this);
            } catch (ThrowException e) {
                throw e;
            }
        } finally {
            ThreadContextManager.popContext();
            this.blockManager = originalBlockManager;
        }
    }

    public Object eval(JSProgram eval, boolean direct) {
        return eval(eval, direct, null);
    }

    public Object eval(JSProgram eval, boolean direct, Debugger debugger) {
        BlockManager originalBlockManager = this.blockManager;
        try {
            setupDebugger(debugger);
            ExecutionContext evalContext = createEvalExecutionContext(eval, direct);
            evalContext.blockManager = eval.getBlockManager();
            ThreadContextManager.pushContext(evalContext);
            Completion result = eval.execute(evalContext);
            return result.value;
        } finally {
            ThreadContextManager.popContext();
            this.blockManager = originalBlockManager;
        }
    }

    public Object call(JSFunction function, Object self, Object... args) {
        return call(null, function, self, args);
    }

    public Object call(Object functionReference, JSFunction function, Object self, Object... args) {
        return call( false, functionReference, function, self, args );
    }

    public Object call(boolean isConstructor, Object functionReference, JSFunction function, Object self, Object... args) {
        // 13.2.1
        ExecutionContext fnContext = null;
        try {
            fnContext = createFunctionExecutionContext(isConstructor, functionReference, function, self, args);
            ThreadContextManager.pushContext(fnContext);
            try {
                Object value = function.call(fnContext);
                if (value == null) {
                    return Types.NULL;
                }
                return value;
            } catch (ThrowException e) {
                throw e;
                //} catch (Throwable e) {
//                throw new ThrowException(fnContext, e);
            }
        } catch (ThrowException t) {
            if (t.getCause() != null) {
                recordThrow(t.getCause(), fnContext);
            } else if (t.getValue() instanceof Throwable) {
                recordThrow((Throwable) t.getValue(), fnContext);
            }
            throw t;
        } catch (Throwable t) {
            recordThrow(t, fnContext);
            throw t;
        } finally {
            ThreadContextManager.popContext();
        }
    }

    public Object construct(Reference reference, Object... args) {
        Object value = reference.getValue(this);
        if (value instanceof JSFunction) {
            return construct(reference, (JSFunction) value, args);
        }
        return null;

    }

    //public Object construct(JSFunction function, Object... args) {
    //return construct( null, function, args );
    //}

    public Object construct(Object reference, JSFunction function, Object... args) {
        if (!function.isConstructor()) {
            throw new ThrowException(this, createTypeError("not a constructor"));
        }

        Object ctorName = function.get(this, "name");
        if (ctorName == Types.UNDEFINED) {
            if (reference instanceof Reference) {
                ctorName = ((Reference) reference).getReferencedName();
            } else {
                ctorName = function.getDebugContext();
            }
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
        Object result = call(true, reference, function, obj, args);
        // 9. If result is a JSObject return it

        if (result instanceof JSObject) {
            obj = (JSObject) result;
        }

        // Otherwise return obj
        return obj;
    }

    protected void recordThrow(Throwable t, ExecutionContext fnContext) {
        if (this.throwStack == null) {
            this.throwStack = new ArrayList<StackElement>();
            this.throwStack.add(fnContext.getStackElement());
        } else {
            // handled lower
        }
    }

    public boolean isThrowInProgress() {
        return this.throwStack != null && !this.throwStack.isEmpty();
    }

    public List<StackElement> getThrowStack() {
        return this.throwStack;
    }

    public void addThrowStack(List<StackElement> throwStack) {
        if (this.throwStack == null) {
            this.throwStack = new ArrayList<>();
        }
        this.throwStack.addAll(throwStack);
    }

    // ----------------------------------------------------------------------

    public ExecutionContext createEvalExecutionContext(JSProgram eval, boolean direct) {
        // 10.4.2 (with caller)
        //System.err.println( "CREATE EVAL EXEC CONTEXT" );
        ExecutionContext context = null;

        Object evalThisBinding = null;
        LexicalEnvironment evalLexEnv = null;
        LexicalEnvironment evalVarEnv = null;

        if (!direct) {
            evalThisBinding = getGlobalContext().getObject();
            evalLexEnv = LexicalEnvironment.newGlobalEnvironment(getGlobalContext().getObject());
            evalVarEnv = LexicalEnvironment.newGlobalEnvironment(getGlobalContext().getObject());
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

        context = new ExecutionContext(this.runtime, this, evalLexEnv, evalVarEnv, evalThisBinding, eval.isStrict());
        context.source = eval.getSource();
        context.fileName = eval.getFileName();
        //System.err.println( context.fileName + " >> " + context.source );
        context.performFunctionDeclarationBindings(eval, true);
        context.performVariableDeclarationBindings(eval, true);
        return context;
    }


    public ExecutionContext createFunctionExecutionContext(boolean isConstructor, Object functionReference, JSFunction function, Object thisArg, Object... arguments) {
        // 10.4.3
        Object thisBinding = null;
        if (function.isStrict()) {
            thisBinding = thisArg;
        } else {
            if (thisArg == null || thisArg == Types.NULL || thisArg == Types.UNDEFINED) {
                thisBinding = getGlobalContext().getObject();
            } else if (!(thisArg instanceof JSObject)) {
                // thisBinding = Types.toObject(this, thisArg);
                thisBinding = Types.toThisObject(this, thisArg);
            } else {
                thisBinding = thisArg;
            }
        }

        LexicalEnvironment scope = function.getScope();
        LexicalEnvironment localEnv = LexicalEnvironment.newDeclarativeEnvironment(scope);

        ExecutionContext context = new ExecutionContext(this.runtime, this, localEnv, localEnv, thisBinding, function.isStrict());
        context.isConstructor = isConstructor;
        context.source = function.getSource();
        context.fileName = function.getFileName();
        if (!(function instanceof IRJSFunction && !(function instanceof JITCompiler.CompiledFunction))) {
            context.performDeclarationBindingInstantiation(function, arguments);
        }
        // System.err.println( "debug null: " + ( function.getDebugContext() == null ? function : "not null") );
        context.debugContext = function.getDebugContext();
        context.functionReference = functionReference;
        context.source = function.getSource();
        context.function = function;
        //System.err.println( "fnContext: " + context.debugContext + " // " + context.source );
        context.setFunctionParameters(arguments);
        return context;
    }

    public ExecutionContext createResourceQuotaExecutionObject(ResourceQuota quota) {
        ExecutionContext context = new ExecutionContext(this.runtime, this, this.lexicalEnvironment, this.variableEnvironment, this.thisBinding, this.strict);
        context.resourceWatchDog = quota.createWatchDog();

        return context;
    }

    public Completion executeCatch(BasicBlock block, String identifier, Object thrown) {
        // 12.14
        if (thrown instanceof Throwable && this.throwStack != null && !this.throwStack.isEmpty()) {
            StackTraceElement[] originalStack = ((Throwable) thrown).getStackTrace();
            List<StackTraceElement> newStack = new ArrayList<>();
            for (int i = 0; i < originalStack.length; ++i) {
                String cn = originalStack[i].getClassName();
                if (cn.startsWith("org.dynjs") || cn.startsWith("java.lang.invoke")) {
                    break;
                }
                newStack.add(originalStack[i]);
            }
            int throwLen = this.throwStack.size();
            for (int i = throwLen - 1; i >= 0; --i) {
                newStack.add(throwStack.get(i).toStackTraceElement());
            }
            ((Throwable) thrown).setStackTrace(newStack.toArray(new StackTraceElement[0]));
        }
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

            env.assignMutableBinding(this, names[i], v, false, function.isStrict());
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

        Arguments obj = new Arguments(getGlobalContext());
        obj.defineOwnProperty(this, "length",
                PropertyDescriptor.newDataPropertyDescriptor(arguments.length, true, true, false), false);

        String[] names = function.getFormalParameters();

        JSObject map = new DynObject(getGlobalContext());
        List<String> mappedNames = new ArrayList<>();

        final LexicalEnvironment env = getVariableEnvironment();

        for (int i = 0; i < arguments.length; ++i) {
            final Object val = arguments[i];

            obj.defineOwnProperty(this, "" + i,
                    PropertyDescriptor.newDataPropertyDescriptor(val, true, true, true), false);

            if (i < names.length) {
                if (!function.isStrict()) {
                    final String name = names[i];
                    if (i < names.length) {
                        if (!mappedNames.contains(name)) {
                            mappedNames.add(name);

                            PropertyDescriptor desc = new PropertyDescriptor();
                            desc.setSetter(new ArgSetter(getGlobalContext(), env, name));
                            desc.setGetter(new ArgGetter(getGlobalContext(), env, name));
                            desc.setConfigurable(true);
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
            final JSFunction thrower = getGlobalContext().getThrowTypeError();

            obj.defineOwnProperty(this, "caller",
                    PropertyDescriptor.newAccessorPropertyDescriptor(thrower, thrower), false);
            obj.defineOwnProperty(this, "callee",
                    PropertyDescriptor.newAccessorPropertyDescriptor(thrower, thrower), false);

        } else {
            obj.defineOwnProperty(this, "callee",
                    PropertyDescriptor.newDataPropertyDescriptor(function, true, true, false), false);
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
                    globalObject.defineOwnProperty(this, identifier,
                            PropertyDescriptor.newDataPropertyDescriptor(Types.UNDEFINED, true, configurableBindings, true), true);
                } else if (existingProp.isAccessorDescriptor() || (!existingProp.isWritable() && !existingProp.isEnumerable())) {
                    throw new ThrowException(this, createTypeError("unable to bind function '" + identifier + "'"));
                }
            }
            JSFunction function = getCompiler().compileFunction(this, identifier, each.getFormalParameters(), each.getBlock(), each.isStrict());
            //System.err.println( identifier + " >> " + function.getFileName() + " >> " + getSource() );
            function.setSource(code.getSource());
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
        return this.runtime.getConfig();
    }

    public GlobalContext getGlobalContext() {
        return this.runtime.getGlobalContext();
    }

    public JSCompiler getCompiler() {
        return this.runtime.getCompiler();
    }

    public BlockManager getBlockManager() {
        if (this.blockManager != null) {
            return this.blockManager;
        }

        if (this.parent != null) {
            return this.parent.getBlockManager();
        }

        return null;
    }

    public DynJS getRuntime() {
        return this.runtime;
    }

    public Reference createPropertyReference(Object base, String propertyName) {
        return new Reference(propertyName, base, isStrict());
    }

    public Entry retrieveBlockEntry(int statementNumber) {
        return getBlockManager().retrieve(statementNumber);
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
        JSFunction func = getGlobalContext().getType(type);
        JSObject err = null;
        if (message == null) {
            err = (JSObject) construct((Object) null, func);
        } else {
            err = (JSObject) construct((Object) null, func, message);
        }
        err.put(this, "__native", true, false);
        return err;

    }

    public void collectStackElements(List<StackElement> elements) {
        elements.add(getStackElement());
        if (parent != null) {
            parent.collectStackElements(elements);
        }
    }

    public StackElement getStackElement() {
        String locationContext = this.debugContext;
        if (locationContext.equals("<anonymous>")) {
            if (this.functionReference != null && this.functionReference instanceof Reference) {
                locationContext = ((Reference) this.functionReference).getReferencedName();
            }
        }
        return new StackElement(locationContext, this);
    }

    public JSObject getPrototypeFor(String type) {
        return getGlobalContext().getPrototypeFor(type);
    }

    public String toString() {
        return "ExecutionContext: " + System.identityHashCode(this);// + "; parent=" + this.parent;
    }

    public DynamicClassLoader getClassLoader() {
        return getRuntime().getConfig().getClassLoader();
    }

    public void setColumnNumber(int column) {
        this.columnNumber = column;
    }

    public int getColumnNumber() {
        return this.columnNumber;
    }

    public void inEval(boolean b) {
        this.inEval = b;
    }

    public void checkResourceUsage() {
        if (this.resourceWatchDog != null) {
            this.resourceWatchDog.check();
        }
    }
}
