package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.dynjs.runtime.JSFunction;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class BuiltinDateTest extends AbstractDynJSTestSupport {

    @Test
    public void testDate() {
        assertThat(eval("Date"))
                .isNotNull()
                .isInstanceOf(JSFunction.class);
    }

    @Test
    public void testDateNow() {
        final long fixedInstant = 1347051329670L;
        DateTimeUtils.setCurrentMillisFixed(fixedInstant);
        assertThat(eval("Date.now()"))
                .isNotNull()
                .isEqualTo(fixedInstant);
        DateTimeUtils.setCurrentMillisSystem();
    }

    @Test
    public void testDateParse() {
        assertThat(eval("Date.parse('2012-09-07T18:00:00.000Z')"))
                .isEqualTo(DateTime.parse("2012-09-07T18:00:00.000Z").toDateTime(DateTimeZone.UTC).getMillis());
    }
}
