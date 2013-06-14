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

    public DynJS() {
        this(new Config());
    }

    public DynJS(Config config) {
        this.config = config;
        this.compiler = new JSCompiler(config);
        this.context = ExecutionContext.createGlobalExecutionContext(this);
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

    public void clearModuleCache() {
        // This is a hack for vert.x. The CommonJS module spec says we have to cache modules.
        // The cache should exist across execution contexts, so we can't really clear it
        // on GlobalObject or ExecutionContext creation, otherwise we break cyclic dependencies.
        // Vert.x, however, assumes only a single DynJS runtime across all DynJS verticles running
        // in a single Java process. As a result, our static module cache is all of a sudden
        // shared across ALL verticles in a single process, and this breaks, at a minimum, the
        // lang-dynjs integration tests. So... the solution for now is to provide a way for
        // the DynJSVerticleFactory to clear the cache every time a new verticle is stood up.
        ModuleProvider.clearCache();
    }

}
