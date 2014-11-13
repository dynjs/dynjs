package org.dynjs.runtime;

import org.dynjs.runtime.linker.DynJSBootstrapper;
import org.dynjs.runtime.source.InputStreamSourceProvider;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.fest.assertions.Assertions.assertThat;

public class BenBreaksStuffTest extends AbstractDynJSTestSupport {

    @After
    public void dumpStatistics() {
        //DynJSBootstrapper.LINKER.dumpStatistics();
    }

    @Test
    public void testBenComplainsAboutStuff1() {

        StringBuilder foo = new StringBuilder();
        foo.append("function foo(){\n");

        for (int i = 0; i < 100000; ++i) {
            foo.append("var foo='bar';\n");
        }

        foo.append("};\n");

        StringBuilder wrapper = new StringBuilder();

        wrapper.append("((function(){");
        wrapper.append(foo);
        wrapper.append("return true;})())");

        // System.err.println( wrapper );
        eval(wrapper.toString());
    }

    @Test
    public void testBenComplainsAboutCoffeeScript() throws IOException {
        InputStream coffee = getClass().getResourceAsStream("coffee-script.js");
        getRuntime().newRunner().withSource(new InputStreamSourceProvider(coffee))
                .withFileName("coffee-script.js")
                .execute();
        Object value = getRuntime().evaluate("CoffeeScript.eval('((x) -> x * x)(8)')");
        assertThat(value).isEqualTo(64L);
    }

    @Test
    @Ignore
    public void testBenComplainsAboutLongerCoffeeScript() throws IOException {
        InputStream coffee = getClass().getResourceAsStream("coffee-script.js");
        getRuntime().newRunner().withSource(new InputStreamSourceProvider(coffee))
                .withFileName("coffee-script.js")
                .execute();
        getRuntime().evaluate("var content = '# Place all the behaviors and hooks related to the matching controller here.\\n' + ",
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
    }
}
