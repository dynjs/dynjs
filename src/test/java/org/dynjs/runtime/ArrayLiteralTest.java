package org.dynjs.runtime;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ArrayLiteralTest extends AbstractDynJSTestSupport {

    @Test
    public void testEmptyArrayCreation() {
        eval("var x = [];");
        final Reference x = getContext().resolve("x");
        assertThat(x).isNotNull();
        assertThat(x.getValue(getContext())).isInstanceOf(DynArray.class);
    }

    @Test
    public void testLiteralArray() {
        check("var x = [1,2,3]; var result = x[0] == 1", true);
        check("var x = [1]; x[0] = 2; var result = x[0] == 2", true);
        check("var x = [1,2]; x[0] = 4; x[1]= 3; var result = x[0] == 4 && x[1] == 3", true);
        check("var x = []; x[33] = 'lol'; var result = x[33] == 'lol';");
    }

}
