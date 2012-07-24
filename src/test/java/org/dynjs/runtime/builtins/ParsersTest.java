package org.dynjs.runtime.builtins;

import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.junit.Test;

public class ParsersTest extends AbstractDynJSTestSupport {

    @Test
    public void parsesFloats() {
        check("var result = parseFloat('33.2');", 33.2);
    }
}
