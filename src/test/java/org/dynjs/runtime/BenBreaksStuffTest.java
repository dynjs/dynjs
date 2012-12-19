package org.dynjs.runtime;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Ignore;
import org.junit.Test;

public class BenBreaksStuffTest extends AbstractDynJSTestSupport {

    @Test
    public void testBenComplainsAboutStuff1() {

        StringBuffer foo = new StringBuffer();
        foo.append("function foo(){\n");

        for (int i = 0; i < 100000; ++i) {
            foo.append("var foo='bar';\n");
        }

        foo.append("};\n");

        StringBuffer wrapper = new StringBuffer();

        wrapper.append("((function(){");
        wrapper.append(foo);
        wrapper.append("return true;})())");

        // System.err.println( wrapper );
        eval(wrapper.toString());
    }

    @Test
    public void testBenComplainsAboutCoffeeScript() throws IOException {
        InputStream coffee = getClass().getResourceAsStream("coffee-script.js");
        //getRuntime().execute(coffee, "coffee-script.js");
        getRuntime().newRunner().withSource(new InputStreamReader(coffee))
                .withFileName("coffee-script.js")
                .execute();
    }
}
