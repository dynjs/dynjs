package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

public class BenBreaksStuffTest extends AbstractDynJSTestSupport {

    @Test
    public void testBenComplainsAboutStuff1() {
        
        StringBuffer foo = new StringBuffer();
        foo.append( "function foo(){\n" );
        
        for ( int i = 0 ; i < 500; ++i ) {
            foo.append( "var foo='bar';\n" );
        }
        
        foo.append( "};\n" );
        
        StringBuffer wrapper = new StringBuffer();
        
        wrapper.append( "((function(){");
        wrapper.append( foo );
        wrapper.append( "return true;})())");
        
        //System.err.println( wrapper );
        eval( wrapper.toString() );
    }
}
