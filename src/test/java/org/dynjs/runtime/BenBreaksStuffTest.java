package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
        getRuntime().newRunner().withSource(new InputStreamReader(coffee))
                .withFileName("coffee-script.js")
                .execute();
        Object value = getRuntime().evaluate("var content = '# Place all the behaviors and hooks related to the matching controller here.\\n' + ",
                "'# All this logic will automatically be available in application.js.\\n' + ",
                "'# You can use CoffeeScript in this file: http://jashkenas.github.com/coffee-script/\\n' + ",
                "'\\n' + ",
                "'class Animal\\n' + ",
                "'  constructor: (@name) ->\\n' + ",
                "'\\n' + ",
                "'  move: (meters) ->\\n' + ",
                "'    alert @name + \" moved #{meters}m.\"\\n' + ",
                "'\\n' + ",
                "'class Snake extends Animal\\n' + ",
                "'  move: ->\\n' + ",
                "'    alert \"Slithering...\"\\n' + ",
                "'    super 5\\n';",
                "CoffeeScript.compile(content);");
        value = getRuntime().evaluate("CoffeeScript.eval('((x) -> x * x)(8)')");
        assertThat(value).isEqualTo(64L);
    }
}
