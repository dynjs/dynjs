package org.dynjs.compiler;

import me.qmx.internal.org.objectweb.asm.tree.LabelNode;
import me.qmx.jitescript.CodeBlock;
import org.dynjs.api.Function;
import org.dynjs.api.Scope;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynFunction;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.DynThreadContext;
import org.junit.Before;
import org.junit.Test;

import static me.qmx.jitescript.CodeBlock.newCodeBlock;
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
        String dynString = "hello dynjs";
        DynFunction dynFunction = new DynFunction() {

            @Override
            public CodeBlock getCodeBlock() {
                return CodeBlock.newCodeBlock()
                        .aload(getArgumentsOffset())
                        .pushInt(getArgumentOffset("a"))
                        .aaload()
                        .areturn();
            }

            @Override
            public String[] getArguments() {
                return new String[]{"a"};
            }

        };
        Function function = dynJSCompiler.compile(dynFunction);
        Object result = function.call(context, scope, new Object[]{dynString});
        assertThat(result)
                .isNotNull()
                .isInstanceOf(String.class);

    }

    @Test
    public void testScriptCompiler() {
        dynJSCompiler.compile(new Statement() {
            @Override
            public CodeBlock getCodeBlock() {
                LabelNode velse = new LabelNode();
                LabelNode vout = new LabelNode();
                return newCodeBlock()
                        .pushBoolean(true)
                        .pushBoolean(true)
                        .ifeq(velse)
                        .ldc("then reached")
                        .aprintln()
                        .go_to(vout)
                        .label(velse)
                        .ldc("else reached")
                        .label(vout)
                        .aconst_null();
            }
        }).execute(new DynThreadContext());
    }

}
