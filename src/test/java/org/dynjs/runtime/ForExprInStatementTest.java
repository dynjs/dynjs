package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

public class ForExprInStatementTest extends AbstractDynJSTestSupport {

    @Test
    public void testBasicLoop() {
        JSObject collector = (JSObject) eval("var x = { a: 1, b:2 }",
                " var collector = []",
                "var e;",
                "for ( e in x ) {",
                "  collector.push( e );",
                "}", 
                "collector");
        
        assertThat( collector.get( getContext(), "length" )).isEqualTo(2L);
    }
    
    @Test
    public void testInStatement() {
        JSObject result = (JSObject) eval( "for ( x in Math ) { } ");
        System.err.println( result );
        
    }
    
}
