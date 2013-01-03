package org.dynjs.compiler;

import org.dynjs.runtime.AbstractDynJSTestSupport;

public class ProgramCompilerTest extends AbstractDynJSTestSupport {

    /*
    @Test
    public void testNonStrictCompilation() {
        JSProgram program = getRuntime().compile("var x = 42;");
        assertThat(program.isStrict()).isFalse();
    }

    @Test
    public void testStrictCompilation() {
        JSProgram program = getRuntime().compile("'use strict'; var x = 42;");
        assertThat(program.isStrict()).isTrue();
    }

    @Test
    public void testStrictCompilationOfFunctions() {
        JSProgram program = getRuntime().compile("var v = function() { 'use strict'; var x = 42; }");
    }
    */
}
