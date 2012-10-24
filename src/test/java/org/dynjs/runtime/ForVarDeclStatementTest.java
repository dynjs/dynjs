package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

public class ForVarDeclStatementTest extends AbstractDynJSTestSupport {

    @Test
    public void testBasicLoop() {
        eval("var y=0;",
                "for ( var i = 0 ; i < 10; ++i ) {",
                "  y = i;",
                "}");

        Object i = getContext().resolve("i").getValue(getContext());
        assertThat(i).isEqualTo(10L);

        Object y = getContext().resolve("y").getValue(getContext());
        assertThat(y).isEqualTo(9L);
    }
    
    @Test
    public void testBasicLoopMultipleDecls() {
        eval("var y=0; var z=0;",
                "for ( var i = 0, j = 3 ; i < 10; ++i, ++j ) {",
                "  y = i;",
                "  z = j;",
                "}");

        Object i = getContext().resolve("i").getValue(getContext());
        assertThat(i).isEqualTo(10L);

        Object y = getContext().resolve("y").getValue(getContext());
        assertThat(y).isEqualTo(9L);
        
        Object j = getContext().resolve("j").getValue(getContext());
        assertThat(j).isEqualTo(13L);

        Object z = getContext().resolve("z").getValue(getContext());
        assertThat(z).isEqualTo(12L);
    }

    @Test
    public void testLoopWithNaNTest() {
        Object result = eval("var y=0;",
                "for ( var i = 0 ; NaN; ) {",
                "  y = i;",
                "}",
                "y");

        assertThat(result).isEqualTo(0L);
    }
}
