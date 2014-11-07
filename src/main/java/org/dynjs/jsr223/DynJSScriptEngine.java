package org.dynjs.jsr223;

import org.dynjs.runtime.Compiler;
import org.dynjs.runtime.*;
import org.dynjs.runtime.linker.DynJSBootstrapper;
import org.dynjs.runtime.linker.java.jsimpl.JSJavaImplementationManager;
import org.dynjs.runtime.source.ReaderSourceProvider;

import javax.script.*;
import java.io.IOException;
import java.io.Reader;

/**
 * @author Bob McWhirter
 */
public class DynJSScriptEngine extends AbstractScriptEngine implements Compilable, Invocable {

    public static final String GLOBAL_OBJECT = "org.dynjs.global-object";

    private final DynJSScriptEngineFactory factory;

    DynJSScriptEngine(DynJSScriptEngineFactory factory) {
        this.factory = factory;
        this.context = new SimpleScriptContext();
        this.context.setBindings(factory.getGlobalBindings(), ScriptContext.GLOBAL_SCOPE);
    }

    @Override
    public DynJSCompiledScript compile(String script) throws ScriptException {
        Compiler compiler = new DynJS().newCompiler();
        try {
            JSProgram program = compiler.withSource(script).compile();
            return new DynJSCompiledScript(this, program);
        } catch (IOException e) {
            throw new ScriptException(e);
        }
    }

    @Override
    public DynJSCompiledScript compile(Reader script) throws ScriptException {
        Compiler compiler = new DynJS().newCompiler();
        try {
            JSProgram program = compiler.withSource( new ReaderSourceProvider(script) ).compile();
            return new DynJSCompiledScript(this, program);
        } catch (IOException e) {
            throw new ScriptException(e);

        }
    }

    @Override
    public Object eval(String script, ScriptContext context) throws ScriptException {
        DynJSCompiledScript program = compile(script);
        return program.eval(context);

        //ScriptEngineGlobalObject global = getGlobalObject( context );
        //DynJS runtime = getRuntime( global, context );
        //return runtime.newRunner().withSource( script ).execute();
    }

    @Override
    public Object eval(Reader reader, ScriptContext context) throws ScriptException {
        DynJSCompiledScript program = compile(reader);
        return program.eval(context);
    }

    @Override
    public Bindings createBindings() {
        return new SimpleBindings();
    }

    @Override
    public ScriptEngineFactory getFactory() {
        return this.factory;
    }

    @Override
    public Object invokeMethod(Object thiz, String name, Object... args) throws ScriptException, NoSuchMethodException {
        if (!(thiz instanceof JSObject)) {
            throw new IllegalArgumentException("'this' should be an instance of " + JSObject.class.getName());
        }

        ScriptEngineGlobalObject global = RuntimeHelper.getGlobalObject(this.context);
        DynJS runtime = RuntimeHelper.getRuntime(global, this.context);

        Object property = ((JSObject) thiz).get(runtime.getDefaultExecutionContext(), name);

        if (property == Types.UNDEFINED) {
            throw new NoSuchMethodException("No such property '" + name + "'");
        }

        if (!(property instanceof JSFunction)) {
            throw new ScriptException("Property '" + name + "' is not a function");
        }

        JSFunction function = (JSFunction) property;

        return runtime.getDefaultExecutionContext().call(function, thiz, args);
    }

    @Override
    public Object invokeFunction(String name, Object... args) throws ScriptException, NoSuchMethodException {
        ScriptEngineGlobalObject global = RuntimeHelper.getGlobalObject(this.context);

        return invokeMethod(global, name, args);
    }

    @Override
    public <T> T getInterface(Class<T> clasz) {
        ScriptEngineGlobalObject global = RuntimeHelper.getGlobalObject(this.context);

        return getInterface(global, clasz);
    }

    @Override
    public <T> T getInterface(Object thiz, Class<T> clasz) {
        if (!(thiz instanceof JSObject)) {
            throw new IllegalArgumentException("'this' should be an instance of " + JSObject.class.getName());
        }

        ScriptEngineGlobalObject global = RuntimeHelper.getGlobalObject(this.context);
        DynJS runtime = RuntimeHelper.getRuntime(global, this.context);

        JSJavaImplementationManager manager = DynJSBootstrapper.JAVA_IMPLEMENTATION_MANAGER;

        try {
            return ((T) manager.getImplementationWrapper(clasz, runtime.getDefaultExecutionContext(), (JSObject) thiz));
        } catch (Exception e) {
            return null;
        }
    }
}
