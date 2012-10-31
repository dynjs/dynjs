package org.dynjs.compiler;

import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.dynjs.runtime.JSProgram;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CompilerBytecodeOutputTest extends AbstractDynJSTestSupport {

    protected File bytecodeOutputDir;

    public void setUp() {
        super.setUp();
        bytecodeOutputDir = new File("target/jsbytecode/" + getClass().getName());
        deleteRecursive(bytecodeOutputDir);
        config.setBytecodeOutputDir(bytecodeOutputDir);
    }

    @Test
    public void testBytecodeOutput() {
        String javaScript = "var v = function() { 'use strict'; return 42 * 2; }; return v();";
        JSProgram program = getRuntime().compile(javaScript);

        // TODO the function bytecode isn't generated yet....
        //assertFileExists(new File(bytecodeOutputDir, "org/dynjs/gen/Function0.class"));
        assertFileExists(new File(bytecodeOutputDir, "org/dynjs/gen/Program0.class"));

        Object completion = getRuntime().evaluate(javaScript);
        assertEquals("JavaScript evaluation", 84L, completion);

        assertFileExists(new File(bytecodeOutputDir, "org/dynjs/gen/Function0.class"));
    }

    public static  void assertFileExists(File file) {
        assertTrue("File should exist " + file, file.exists());
    }

    public static boolean deleteRecursive(File path) {
        if (!path.exists()) {
            return false;
        }
        boolean answer = true;
        if (path.isDirectory()) {
            File[] files = path.listFiles();
            if (files != null) {
                for (File f : files) {
                    answer = answer && deleteRecursive(f);
                }
            }
        }
        return answer && path.delete();
    }
}
