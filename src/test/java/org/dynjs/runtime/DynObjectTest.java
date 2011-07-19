package org.dynjs.runtime;

import org.junit.Before;
import org.junit.Test;

import static org.dynjs.runtime.IsUndefined.undefined;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class DynObjectTest {

    private DynObject object;

    @Before
    public void setUp() throws Exception {
        object = new DynObject();
    }

    @Test
    public void returnsUndefinedForUnknownAttributes() {
        assertThat(object.get("prototype"), is(undefined()));
    }

    @Test
    public void allowsSettingAttributes() {
        final DynObject person = new DynObject();
        object.set("person").to(person);
        assertThat(object.get("person"), is(not(undefined())));
    }
}
