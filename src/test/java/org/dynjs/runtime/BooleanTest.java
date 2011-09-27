package org.dynjs.runtime;

import org.junit.Test;

import static org.dynjs.runtime.DynObject.toBoolean;
import static org.dynjs.runtime.primitives.DynPrimitiveBoolean.FALSE;
import static org.dynjs.runtime.primitives.DynPrimitiveBoolean.TRUE;
import static org.dynjs.runtime.primitives.DynPrimitiveNull.NULL;
import static org.dynjs.runtime.primitives.DynPrimitiveUndefined.UNDEFINED;
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
        assertThat(toBoolean(UNDEFINED)).isEqualTo(FALSE);
    }

    @Test
    public void nullBecomesFalse() {
        assertThat(toBoolean(NULL)).isEqualTo(FALSE);
    }

    @Test
    public void booleanIsNotConverted() {
        assertThat(toBoolean(TRUE)).as("true").isEqualTo(TRUE);

        assertThat(toBoolean(FALSE)).as("false").isEqualTo(FALSE);
    }

    @Test
    public void nanBecomesFalse() {
        assertThat(toBoolean(DynNumber.NAN)).isEqualTo(FALSE);
    }

    @Test
    public void zeroBecomesFalse() {
        assertThat(toBoolean(new DynNumber(0))).isEqualTo(FALSE);
    }

    @Test
    public void anotherValidNumberBecomesTrue() {
        assertThat(toBoolean(new DynNumber(42))).isEqualTo(TRUE);
    }

    @Test
    public void emptyStringBecomesFalse() {
        assertThat(toBoolean(new DynString(""))).isEqualTo(FALSE);
    }

    @Test
    public void nonEmptyStringBecomesTrue(){
        assertThat(toBoolean(new DynString("zzz..."))).isEqualTo(TRUE);
    }

    @Test
    public void objectBecomesTrue(){
        assertThat(toBoolean(new DynObject())).isEqualTo(TRUE);
    }
}