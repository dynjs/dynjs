package org.dynjs.runtime.builtins.types;

import static org.fest.assertions.Assertions.*;

import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.dynjs.runtime.DynArray;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;
import org.junit.Test;

public class JSONTest extends AbstractDynJSTestSupport {
    
    @Test
    public void testParseArray() {
        DynArray result = (DynArray) eval( "JSON.parse('[42,86]')" );
        assertThat( result.get( getContext(), "length" )).isEqualTo(2);
        assertThat( result.get( getContext(), "0" )).isEqualTo(42);
        assertThat( result.get( getContext(), "1" )).isEqualTo(86);
    }
    
    @Test
    public void testParseNestedArray() {
        DynArray result = (DynArray) eval( "JSON.parse('[42,86,[\"a\", \"b\", \"c\" ]]')" );
        assertThat( result.get( getContext(), "length" )).isEqualTo(3);
        assertThat( result.get( getContext(), "0" )).isEqualTo(42);
        assertThat( result.get( getContext(), "1" )).isEqualTo(86);
        DynArray nested = (DynArray) result.get( getContext(), "2" );
        assertThat( nested.get( getContext(), "length" )).isEqualTo(3);
        assertThat( nested.get( getContext(), "0" )).isEqualTo("a");
        assertThat( nested.get( getContext(), "1" )).isEqualTo("b");
        assertThat( nested.get( getContext(), "2" )).isEqualTo("c");
    }
    
    @Test
    public void testParseObject() {
        JSObject result = (JSObject) eval( "JSON.parse('{\"foo\": 42, \"bar\": \"cheese\"}')");
        
        assertThat( result.get( getContext(), "foo" )).isEqualTo(42);
        assertThat( result.get( getContext(), "bar" )).isEqualTo("cheese");
    }
    
    @Test
    public void testParseWithReviver() {
        JSObject result = (JSObject) eval( "JSON.parse( '[1,2,3,4,5,6,7]', function(name,val){",
                "if ( name != '' ) {",
                "  if ( val % 2 == 0 ) {",
                "    return undefined;",
                "  } else {",
                "    return val;",
                "  }",
                "} else {",
                "  return val;",
                "}",
                "})");
        
        assertThat( result.get( getContext(), "length" ) ).isEqualTo(7);
        assertThat( result.get( getContext(), "0" ) ).isEqualTo(1);
        assertThat( result.get( getContext(), "1" ) ).isEqualTo(Types.UNDEFINED);
        assertThat( result.get( getContext(), "2" ) ).isEqualTo(3);
        assertThat( result.get( getContext(), "3" ) ).isEqualTo(Types.UNDEFINED);
        assertThat( result.get( getContext(), "4" ) ).isEqualTo(5);
        assertThat( result.get( getContext(), "5" ) ).isEqualTo(Types.UNDEFINED);
        assertThat( result.get( getContext(), "6" ) ).isEqualTo(7);
    }

}
