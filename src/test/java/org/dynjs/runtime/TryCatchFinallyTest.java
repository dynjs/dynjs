package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.dynjs.exception.DynJSException;
import org.junit.Test;

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

    @Test
    public void testTryCatchFinally() {

        eval("var executed_try=false;",
                "var executed_finally;",
                "var caught;",
                "try {",
                "  executed_try=true;",
                "  throw 'taco';",
                "} catch(e) {",
                "  caught = e;",
                "} finally {",
                "  executed_finally=true;",
                "}");

        Object execTry = getContext().resolve("executed_try").getValue(getContext());
        Object caught = getContext().resolve("caught").getValue(getContext());
        Object execFinally = getContext().resolve("executed_finally").getValue(getContext());

        assertThat(execTry).isEqualTo(true);
        assertThat(caught).isEqualTo("taco");
        assertThat(execFinally).isEqualTo(true);
    }

    @Test
    public void testTryCatchFinallyWithCatchThrowing() {
        try {
            eval("var executed_try=false;",
                    "var executed_finally;",
                    "var caught;",
                    "try {",
                    "  executed_try=true;",
                    "  throw 'taco';",
                    "} catch(e) {",
                    "  caught = e;",
                    "  throw 'fish';",
                    "} finally {",
                    "  executed_finally=true;",
                    "}");
            throw new AssertionError("Should have thrown");
        } catch (DynJSException e) {
            assertThat( e.getMessage() ).isEqualTo("fish");
        }

        Object execTry = getContext().resolve("executed_try").getValue(getContext());
        Object caught = getContext().resolve("caught").getValue(getContext());
        Object execFinally = getContext().resolve("executed_finally").getValue(getContext());

        assertThat(execTry).isEqualTo(true);
        assertThat(caught).isEqualTo("taco");
        assertThat(execFinally).isEqualTo(true);
    }
    
    @Test
    public void testTryCatchFinallyWithFinallyThrowing() {
        try {
            eval("var executed_try=false;",
                    "var executed_finally;",
                    "var caught;",
                    "try {",
                    "  executed_try=true;",
                    "  throw 'taco';",
                    "} catch(e) {",
                    "  caught = e;",
                    "} finally {",
                    "  executed_finally=true;",
                    "  throw 'fish';",
                    "}");
            throw new AssertionError("Should have thrown");
        } catch (DynJSException e) {
            assertThat( e.getMessage() ).isEqualTo("fish");
        }

        Object execTry = getContext().resolve("executed_try").getValue(getContext());
        Object caught = getContext().resolve("caught").getValue(getContext());
        Object execFinally = getContext().resolve("executed_finally").getValue(getContext());

        assertThat(execTry).isEqualTo(true);
        assertThat(caught).isEqualTo("taco");
        assertThat(execFinally).isEqualTo(true);
    }

}
