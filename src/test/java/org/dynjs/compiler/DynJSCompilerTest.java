package org.dynjs.compiler;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.api.Function;
import org.dynjs.api.Scope;
import org.dynjs.parser.Statement;
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
                        .aload(getArgumentsOffset())
                        .pushInt(getArgumentOffset("a"))
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
        DynFunction shout = new DynFunction(new String[]{"a"}) {

            @Override
            public CodeBlock getCodeBlock() {
                return newCodeBlock()
                        .aload(getArgumentsOffset())
                        .bipush(getArgumentOffset("a"))
                        .aaload()
                        .invokedynamic("print", sig(void.class, DynAtom.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS)
                        .aconst_null()
                        .areturn();
            }
        };
        Function function = dynJSCompiler.compile(shout);
        function.call(context, scope, new DynString("test"));
    }

    @Test
    public void testScriptCompiler() {
        dynJSCompiler.compile(new Statement() {
            @Override
            public CodeBlock getCodeBlock() {
                return newCodeBlock()
                        .aconst_null()
                        .areturn();
            }
        }).execute(null, null);
    }

}
