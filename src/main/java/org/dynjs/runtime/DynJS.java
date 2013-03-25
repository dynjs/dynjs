package org.dynjs.runtime;

import org.dynjs.Config;
import org.dynjs.compiler.JSCompiler;

import java.io.IOException;
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
        return new Runner( this.context );
    }

    // ----------------------------------------------------------------------

    public Object execute(String source) {
        return newRunner().withSource(source).execute();
    }

    public Object evaluate(String source) {
        return newRunner().withSource(source).evaluate();
    }

    public Object evaluate(String...sourceLines) {
        StringBuffer buffer = new StringBuffer();

        for ( String line : sourceLines ) {
            buffer.append( line ).append( "\n" );
        }
        return evaluate( buffer.toString() );
    }
    static {
        try {
            Properties properties = new Properties();
            properties.load(DynJS.class.getClassLoader().getResourceAsStream("version.properties"));
            VERSION = properties.getProperty("git.commit.id.describe");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
