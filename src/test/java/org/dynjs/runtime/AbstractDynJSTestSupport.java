package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.dynjs.Config;
import org.junit.Before;

public abstract class AbstractDynJSTestSupport {
    protected Config config;
    protected JSEngine engine;

    @Before
    public void setUp() {
        this.config = new Config();
        this.engine = new JSEngine( this.config );
    }

    protected void check(String scriptlet) {
        check(scriptlet, true);
    }

    protected void check(String scriptlet, Boolean expected) {
        this.engine.execute( scriptlet, null, 0 );
        Object result = this.engine.getExecutionContext().resolve( "result" );
        assertThat(result).isEqualTo(expected);
    }

    protected void check(String scriptlet, Object expected) {
        this.engine.execute( scriptlet, null, 0 );
        Object result = this.engine.getExecutionContext().resolve( "result" );
        assertThat(result).isEqualTo(expected);
    }

    public JSEngine getEngine() {
        return this.engine;
    }

    public ExecutionContext getContext() {
        return this.engine.getExecutionContext();
    }

    public Config getConfig() {
        return this.engine.getConfig();
    }

    protected Object resultFor(String expression) {
        this.engine.execute( expression, null, 0 );
        return getContext().resolve("result");
    }
}
