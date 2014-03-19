package org.dynjs.runtime;

import org.dynjs.Config;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.runtime.modules.ModuleProvider;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DynJS {

    public static final String VERSION;
    private Config config;
    private JSCompiler compiler;
    private ExecutionContext context;
    private GlobalObject globalObject;

    public DynJS() {
        this(new Config());
    }

    public DynJS(Config config) {
        this.config = config;
        this.compiler = new JSCompiler(config);
        this.globalObject = GlobalObject.newGlobalObject(this);
        this.context = ExecutionContext.createGlobalExecutionContext(this);
    }

    public GlobalObject getGlobalObject() {
        return this.globalObject;
    }

    public Config getConfig() {
        return this.config;
    }

    public JSCompiler getCompiler() {
        return this.compiler;
    }

    public ExecutionContext getExecutionContext() {
        return this.context;
    }

    public Runner newRunner() {
        return new Runner(this.context);
    }

    // ----------------------------------------------------------------------

    public Object execute(String source) {
        return newRunner().withSource(source).execute();
    }

    public Object evaluate(String source) {
        return newRunner().withSource(source).evaluate();
    }

    public Object evaluate(String... sourceLines) {
        StringBuilder buffer = new StringBuilder();

        for (String line : sourceLines) {
            buffer.append(line).append("\n");
        }
        return evaluate(buffer.toString());
    }

    static {
        String version = "undefined";
        try {
            Properties properties = new Properties();
            InputStream stream = DynJS.class.getClassLoader().getResourceAsStream("version.properties");
            if (stream != null) {
                properties.load(stream);
                version = properties.getProperty("git.commit.id.describe");
            }
        } catch (IOException e) {
            // intentionally suppressed
        } finally {
            VERSION = version;
        }
    }
}
