package org.dynjs.runtime.linker;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynAtom;
import org.dynjs.runtime.DynNumber;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.DynThreadContext;
import org.dynjs.runtime.RT;
import org.dynjs.runtime.primitives.DynPrimitiveNumber;
import org.junit.Before;
import org.junit.Test;

import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class DynJSLinkerTest {

    private DynThreadContext context;
    private DynObject scope;
    private DynJSCompiler compiler = new DynJSCompiler();

    @Before
    public void setup() {
        context = new DynThreadContext();
        scope = new DynObject();
    }

    @Test
    public void testPrimitiveSum() {
        final String lhs = "1";
        final String rhs = "2";
        compiler.compile(new Statement() {

            @Override
            public CodeBlock getCodeBlock() {
                return CodeBlock.newCodeBlock()
                        .aload(1)
                        .ldc(lhs)
                        .invokevirtual(p(DynThreadContext.class), "defineDecimalLiteral", sig(DynPrimitiveNumber.class, String.class))
                        .astore(3)
                        .aload(1)
                        .ldc(rhs)
                        .invokevirtual(p(DynThreadContext.class), "defineDecimalLiteral", sig(DynPrimitiveNumber.class, String.class))
                        .astore(4)
                        .aload(3)
                        .aload(4)
                        .invokedynamic("dynjs:runtime:bop:add", sig(DynNumber.class, DynAtom.class, DynAtom.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS);
            }
        }).execute(context, scope);

    }

}
