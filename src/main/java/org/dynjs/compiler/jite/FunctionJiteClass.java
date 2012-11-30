package org.dynjs.compiler.jite;

import static me.qmx.jitescript.util.CodegenUtils.*;
import me.qmx.jitescript.CodeBlock;

import org.dynjs.parser.Position;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.AbstractJavascriptFunction;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.LexicalEnvironment;
import org.dynjs.runtime.Types;
import org.objectweb.asm.tree.LabelNode;

public class FunctionJiteClass extends AbstractDynJSJiteClass {

    public FunctionJiteClass(final String className, final CodeBlock body, final boolean strict, Position position) {
        super(className, AbstractJavascriptFunction.class, position );
        
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
        
        defineMethod("call", ACC_PUBLIC, sig(Object.class, ExecutionContext.class), new CodeBlock() {
            {
                append(body);
                // completion
                dup();
                // completion completion
                getfield(p(Completion.class), "type", ci(Completion.Type.class));
                // completion type
                invokevirtual(p(Completion.Type.class), "ordinal", sig(int.class));
                // completion type
                ldc(Completion.Type.RETURN.ordinal());
                // completion type RETURN

                LabelNode returnValue = new LabelNode();
                if_icmpeq(returnValue);
                // completion
                pop();
                getstatic(p(Types.class), "UNDEFINED", ci(Types.Undefined.class));
                // UNDEF
                areturn();

                label(returnValue);
                // completion
                getfield(p(Completion.class), "value", ci(Object.class));
                areturn();
            }
        });
    }

}
