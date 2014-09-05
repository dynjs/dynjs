package org.dynjs.parser.js;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.junit.Test;

import static org.junit.Assert.*;

import static org.fest.assertions.Assertions.*;

/**
 * @author Bob McWhirter
 */
public class StackTraceTest extends AbstractDynJSTestSupport {

    @Test
    public void testStackCreationAccuracy() {
        try {
            Object result = eval(getClass().getResourceAsStream("/stack.js"));
            fail("should have thrown");
        } catch (ThrowException e) {
            StackTraceElement[] stack = e.getStackTrace();
            //for ( int i = 0 ; i < stack.length ; ++i ) {
            //System.err.println( i + ": " + stack[i] );
            //}
            assertThat(stack[0].getLineNumber()).isEqualTo(13);
        }
    }

    @Test
    public void testStackCreationAccuracyWithJavaException() {
        try {
            Object result = eval(getClass().getResourceAsStream("/java-stack.js"));
            fail("should have thrown");
        } catch (ThrowException e) {
            StackTraceElement[] stack = e.getStackTrace();
            //for ( int i = 0 ; i < stack.length ; ++i ) {
            //System.err.println( i + ": " + stack[i] );
            //}
            assertThat(stack[0].getLineNumber()).isEqualTo(13);
        }
    }

    @Test
    public void testStackCreationAccuracyWithWrappedJavaException() {
        try {
            Object result = eval(getClass().getResourceAsStream("/java-stack-wrap.js"));
            fail("should have thrown");
        } catch (ThrowException e) {

            StackTraceElement[] stack = e.getStackTrace();
            assertThat(stack[0].getLineNumber()).isEqualTo(22);

            Throwable cause = e.getCause();
            assertThat(cause).isNotNull();
            stack = cause.getStackTrace();
            assertThat(stack[0].getLineNumber()).isEqualTo(13);
        }
    }

    @Test
    public void testStackWrapping() {
        try {
            eval("var a = 42; a(); ");
            fail( "should have thrown" );
        } catch (ThrowException e) {
            assertThat(e.getCause()).isNull();
        }
    }
}
