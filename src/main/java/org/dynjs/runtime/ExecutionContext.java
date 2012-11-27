package org.dynjs.runtime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import org.dynjs.Clock;
import org.dynjs.Config;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.exception.ThrowException;
import org.dynjs.parser.ast.FunctionDeclaration;
import org.dynjs.parser.ast.VariableDeclaration;
import org.dynjs.runtime.BlockManager.Entry;

public class ExecutionContext {

    public static ExecutionContext createGlobalExecutionContext(DynJS runtime) {
        // 10.4.1.1
        LexicalEnvironment env = LexicalEnvironment.newGlobalEnvironment(runtime);
        ExecutionContext context = new ExecutionContext(null, env, env, env.getGlobalObject(), false);
        context.clock = runtime.getConfig().getClock();
        context.timeZone = runtime.getConfig().getTimeZone();
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
    private List<CallContext> callContexts = new ArrayList<>();

    private int lineNumber;
    private String fileName;
    private String debugContext = "<eval>";

    private Clock clock;
    private TimeZone timeZone;
    private Object functionReference;

    public ExecutionContext(ExecutionContext parent, LexicalEnvironment lexicalEnvironment, LexicalEnvironment variableEnvironment, Object thisBinding, boolean strict) {
        this.parent = parent;
        this.lexicalEnvironment = lexicalEnvironment;
        this.variableEnvironment = variableEnvironment;
        this.thisBinding = thisBinding;
        this.strict = strict;
        pushCallContext();
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
    }

    public Object eval(JSProgram eval, boolean direct) {
        ExecutionContext evalContext = createEvalExecutionContext(eval, direct);
        Completion result = eval.execute(evalContext);
        return result.value;
    }

    public void incrementPendingConstructorCount() {
        this.callContexts.get(this.callContexts.size() - 1).incrementPendingConstructorCount();
    }

    public void decrementPendingConstructorCount() {
        this.callContexts.get(this.callContexts.size() - 1).decrementPendingConstructorCount();
    }

    public int getPendingConstructorCount() {
        return this.callContexts.get(this.callContexts.size() - 1).getPendingConstructorCount();
    }

    public void pushCallContext() {
        this.callContexts.add(new CallContext());
    }

    public void popCallContext() {
        this.callContexts.remove(this.callContexts.size() - 1);
    }

    public Object call(JSFunction function, Object self, Object... args) {
        return call(null, function, self, args);
    }

    public Object call(Object functionReference, JSFunction function, Object self, Object... args) {
        // 13.2.1
        if (getPendingConstructorCount() > 0) {
            return construct(function, args);
        }

        return internalCall(functionReference, function, self, args);
    }

    public Object construct(JSFunction function, Object... args) {

        if (!function.isConstructor()) {
            throw new ThrowException(this, createTypeError("not a constructor"));
        }

        if (getPendingConstructorCount() > 0) {
            decrementPendingConstructorCount();
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

        ExecutionContext fnContext = createFunctionExecutionContext(functionReference, function, self, args);
        try {
            Object result = function.call(fnContext);
            if (result == null) {
                return Types.UNDEFINED;
            }

            return result;
        } catch (ThrowException e) {
            throw e;
        } catch (Throwable e) {
            throw new ThrowException(fnContext, e);
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
                thisBinding = Types.toObjectForThis(this, thisArg);
            } else {
                thisBinding = thisArg;
            }
        }

        LexicalEnvironment scope = function.getScope();
        LexicalEnvironment localEnv = LexicalEnvironment.newDeclarativeEnvironment(scope);

        ExecutionContext context = new ExecutionContext(this, localEnv, localEnv, thisBinding, function.isStrict());
        context.performDeclarationBindingInstantiation(function, arguments);
        context.fileName = function.getFileName();
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
                set("Value", arguments.length);
                set("Writable", true);
                set("Enumerable", false);
                set("Configurable", true);
            }
        };
        obj.defineOwnProperty(this, "length", desc, false);

        String[] names = function.getFormalParameters();

        DynObject map = new DynObject(getGlobalObject());
        List<String> mappedNames = new ArrayList<>();

        final LexicalEnvironment env = getVariableEnvironment();

        for (int i = arguments.length - 1; i >= 0; --i) {
            final Object val = arguments[i];
            desc = new PropertyDescriptor() {
                {
                    set("Value", val);
                    set("Writable", true);
                    set("Enumerable", true);
                    set("Configurable", true);
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
                                    set("Set", new ArgSetter(env, name));
                                    set("Get", new ArgGetter(env, name));
                                    set("Configurable", true);
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
                    set("Get", thrower);
                    set("Set", thrower);
                    set("Enumerable", false);
                    set("Configurable", false);
                }
            }, false);

            obj.defineOwnProperty(this, "callee", new PropertyDescriptor() {
                {
                    set("Get", thrower);
                    set("Set", thrower);
                    set("Enumerable", false);
                    set("Configurable", false);
                }
            }, false);

        } else {
            obj.defineOwnProperty(this, "callee", new PropertyDescriptor() {
                {
                    set("Value", function);
                    set("Writable", true);
                    set("Enumerable", false);
                    set("Configurable", true);
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
                PropertyDescriptor existingProp = (PropertyDescriptor) globalObject.getProperty(this, identifier);
                if (existingProp.isConfigurable()) {
                    PropertyDescriptor newProp = new PropertyDescriptor() {
                        {
                            set("Value", Types.UNDEFINED);
                            set("Writable", true);
                            set("Enumerable", true);
                            set("Configurable", configurableBindings);
                        }
                    };
                    globalObject.defineOwnProperty(this, identifier, newProp, true);
                } else if (existingProp.isAccessorDescriptor() || (!existingProp.isWritable() && !existingProp.isEnumerable())) {
                    throw new ThrowException(this, createTypeError("unable to bind function '" + identifier + "'"));
                }
            }
            JSFunction function = getCompiler().compileFunction(this, each.getFormalParameters(), each.getBlock(), each.isStrict());
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

}
