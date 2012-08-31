package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class SpecTestHarnessWeirdnessTest extends AbstractDynJSTestSupport {

    
    @Test
    public void testWeirdnessOne() throws IOException {
        InputStream in = getClass().getClassLoader().getResourceAsStream( "org/dynjs/runtime/weirdness1.js" );
        getRuntime().execute( in, "weirdness1.js");
    }
}