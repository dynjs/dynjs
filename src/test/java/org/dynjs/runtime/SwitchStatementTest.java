package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

public class SwitchStatementTest extends AbstractDynJSTestSupport {

    @Test
    public void testOnlyDefault() {
        Object result = eval( "var y;",
                "var x = 'taco';",
                "switch(x) {",
                "  default:",
                "    y = x;",
                "}",
                "y" );
        
        assertThat( result ).isEqualTo( "taco" );
    }
    
    @Test
    public void testOneNonDefaultCase() {
        Object result = eval( "var y;",
                "var x = 42;",
                "switch(x) {",
                "  case 42:",
                "    y='forty-two';",
                "}",
                "y" );
        
        assertThat( result ).isEqualTo( "forty-two" );
    }
    
    @Test
    public void testIndividualCases() {
        Object result = eval( "var y;",
                "var x = 42;",
                "switch(x) {",
                "  case 1:",
                "    y='one';",
                "    break;",
                "  case 2:",
                "    y='two';",
                "    break;",
                "  case 42:",
                "    y='forty-two';",
                "    break;",
                "  case 99:",
                "    y='ninety-nine';",
                "    break;",
                "}",
                "y" );
        
        assertThat( result ).isEqualTo( "forty-two" );
    }
    
    @Test
    public void testIndividualCasesWithFallThrough() {
        Object result = eval( "var y='';",
                "var x = 42;",
                "switch(x) {",
                "  case 1:",
                "    y+='one';",
                "  case 2:",
                "    y+='two';",
                "  case 42:",
                "    y+='forty-two';",
                "  case 99:",
                "    y+='ninety-nine';",
                "}",
                "y" );
        
        assertThat( result ).isEqualTo( "forty-twoninety-nine" );
    }
    
    @Test
    public void testIndividualCasesWithFallThroughButLackingBlocks() {
        Object result = eval( "var y='';",
                "var x = 42;",
                "switch(x) {",
                "  case 1:",
                "    y+='one';",
                "  case 2:",
                "    y+='two';",
                "  case 42:",
                "  case 99:",
                "    y+='ninety-nine';",
                "}",
                "y" );
        
        assertThat( result ).isEqualTo( "ninety-nine" );
    }
    
    @Test
    public void testIndividualCasesWithFallThroughButLackingBlocksWithDefault() {
        Object result = eval( "var y='';",
                "var x = 42;",
                "switch(x) {",
                "  case 1:",
                "    y+='one';",
                "  case 2:",
                "    y+='two';",
                "  case 42:",
                "  case 99:",
                "    y+='ninety-nine';",
                "  default:",
                "    y+='default';",
                "}",
                "y" );
        
        assertThat( result ).isEqualTo( "ninety-ninedefault" );
    }
    
    @Test
    public void testIndividualCasesWithFallThroughButLackingBlocksWithDefaultAndBReak() {
        Object result = eval( "var y='';",
                "var x = 1;",
                "switch(x) {",
                "  case 1:",
                "    y+='one';",
                "  case 2:",
                "    y+='two';",
                "    break;",
                "  case 42:",
                "  case 99:",
                "    y+='ninety-nine';",
                "  default:",
                "    y+='default';",
                "}",
                "y" );
        
        assertThat( result ).isEqualTo( "onetwo" );
    }
    
    @Test
    public void testIndividualCasesWithReturns() {
        Object result = eval( "var y;",
                "var x = 42;",
                "(function(x){",
                "  switch(x) {",
                "    case 1:",
                "      y='one';",
                "      break;",
                "    case 2:",
                "      y='two';",
                "      break;",
                "    case 42:",
                "      y='forty-two';",
                "      return y;",
                "    case 99:",
                "      y='ninety-nine';",
                "      break;",
                "  }",
                "})(x);" );
        
        assertThat( result ).isEqualTo( "forty-two" );
    }

    @Test
    public void testIndividualCasesWithContinue() {
        Object result = eval( "var y;",
                "var x;",
                "do{",
                "  switch(1) {",
                "    case 1:",
                "      x='one';",
                "      continue;",
                "    default:",
                "      x='default';",
                "      break;",
                "  }",
                "} while(false);",
                "x;");

        assertThat( result ).isEqualTo( "one" );
    }

}
