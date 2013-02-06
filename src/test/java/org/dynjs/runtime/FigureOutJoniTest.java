package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.jcodings.specific.UTF8Encoding;
import org.joni.Matcher;
import org.joni.Option;
import org.joni.Regex;
import org.joni.Region;
import org.joni.Syntax;
import org.junit.Test;

public class FigureOutJoniTest extends AbstractDynJSTestSupport {

    @Test
    public void testBasics() {
        String regexStr = "foo";
        byte[] bytes = regexStr.getBytes();
        Regex regex = new Regex(bytes, 0, bytes.length, Option.POSIX_REGION, UTF8Encoding.INSTANCE, Syntax.Perl );
        
        assertThat( regex ).isNotNull();
        
        String input = "afoot\nafool";
        Matcher matcher = regex.matcher( input.getBytes() );
        
        System.err.println( matcher.search(0, input.length(), 0));
        System.err.println( matcher.getBegin() );
        System.err.println( matcher.getEnd() );
        System.err.println( input.substring( matcher.getBegin(), matcher.getEnd() ));
        
        Region region = matcher.getEagerRegion();
        
        System.err.println( region );
        
        System.err.println( input.substring( region.beg[0], region.end[0] ));
        
    }
    
}