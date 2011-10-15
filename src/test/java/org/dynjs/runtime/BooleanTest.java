package org.dynjs.runtime;

import org.junit.Test;

import static org.dynjs.runtime.DynObject.toBoolean;
import static org.dynjs.runtime.primitives.DynPrimitiveNull.NULL;
import static org.dynjs.runtime.DynThreadContext.UNDEFINED;
import static org.fest.assertions.Assertions.assertThat;

public class BooleanTest {
    // http://es5.github.com/#x9.2
    // undefined    => false
    // null         => false
    // Boolean      => no conversion
    // Number       => 0, NaN = false
    // String       => "" = false
    // Object       => true

    @Test
    public void undefinedBecomesFalse() {
        assertThat(toBoolean(UNDEFINED)).isFalse();
    }

    @Test
    public void nullBecomesFalse() {
        assertThat(toBoolean(NULL)).isFalse();
    }

    @Test
    public void booleanIsNotConverted() {
        assertThat(toBoolean(true)).as("true").isTrue();

        assertThat(toBoolean(false)).as("false").isFalse();
    }

    @Test
    public void nanBecomesFalse() {
        assertThat(toBoolean(DynNumber.NAN)).isFalse();
    }

    @Test
    public void zeroBecomesFalse() {
        assertThat(toBoolean(new DynNumber(0))).isFalse();
    }

    @Test
    public void anotherValidNumberBecomesTrue() {
        assertThat(toBoolean(new DynNumber(42))).isTrue();
    }

    @Test
    public void emptyStringBecomesFalse() {
        assertThat(toBoolean(new DynString(""))).isFalse();
    }

    @Test
    public void nonEmptyStringBecomesTrue() {
        assertThat(toBoolean(new DynString("zzz..."))).isTrue();
    }

    @Test
    public void objectBecomesTrue() {
        assertThat(toBoolean(new DynObject())).isTrue();
    }
}