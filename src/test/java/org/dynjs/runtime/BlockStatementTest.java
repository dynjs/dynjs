package org.dynjs.runtime;

import org.junit.Test;

public class BlockStatementTest extends AbstractDynJSTestSupport {

    @Test
    public void testOneLine() {
        eval("void true;");
    }

}
