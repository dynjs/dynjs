package org.dynjs.runtime;

import org.junit.Test;

import static org.fest.assertions.Assertions.*;

public class TryCatchFinallyTest extends AbstractDynJSTestSupport {

    @Test
    public void testTryFinally() {
        eval("var executed_try=false;",
                "var executed_finally=false;",
                "try {",
                "  executed_try=true;",
                "} finally {",
                "  executed_finally=true;",
                "}");

        Object execTry = getContext().resolve("executed_try").getValue(getContext());
        Object execFinally = getContext().resolve("executed_finally").getValue(getContext());

        assertThat(execTry).isEqualTo(true);
        assertThat(execFinally).isEqualTo(true);
    }
    
    @Test
    public void testTryCatch() {
        eval("var executed_try=false;",
                "var caught;",
                "try {",
                "  executed_try=true;",
                "  throw 'taco';",
                "} catch(e) {",
                "  caught = e;",
                "}");

        Object execTry = getContext().resolve("executed_try").getValue(getContext());
        Object caught = getContext().resolve("caught").getValue(getContext());

        assertThat(execTry).isEqualTo(true);
        assertThat(caught).isEqualTo("taco");
    }

}
