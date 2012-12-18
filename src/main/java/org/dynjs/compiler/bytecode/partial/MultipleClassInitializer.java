package org.dynjs.compiler.bytecode.partial;

import static me.qmx.jitescript.util.CodegenUtils.*;
import me.qmx.jitescript.CodeBlock;

import org.dynjs.codegen.CodeGeneratingVisitor.Arities;
import org.dynjs.runtime.BasicBlock;

public class MultipleClassInitializer extends CodeBlock {

    public MultipleClassInitializer(final String className, final int numChunks) {
        for (int i = 0; i < numChunks; ++i) {
            String chunkClassName = className + '$' + i;
            aload(Arities.THIS);
            // this
            newobj(chunkClassName.replace('.', '/'));
            // this chunk
            dup();
            // this chunk chunk
            invokespecial(chunkClassName.replace('.', '/'), "<init>", sig(void.class));
            // this chunk
            putfield(className.replace('.', '/'), "chunk" + i, ci(BasicBlock.class));
            // <empty>
            voidreturn();
        }
    }

}
