package org.dynjs.compiler;

import static org.fest.assertions.Assertions.*;

import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.dynjs.runtime.JSProgram;
import org.junit.Test;

public class ProgramCompilerTest extends AbstractDynJSTestSupport {

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

}
