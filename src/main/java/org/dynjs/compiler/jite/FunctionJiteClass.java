package org.dynjs.compiler.jite;

import static me.qmx.jitescript.util.CodegenUtils.*;

import java.util.List;

import me.qmx.jitescript.CodeBlock;

import org.dynjs.compiler.BodyCaller;
import org.dynjs.parser.Position;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.AbstractJavascriptFunction;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.LexicalEnvironment;

public class FunctionJiteClass extends AbstractDynJSJiteClass {

    public FunctionJiteClass(final String className, final List<CodeBlock> bodies, final boolean strict, Position position) {
        super(className, AbstractJavascriptFunction.class, position);

        defineMethod("<init>", ACC_PUBLIC, sig(void.class, Statement.class, LexicalEnvironment.class, String[].class),
                new CodeBlock() {
                    {
                        aload(0);
                        // this
                        aload(1);
                        // this statements
                        aload(2);
                        // this statements scope
                        pushBoolean(strict);
                        // this statements scope strict
                        aload(3);
                        // this statements scope strict formal-parameters
                        invokespecial(p(AbstractJavascriptFunction.class), "<init>",
                                sig(void.class, Statement.class, LexicalEnvironment.class, boolean.class, String[].class));
                        voidreturn();
                    }
                });

        int chunkNum = 0;

        for (CodeBlock body : bodies) {
            definePartialCall(chunkNum, body);
            ++chunkNum;
        }

        defineMethod("call", ACC_PUBLIC, sig(Object.class, ExecutionContext.class), new BodyCaller(className, bodies.size()));
    }

    private void definePartialCall(final int chunkNum, final CodeBlock body) {
        defineMethod("call_" + chunkNum, ACC_PUBLIC, sig(Completion.class, ExecutionContext.class), new CodeBlock() {
            {
                append(body);
                // completion
                areturn();
            }
        });

    }

}
