package org.dynjs.runtime;

import org.junit.Test;

import static org.dynjs.runtime.DynNumber.NAN;
import static org.dynjs.runtime.DynNumber.parseInt;
import static org.fest.assertions.Assertions.assertThat;

public class ParseIntTest {

    @Test
    public void emptyStringReturnsNaN(){
        assertThat(parseInt(new DynString(""))).isEqualTo(NAN);
    }

}