package org.dynjs.runtime;

import static org.fest.assertions.Assertions.assertThat;

import org.dynjs.exception.ThrowException;
import org.junit.Test;

public class StringLiteralTest extends AbstractDynJSTestSupport {

    @Test
    public void testBigConcat() {
        Object result = eval( "'A........................'+",
               "'B...........'+",
               "'C...'+",
               "'D..................'+",
               "'E...................'+",
               "'F...................'+",
               "'G...........'+",
               "'H...'+",
               "'I...................................'+",
               "'J...................................'+",
               "'K...................................'+",
               "'L...................................'+",
               "'M...................................'+",
               "'N...................................'+",
               "'O...................................'+",
               "'P...................................'+",
                "''" );
    }
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

    @Test
    public void testEscapeSequences() {
        assertThat(eval("'foo\\nbar\\rbaz\\t'")).isEqualTo("foo\nbar\rbaz\t");
        assertThat(eval("'\\x59\\x5A'")).isEqualTo("YZ");
        assertThat(eval("'\\u0062\\u006f\\u0062'")).isEqualTo("bob");
    }
    
    @Test
    public void testEmbeddedNewlines() {
        String result = (String) eval( "'howdy\\\n  bob'");
        assertThat( result ).isEqualTo( "howdy  bob");
    }
    
    @Test
    public void testEmbeddedNewlinesDoubleQuotes() {
        String result = (String) eval( "\"howdy\\\n  bob\"");
        assertThat( result ).isEqualTo( "howdy  bob");
    }
    
    @Test
    public void testWeirdSpec() {
        String script = "'use strict';\neval(\"function foo(){\\\r}\");";
        
        eval( script );
    }
    
    @Test
    public void testLineContinuations() {
        Object result = eval( "'foo\\\r\nbar'");
        
        assertThat( result ).isEqualTo( "foobar" );
    }
}
