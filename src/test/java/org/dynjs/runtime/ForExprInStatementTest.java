package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

public class ForExprInStatementTest extends AbstractDynJSTestSupport {

    @Test
    public void testBasicLoop() {
        eval("var x = { a: 1, b:2 }",
                "var e;",
                "for ( e in x ) {",
                "  print(e);",
                "}");
    }

}
