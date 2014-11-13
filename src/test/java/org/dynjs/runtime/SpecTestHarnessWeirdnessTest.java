package org.dynjs.runtime;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.dynjs.runtime.source.InputStreamSourceProvider;
import org.junit.Test;

public class SpecTestHarnessWeirdnessTest extends AbstractDynJSTestSupport {

    @Test
    public void testWeirdnessOne() throws IOException {
        InputStream in = getClass().getClassLoader().getResourceAsStream("org/dynjs/runtime/weirdness1.js");
        getRuntime().newRunner().withSource(new InputStreamSourceProvider(in)).withFileName("weirdness1.js").execute();
    }
}
