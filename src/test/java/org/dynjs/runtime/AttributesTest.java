package org.dynjs.runtime;

import org.junit.Before;
import org.junit.Test;

import static org.dynjs.runtime.helpers.fest.UndefinedCondition.undefined;
import static org.fest.assertions.Assertions.assertThat;


public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() throws Exception {
        attributes = new Attributes();
    }

    @Test
    public void neverReturnsNullValues() throws Exception {
        assertThat(attributes.get("whatever")).is(undefined());
        assertThat(attributes.get("prototype")).is(undefined());
    }

    @Test
    public void allowsAttributionOfValues(){
        attributes.put("test", new Attribute<DynAtom>(new DynObject()));
        assertThat(attributes.get("test"))
                .isNot(undefined())
                .isInstanceOf(Attribute.class);
    }
}
