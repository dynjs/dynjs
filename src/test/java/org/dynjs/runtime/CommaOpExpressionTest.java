package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.junit.Ignore;
import org.junit.Test;

public class CommaOpExpressionTest extends AbstractDynJSTestSupport {

    @Test
    public void testSimpleCommaOp() {
        Object result = eval( "(42, 'taco')");
        assertThat( result).isEqualTo("taco");
    }
    
    @Test
    @Ignore
    public void testCommaOpWithEval() {
        Object result = eval( "(42,eval)('\"taco\"')");
        assertThat( result).isEqualTo("taco");
    }
    
    @Test
    public void testNonCommaOpWithEval() {
        Object result = eval( "eval('\"taco\"')");
        assertThat( result).isEqualTo("taco");
    }
}