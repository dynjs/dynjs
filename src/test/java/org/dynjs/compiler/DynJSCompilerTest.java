package org.dynjs.compiler;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.api.Function;
import org.dynjs.api.Scope;
import org.dynjs.runtime.Argument;
import org.dynjs.runtime.DynAtom;
import org.dynjs.runtime.DynFunction;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.DynString;
import org.dynjs.runtime.DynThreadContext;
import org.dynjs.runtime.RT;
import org.junit.Before;
import org.junit.Test;

import static me.qmx.jitescript.CodeBlock.newCodeBlock;
import static me.qmx.jitescript.util.CodegenUtils.sig;
import static org.fest.assertions.Assertions.assertThat;

public class DynJSCompilerTest {

    private DynJSCompiler dynJSCompiler;
    private DynThreadContext context;
    private Scope scope;

    @Before
    public void setUp() throws Exception {
        dynJSCompiler = new DynJSCompiler();
        context = new DynThreadContext();
        scope = new DynObject();
    }

    @Test
    public void testCompile() throws Exception {
        DynString dynString = new DynString("hello dynjs");
        DynFunction dynFunction = new DynFunction("a") {

            @Override
            public CodeBlock getCodeBlock() {
                return CodeBlock.newCodeBlock()
                        .aload(getArgumentOffset("a"))
                        .iconst_0()
                        .aaload()
                        .areturn();
            }

        };
        Function function = dynJSCompiler.compile(dynFunction);
        DynAtom result = function.call(context, scope, dynString);
        assertThat(result)
                .isNotNull()
                .isInstanceOf(DynString.class);

    }

    @Test
    public void testInvokeDynamicCompilation() {
        DynString dynString = new DynString("hello dynjs");
        DynFunction shout = new DynFunction("a") {

            @Override
            public CodeBlock getCodeBlock() {
                int a = getArgumentOffset("a");
                System.out.println(a);
                return newCodeBlock()
                        .aload(a)
                        .iconst_0()
                        .aaload()
                        .visitInvokeDynamicInsn("print", sig(void.class, DynAtom.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS)
                        .aload(a)
                        .iconst_0()
                        .aaload()
                        .areturn();
            }
        };
        Function function = dynJSCompiler.compile(shout);
        function.call(context, scope, new DynString("test"));
    }
}
