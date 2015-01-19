package org.dynjs.runtime;

import jnr.posix.util.Platform;
import org.dynjs.Config;
import org.dynjs.cli.Options;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.exception.DynJSException;
import org.dynjs.ir.JITCompiler;
import org.dynjs.runtime.util.SafePropertyAccessor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    private GlobalContext globalContext;

    private ExecutionContext defaultExecutionContext;

    public DynJS() {
        this(new Config());
    }

    public DynJS(Config config) {
        this( config, new DynObject() );
    }

    public DynJS(Config config, JSObject globalObject) {
        this.config = config;
        this.compiler = new JSCompiler(config);
        this.jitCompiler = new JITCompiler();
        this.globalContext = GlobalContext.newGlobalContext(this, globalObject);
        this.defaultExecutionContext = ExecutionContext.createDefaultGlobalExecutionContext( this );
        loadKernel();
    }


    private void loadKernel() {
        // FIXME only works for non-IR atm
        if (!Config.CompileMode.IR.equals(this.config.getCompileMode()) && !config.isSandbox()) {
            switch (this.config.getKernelMode()) {
                case INTERNAL:
                    // Load pure-JS kernel
                    this.evaluate(getClass().getResourceAsStream("/dynjs/kernel.js"));
                    break;
                case EXTERNAL:
                    try {
                        FileInputStream stream = new FileInputStream("src/main/resources/dynjs/kernel.js");
                        this.evaluate(stream);
                    } catch (FileNotFoundException e) {
                        throw new DynJSException(e);
                    }
                    break;
            }
        }
    }

    public GlobalContext getGlobalContext() {
        return this.globalContext;
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

    public Runner newRunner() {
        return new Runner(this);
    }

    public Compiler newCompiler() {
        return new Compiler(this.config);
    }

    // ----------------------------------------------------------------------

    public Object execute(String source) {
        return newRunner().withContext( this.defaultExecutionContext ).withSource(source).execute();
    }

    public Object evaluate(String source) {
        return newRunner().withContext( this.defaultExecutionContext ).withSource(source).evaluate();
    }

    public Object evaluate(InputStream in) {
        return newRunner().withContext( this.defaultExecutionContext ).withSource(new InputStreamReader(in)).evaluate();
    }

    public ExecutionContext getDefaultExecutionContext() {
        return this.defaultExecutionContext;
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
