package org.dynjs.runtime;

import org.junit.Test;
import static org.fest.assertions.Assertions.*;

public class VariableDeclarationTest extends AbstractDynJSTestSupport {

    @Test
    public void testUnresolvedReference() {
        eval("var x;");
        Reference result = getContext().resolve("x");
        assertThat(result).isNotNull();
        assertThat(result.getReferencedName()).isEqualTo("x");
        assertThat(result.isPropertyReference()).isFalse();
        assertThat(result.isUnresolvableReference()).isFalse();
    }

    @Test
    public void testResolvableReference() {
        eval("var x = 42;");
        Reference result = getContext().resolve("x");
        assertThat(result).isNotNull();
        assertThat(result.getReferencedName()).isEqualTo("x");
        assertThat(result.isPropertyReference()).isFalse();
        assertThat(result.isUnresolvableReference()).isFalse();
        assertThat(result.getValue(getContext())).isEqualTo(42);
    }

    @Test
    public void testMultipleResolvableReferences() {
        eval("var x = 42, y = 84;");

        Reference x = getContext().resolve("x");
        assertThat(x).isNotNull();
        assertThat(x.getReferencedName()).isEqualTo("x");
        assertThat(x.isPropertyReference()).isFalse();
        assertThat(x.isUnresolvableReference()).isFalse();
        assertThat(x.getValue(getContext())).isEqualTo(42);

        Reference y = getContext().resolve("y");
        assertThat(y).isNotNull();
        assertThat(y.getReferencedName()).isEqualTo("y");
        assertThat(y.isPropertyReference()).isFalse();
        assertThat(y.isUnresolvableReference()).isFalse();
        assertThat(y.getValue(getContext())).isEqualTo(84);
    }

    @Test
    public void testMultipleDeclarationStatements() {
        //eval( "var x;" );
        eval("var x; var y;");
    }

}
