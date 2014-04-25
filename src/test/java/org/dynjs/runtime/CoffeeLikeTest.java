package org.dynjs.runtime;

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.fest.assertions.Assertions.assertThat;

public class CoffeeLikeTest extends AbstractDynJSTestSupport {

    @Test
    public void testImmediatelyCalledConstructorFunction() {
        JSObject result = (JSObject) eval(getClass().getResourceAsStream("/coffeelike.js"));

        assertThat( result ).isNotNull();

        JSObject dangit = (JSObject) result.get( null, "dangit" );

        assertThat( dangit ).isNotNull();

        assertThat( dangit.get( null, "taco" ) ).isEqualTo( 42L );
    }

}
