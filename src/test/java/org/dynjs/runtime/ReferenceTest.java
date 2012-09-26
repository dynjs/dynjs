package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

public class ReferenceTest extends AbstractDynJSTestSupport {

    @Test
    public void testVarDeclsInsideObscureBlockLikeThings() {
        Object result = eval("var foo=false;",
                "if (foo) {",
                "  var bar='taco';",
                "}",
                "bar" );
        
        assertThat( result ).isEqualTo( Types.UNDEFINED );
    }
}
