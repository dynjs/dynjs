package org.dynjs.runtime.modules;

import static org.fest.assertions.Assertions.assertThat;

import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.dynjs.runtime.Types;
import org.junit.Ignore;
import org.junit.Test;

public class UtilModuleTest extends AbstractDynJSTestSupport {
    
    private UtilModule module = new UtilModule();

    @Test
    @Ignore
    public void formatParity() {
        assertThat(module.format("The secret to %s is %d", "the universe", 42)).isEqualTo(String.format("The secret to %s is %d", "the universe", 42));
    }
    
    @Test
    @Ignore
    // http://nodejs.org/api/util.html#util_util_format_format
    public void formatWithExtraArgs() {
        assertThat(module.format("%s:%s", "foo", "bar", "baz")).isEqualTo("foo:bar baz");
    }
    
    @Test
    @Ignore
    public void formatWithNoFormatString() {
        assertThat(module.format(1, 2, 3)).isEqualTo("1 2 3");
    }
    
    @Test
    @Ignore
    public void formatWithSinglePercentSign() {
        assertThat(module.format("%")).isEqualTo("%");
    }

    @Test
    @Ignore
    public void formatEvaluation() {
        assertThat(eval("require('util').format('1 2 3')")).isEqualTo("1 2 3");
    }

    @Test
    @Ignore
    public void testDebug() {
        // Tests only that we don't fail
        assertThat(eval("require('util').debug('message to stderr')")).isEqualTo(Types.UNDEFINED);
    }
    
    @Test
    @Ignore
    public void testError() {
        // Tests only that we don't fail
        assertThat(eval("require('util').error('message to stderr', 'another message')")).isEqualTo(Types.UNDEFINED);
    }
    
    @Test
    @Ignore
    public void testLog() {
        // Tests only that we don't fail
        assertThat(eval("require('util').log('timestamped message')")).isEqualTo(Types.UNDEFINED);
    }

    @Test
    @Ignore
    public void testIsArray() {
        assertThat(eval("require('util').isArray([])")).isEqualTo(true);
        assertThat(eval("require('util').isArray(new Array)")).isEqualTo(true);
        assertThat(eval("require('util').isArray({})")).isEqualTo(false);
    }

    @Test
    @Ignore
    public void testIsRegExp() {
        assertThat(eval("require('util').isRegExp(/some regexp/)")).isEqualTo(true);
        assertThat(eval("require('util').isRegExp(new RegExp('another regexp'))")).isEqualTo(true);
        assertThat(eval("require('util').isRegExp({})")).isEqualTo(false);
    }

    @Test
    @Ignore
    public void testIsDate() {
        assertThat(eval("require('util').isDate(new Date())")).isEqualTo(true);
        assertThat(eval("require('util').isDate(Date())")).isEqualTo(false); // without new, Date() returns a string
        assertThat(eval("require('util').isDate({})")).isEqualTo(false);
    }

    @Test
    @Ignore
    public void testIsError() {
        assertThat(eval("require('util').isError(new Error())")).isEqualTo(true);
        assertThat(eval("require('util').isError(new TypeError())")).isEqualTo(true);
        assertThat(eval("require('util').isError({ name: 'Error', message: 'an error occurred' })")).isEqualTo(false);
    }

}
