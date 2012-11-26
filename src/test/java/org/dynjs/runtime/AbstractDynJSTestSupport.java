package org.dynjs.runtime;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Locale;
import java.util.TimeZone;

import org.dynjs.Config;
import org.dynjs.ManualClock;
import org.junit.Before;

public abstract class AbstractDynJSTestSupport {
    
    public final long fixedInstant = 1347051329670L;
    
    protected Config config;
    protected DynJS runtime;

    @Before
    public void setUp() {
        this.config = new Config();
        this.config.setDebug(false);
        config.setClock( new ManualClock( fixedInstant ) );
        config.setTimeZone( TimeZone.getTimeZone( "America/Sao_Paulo" ) );
        config.setLocale( Locale.US );
        this.runtime = new DynJS(this.config);
    }

    protected Object eval(String... lines) {
        return getRuntime().evaluate(lines);
    }

    protected void check(String scriptlet) {
        check(scriptlet, true);
    }

    protected void check(String scriptlet, Boolean expected) {
        this.runtime.execute(scriptlet, null, 0);
        Reference result = this.runtime.getExecutionContext().resolve("result");
        Object value = result.getValue(getContext());
        assertThat(value).isEqualTo(expected);
    }

    protected void check(String scriptlet, Object expected) {
        this.runtime.execute(scriptlet, null, 0);
        Reference result = this.runtime.getExecutionContext().resolve("result");
        Object value = result.getValue(getContext());
        assertThat(value).isEqualTo(expected);
    }

    protected void assertNull(String scriptlet) {
        this.runtime.execute(scriptlet, null, 0);
        Reference result = this.runtime.getExecutionContext().resolve("result");
        assertThat(result.getValue(getContext())).isEqualTo(Types.NULL);
    }

    protected void assertUndefined(String scriptlet) {
        this.runtime.execute(scriptlet, null, 0);
        Reference result = this.runtime.getExecutionContext().resolve("result");
        assertThat(result.getValue(getContext())).isEqualTo(Types.UNDEFINED);
    }

    public DynJS getRuntime() {
        return this.runtime;
    }

    public ExecutionContext getContext() {
        return this.runtime.getExecutionContext();
    }

    public Config getConfig() {
        return this.runtime.getConfig();
    }

    protected Object resultFor(String expression) {
        this.runtime.execute(expression, null, 0);
        return getContext().resolve("result").getValue(getContext());
    }
}
