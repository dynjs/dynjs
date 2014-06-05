package org.dynjs.runtime;

import jnr.posix.util.Platform;
import org.dynjs.Config;
import org.dynjs.cli.Options;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.ir.JITCompiler;
import org.dynjs.runtime.modules.ModuleProvider;
import org.dynjs.runtime.util.SafePropertyAccessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class DynJS {

    public static final String VERSION;
    public static final String VERSION_STRING;
    private final JITCompiler jitCompiler;
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
        this.jitCompiler = new JITCompiler();
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

    public JITCompiler getJitCompiler() {
        return jitCompiler;
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

    public Object evaluate(InputStream in) {
        return newRunner().withSource( new InputStreamReader( in ) ).evaluate();
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
        String fullVersion = "";
        try {
            Properties properties = new Properties();
            InputStream stream = DynJS.class.getClassLoader().getResourceAsStream("version.properties");
            if (stream != null) {
                properties.load(stream);
                version = properties.getProperty("git.commit.id.describe");
            }
            fullVersion = String.format(
                    "dynjs %s on %s %s%s [%s-%s]",
                    version,
                    SafePropertyAccessor.getProperty("java.vm.name", "Unknown JVM"),
                    SafePropertyAccessor.getProperty("java.runtime.version", SafePropertyAccessor.getProperty("java.version", "Unknown version")),
                    Options.INVOKEDYNAMIC.load() ? " +indy" : "",
                    Platform.getOSName(),
                    SafePropertyAccessor.getProperty("os.arch", "Unknown arch")
            );

        } catch (IOException e) {
            // intentionally suppressed
        } finally {
            VERSION = version;
            VERSION_STRING = fullVersion;
        }
    }
}
