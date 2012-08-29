package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.dynjs.runtime.builtins.types.regexp.DynRegExp;
import org.junit.Test;

public class RegexpLiteralTest extends AbstractDynJSTestSupport {

    @Test
    public void testLiteralNoFlags() {
        Object result = eval( "/foo/" );
        assertThat( result ).isInstanceOf(DynRegExp.class );
    }
    
    @Test
    public void testLiteralWithFlags() {
        Object result = eval( "/foo/mg" );
        assertThat( result ).isInstanceOf(DynRegExp.class );
    }
    
    @Test
    public void testExec() {
        eval( "var r = /foo./g", 
                "var s = 'footfool';");
        
        JSObject result = (JSObject) eval( "r.exec(s)" );
        
        assertThat( result ).isNotNull();
        assertThat( result.get( getContext(), "length" ) ).isEqualTo(1);
        assertThat( result.get( getContext(), "0" ) ).isEqualTo("foot");
        
        result = (JSObject) eval( "r.exec(s)" );
        assertThat( result.get( getContext(), "length" ) ).isEqualTo(1);
        assertThat( result.get( getContext(), "0" ) ).isEqualTo("fool");
        
    }


}