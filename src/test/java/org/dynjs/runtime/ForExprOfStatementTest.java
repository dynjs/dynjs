package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

public class ForExprOfStatementTest extends AbstractDynJSTestSupport {

    @Test
    public void testBasicLoop() {
        JSObject collector = (JSObject) eval("var x = { a: 1, b:2 }",
                " var collector = []",
                "var e;",
                "for ( e of x ) {",
                "  collector.push( e );",
                "}", 
                "collector");
        
        assertThat( collector.get( getContext(), "length" )).isEqualTo(2L);
    }
    
    @Test
    public void testOfStatement() {
        JSObject result = (JSObject) eval( "for ( x of Math ) { } ");
        System.err.println( result );
        
    }
    
}
