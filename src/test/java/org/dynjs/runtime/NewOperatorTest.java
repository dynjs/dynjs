package org.dynjs.runtime;

import org.junit.Test;
import static org.fest.assertions.Assertions.*;

public class NewOperatorTest extends AbstractDynJSTestSupport {

    @Test
    public void testNew() {
        eval("var y;",
                "function Thing(){ y = this };",
                "var x = new Thing();");

        JSObject x = (JSObject) getContext().resolve("x").getValue(getContext());
        JSFunction thing = (JSFunction) getContext().resolve("Thing").getValue(getContext());

        JSObject y = (JSObject) getContext().resolve("y").getValue(getContext());

        assertThat(y).isSameAs(x);
    }

    @Test
    public void testNewSettingThisProps() {
        eval( "function Thing(){ this.taco='fish'; };",
                "var x = new Thing();");

        JSObject x = (JSObject) getContext().resolve("x").getValue(getContext());
        Object result = x.get(getContext(), "taco" );
        assertThat( result ).isEqualTo("fish");
    }

}
