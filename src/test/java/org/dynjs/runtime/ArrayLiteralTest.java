package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

public class ArrayLiteralTest extends AbstractDynJSTestSupport {

    @Test
    public void testEmptyArrayCreation() {
        eval("var x = [];");
        final Reference x = getContext().resolve("x");
        assertThat(x).isNotNull();
        assertThat(x.getValue(getContext())).isInstanceOf(DynArray.class);
    }

    /*
     * @Test
     * public void testLiteralArray() {
     * check("var x = [1,2,3]; var result = x[0] == 1", true);
     * check("var x = [1]; x[0] = 2; var result = x[0] == 2", true);
     * check("var x = [1,2]; x[0] = 4; x[1]= 3; var result = x[0] == 4 && x[1] == 3"
     * , true);
     * check("var x = []; x[33] = 'lol'; var result = x[33] == 'lol';");
     * }
     */

    @Test
    public void testNonEmptyArrayCreation() {
        eval("var x = [ 1, 2, 3 ];");
        DynArray x = (DynArray) getContext().resolve("x").getValue(getContext());
        assertThat(x.get(getContext(), "0")).isEqualTo(1);
        assertThat(x.get(getContext(), "1")).isEqualTo(2);
        assertThat(x.get(getContext(), "2")).isEqualTo(3);
    }

    @Test
    public void testArrayAccess() {
        eval("var x = [ 1, 2, 3 ]; var y = x[0];");
        Object y = getContext().resolve("y").getValue(getContext());
        assertThat(y).isEqualTo(1);
    }

    @Test
    public void testArrayAssignmentWithinBounds() {
        eval("var x = [ 1, 2, 3]; x[1] = 'taco';");
        DynArray x = (DynArray) getContext().resolve("x").getValue(getContext());
        assertThat(x.get(getContext(), "0")).isEqualTo(1);
        assertThat(x.get(getContext(), "1")).isEqualTo("taco");
        assertThat(x.get(getContext(), "2")).isEqualTo(3);
    }

    @Test
    public void testArrayAssignmentOutsideOfBounds() {
        eval("var x = [ 1, 2, 3]; x[3] = 'taco';");
        DynArray x = (DynArray) getContext().resolve("x").getValue(getContext());
        assertThat(x.get(getContext(), "0")).isEqualTo(1);
        assertThat(x.get(getContext(), "1")).isEqualTo(2);
        assertThat(x.get(getContext(), "2")).isEqualTo(3);
        assertThat(x.get(getContext(), "3")).isEqualTo("taco");
    }

    @Test
    public void testArrayTruncationUsingLength() {
        eval("var x = [1, 2, 3]; x.length=1;");
        DynArray x = (DynArray) getContext().resolve("x").getValue(getContext());
        assertThat(x.get(getContext(), "0")).isEqualTo(1);
        assertThat(x.get(getContext(), "1")).isEqualTo(Types.UNDEFINED);
        assertThat(x.get(getContext(), "2")).isEqualTo(Types.UNDEFINED);
        assertThat(x.get(getContext(), "3")).isEqualTo(Types.UNDEFINED);
    }

}
