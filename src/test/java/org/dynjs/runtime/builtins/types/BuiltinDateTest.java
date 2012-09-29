package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.dynjs.runtime.JSFunction;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.fest.assertions.Assertions.assertThat;

public class BuiltinDateTest extends AbstractDynJSTestSupport {

    private final long fixedInstant = 1347051329670L;
    private final DateTimeZone defaultTimeZone = DateTimeZone.getDefault();

    @Before
    public void setup() {
        DateTimeUtils.setCurrentMillisFixed(fixedInstant);
        DateTimeZone.setDefault(DateTimeZone.forID("Brazil/East"));
        Locale.setDefault(Locale.forLanguageTag("en_US"));
    }

    @After
    public void tearDown() {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(defaultTimeZone);
    }

    @Test
    public void testDate() {
        assertThat(eval("Date"))
                .isNotNull()
                .isInstanceOf(JSFunction.class);
    }

    @Test
    public void testDateNow() {
        assertThat(eval("Date.now()"))
                .isNotNull()
                .isEqualTo(fixedInstant);
    }

    @Test
    public void testDateParse() {
        assertThat(eval("Date.parse('2012-09-07T18:00:00.000Z')"))
                .isEqualTo(DateTime.parse("2012-09-07T18:00:00.000Z").toDateTime(DateTimeZone.UTC).getMillis());
    }

    @Test
    public void testDateValueOf() {
        assertThat(eval("new Date().valueOf()")).isEqualTo(fixedInstant);
    }

    @Test
    public void testDateToISOString() {
        assertThat(eval("new Date().toISOString()")).isEqualTo("2012-09-07T17:55:29.670-03:00");
    }

    @Test
    public void testDateToString() {
        assertThat(eval("new Date().toString()")).isEqualTo("Fri Sep 07 2012 17:55:29 GMT-0300 (BRT)");
    }

    @Test
    public void testDateToDateString() {
        assertThat(eval("new Date().toDateString()")).isEqualTo("Fri Sep 07 2012");
    }

    @Test
    public void testDateToTimeString() {
        assertThat(eval("new Date().toTimeString()")).isEqualTo("17:55:29 GMT-0300 (BRT)");
    }

    @Test
    public void testDateToLocaleString() {
        assertThat(eval("new Date().toLocaleString()")).isEqualTo("Fri Sep 07 17:55:29 2012");
    }

    @Test
    public void testDateToLocaleDateString() {
        assertThat(eval("new Date().toLocaleDateString()")).isEqualTo("09/07/2012");
    }

    @Test
    public void testDateToLocaleTimeString() {
        assertThat(eval("new Date().toLocaleTimeString()")).isEqualTo("17:55:29");
    }

    @Test
    public void testDateGetFullYear() {
        assertThat(eval("new Date().getFullYear()")).isEqualTo(2012);
    }

    @Test
    public void testDateGetMonth() {
        assertThat(eval("new Date().getMonth()")).isEqualTo(9);
    }

    @Test
    public void testDateGetDay() {
        assertThat(eval("new Date().getDay()")).isEqualTo(5);
    }

    @Test
    public void testDateGetHours() {
        assertThat(eval("new Date().getHours()")).isEqualTo(17);
    }

    @Test
    public void testDateGetMinutes() {
        assertThat(eval("new Date().getMinutes()")).isEqualTo(55);
    }

    @Test
    public void testDateGetSeconds() {
        assertThat(eval("new Date().getSeconds()")).isEqualTo(29);
    }

    @Test
    public void testDateGetUTCFullYear() {
        assertThat(eval("new Date().getUTCFullYear()")).isEqualTo(2012);
    }

    @Test
    public void testDateGetUTCMonth() {
        assertThat(eval("new Date().getUTCMonth()")).isEqualTo(9);
    }

    @Test
    public void testDateGetUTCDay() {
        assertThat(eval("new Date().getUTCDay()")).isEqualTo(5);
    }

    @Test
    public void testDateGetUTCHours() {
        assertThat(eval("new Date().getUTCHours()")).isEqualTo(20);
    }

    @Test
    public void testDateGetUTCMinutes() {
        assertThat(eval("new Date().getUTCMinutes()")).isEqualTo(55);
    }

    @Test
    public void testDateGetUTCSeconds() {
        assertThat(eval("new Date().getUTCSeconds()")).isEqualTo(29);
    }

    @Test
    public void testDateGetTimezoneOffset() {
        assertThat(eval("new Date().getTimezoneOffset()")).isEqualTo(-10800000);
    }

    @Test
    public void testDateGetMilliseconds() {
        assertThat(eval("new Date().getMilliseconds()")).isEqualTo(670);
    }

    @Test
    public void testDateSetMilliseconds() {
        assertThat(eval("new Date().setMilliseconds(670)")).isEqualTo(fixedInstant);
    }

    @Test
    public void testDateGetUTCMilliseconds() {
        assertThat(eval("new Date().getUTCMilliseconds()")).isEqualTo(670);
    }

    @Test
    public void testDateSetUTCMilliseconds() {
        assertThat(eval("new Date().setUTCMilliseconds(670)")).isEqualTo(fixedInstant);
    }

    @Test
    public void testDateSetTime() {
        assertThat(eval("new Date().setTime(1347051329670)")).isEqualTo(fixedInstant);
    }

    @Test
    public void testDateSetSeconds() {
        assertThat(eval("new Date().setSeconds(29)")).isEqualTo(fixedInstant);
    }

    @Test
    public void testDateSetUTCSeconds() {
        assertThat(eval("new Date().setUTCSeconds(29)")).isEqualTo(fixedInstant);
    }

    @Test
    public void testDateSetMinutes() {
        assertThat(eval("new Date().setMinutes(55)")).isEqualTo(fixedInstant);
    }

    @Test
    public void testDateSetUTCMinutes() {
        assertThat(eval("new Date().setUTCMinutes(55)")).isEqualTo(fixedInstant);
    }

    @Test
    public void testDateSetHours() {
        assertThat(eval("new Date().setHours(17)")).isEqualTo(fixedInstant);
    }

    @Test
    public void testDateSetUTCHours() {
        assertThat(eval("new Date().setUTCHours(20)")).isEqualTo(fixedInstant);
    }

    @Test
    public void testDateSetDate() {
        assertThat(eval("new Date().setDate(7)")).isEqualTo(fixedInstant);
    }

    @Test
    public void testDateGetDate() {
        assertThat(eval("new Date().getDate()")).isEqualTo(7);
    }

    @Test
    public void testDateSetUTCDate() {
        assertThat(eval("new Date().setUTCDate(7)")).isEqualTo(fixedInstant);
    }

    @Test
    public void testDateGetUTCDate() {
        assertThat(eval("new Date().getUTCDate()")).isEqualTo(7);
    }

    @Test
    public void testDateToUTCString() {
        assertThat(eval("new Date().toUTCString()")).isEqualTo("Fri, 07 Sep 2012 20:55:29 GMT");
    }

    @Test
    public void testDateSetMonth() {
        assertThat(eval("new Date().setMonth(9)")).isEqualTo(fixedInstant);
    }

    @Test
    public void testDateSetUTCMonth() {
        assertThat(eval("new Date().setUTCMonth(9)")).isEqualTo(fixedInstant);
    }
}
