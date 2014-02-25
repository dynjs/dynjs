package org.dynjs.runtime;

import org.junit.Test;

public class ForVarDeclOfStatementTest extends AbstractDynJSTestSupport {

    @Test
    public void testBasicLoop() {
        eval("var x = { a: 1, b:2 }",
                "for ( var e of x ) {",
                "  print(e);",
                "}");
    }

}
