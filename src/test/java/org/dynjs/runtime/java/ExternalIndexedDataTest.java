package org.dynjs.runtime.java;

import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.dynjs.runtime.ExternalIndexedData;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;


public class ExternalIndexedDataTest extends AbstractDynJSTestSupport {
    
    
    @Test
    public void testAccessExternalData() throws Exception {

        final String[] array = new String[] {
                "cheese",
                "tacos",
                "bees",
        };

        JSObject obj = (JSObject) eval( "var o = {}; o;" );
        obj.setExternalIndexedData( new ExternalIndexedData() {
            @Override
            public Object get(long index) {
                if ( index > array.length-1 ) {
                    return null;
                }
                return array[((int) index)];
            }

            @Override
            public void put(long index, Object value) {
                if ( index > array.length-1 ) {
                    return;
                }
                array[((int) index)] = (String) value;
            }
        });

        assertThat( eval( "o[0]" ) ).isEqualTo( "cheese" );
        assertThat( eval( "o[1]" ) ).isEqualTo( "tacos" );
        assertThat( eval( "o[2]" ) ).isEqualTo( "bees" );
        assertThat( eval( "o[3]" ) ).isEqualTo( Types.UNDEFINED );

        array[0] = "bob";

        assertThat( eval( "o[0]" ) ).isEqualTo( "bob" );
    }

    @Test
    public void testUpdateExternalData() throws Exception {

        final String[] array = new String[] {
                "cheese",
                "tacos",
                "bees",
        };

        JSObject obj = (JSObject) eval( "var o = {}; o;" );
        obj.setExternalIndexedData( new ExternalIndexedData() {
            @Override
            public Object get(long index) {
                if ( index > array.length-1 ) {
                    return null;
                }
                return array[((int) index)];
            }

            @Override
            public void put(long index, Object value) {
                if ( index > array.length-1 ) {
                    return;
                }
                array[((int) index)] = (String) value;
            }
        });

        assertThat( eval( "o[0]" ) ).isEqualTo( "cheese" );
        assertThat( eval( "o[1]" ) ).isEqualTo( "tacos" );
        assertThat( eval( "o[2]" ) ).isEqualTo( "bees" );

        eval( "o[0] = 'bob';" );

        assertThat( array[0] ).isEqualTo( "bob" );

        assertThat( eval( "o[0]" ) ).isEqualTo( "bob" );
        assertThat( eval( "o[1]" ) ).isEqualTo( "tacos" );
        assertThat( eval( "o[2]" ) ).isEqualTo( "bees" );

        eval( "o.tacos = 42;" );

        assertThat( eval( "o.tacos" ) ).isEqualTo( 42L );

    }

    

}
