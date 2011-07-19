package org.dynjs.runtime;

import org.junit.Test;

import static org.dynjs.runtime.IsUndefined.undefined;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DynObjectTest {

    @Test
    public void returnsUndefinedForUnknownAttributes() {
        final DynObject object = new DynObject();
        assertThat(object.get("prototype"), is(undefined()));
    }
}
