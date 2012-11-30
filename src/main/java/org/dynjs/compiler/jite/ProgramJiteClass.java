package org.dynjs.compiler.jite;

import static me.qmx.jitescript.util.CodegenUtils.*;
import me.qmx.jitescript.CodeBlock;

import org.dynjs.parser.Position;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.AbstractProgram;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.ExecutionContext;

public class ProgramJiteClass extends AbstractDynJSJiteClass {

    public ProgramJiteClass(final String className, CodeBlock body, final boolean strict, Position position) {
        super(className, AbstractProgram.class, position);

        defineMethod("<init>", ACC_PUBLIC | ACC_VARARGS, sig(void.class, Statement.class),
                new CodeBlock() {
                    {
                        aload(0);
                        aload(1);
                        if (strict) {
                            iconst_1();
                            i2b();
                        } else {
                            iconst_0();
                            i2b();
                        }
                        invokespecial(p(AbstractProgram.class), "<init>", sig(void.class, Statement.class, boolean.class));
                        voidreturn();
                    }
                });
        defineMethod("execute", ACC_PUBLIC | ACC_VARARGS, sig(Completion.class, ExecutionContext.class), body.areturn());

    }

}
