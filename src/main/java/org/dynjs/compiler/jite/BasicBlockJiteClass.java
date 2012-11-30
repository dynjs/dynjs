package org.dynjs.compiler.jite;

import static me.qmx.jitescript.util.CodegenUtils.*;
import me.qmx.jitescript.CodeBlock;

import org.dynjs.parser.Position;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.AbstractBasicBlock;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.ExecutionContext;

public class BasicBlockJiteClass extends AbstractDynJSJiteClass {

    public BasicBlockJiteClass(final String className, final CodeBlock body, Position position) {
        super(className, AbstractBasicBlock.class, position );
        
        defineMethod("<init>", ACC_PUBLIC, sig(void.class, Statement.class),
                new CodeBlock() {
                    {
                        aload(0);
                        // this
                        aload(1);
                        // this statements
                        invokespecial(p(AbstractBasicBlock.class), "<init>", sig(void.class, Statement.class));
                        voidreturn();
                    }
                });
        defineMethod("call", ACC_PUBLIC, sig( Completion.class, ExecutionContext.class), body.areturn() );
    }

}
