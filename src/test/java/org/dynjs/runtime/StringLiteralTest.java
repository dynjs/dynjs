package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

public class StringLiteralTest extends AbstractDynJSTestSupport {

    @Test
    public void testEvalStringLiteral() {
        Object result = eval("'howdy'");
        assertThat(result).isEqualTo("howdy");
    }

    @Test
    public void testStringConcatentation() {
        Object result = eval("'howdy' + ' ' + 'dude'");
        assertThat(result).isEqualTo("howdy dude");
    }

    @Test
    public void testStringLiteralInitializer() {
        eval("var x = 'howdy';");
        Reference x = getContext().resolve("x");
        assertThat(x).isNotNull();
        assertThat(x.getValue(getContext())).isEqualTo("howdy");
    }
}
