package org.dynjs.compiler.bytecode.partial;

import static me.qmx.jitescript.util.CodegenUtils.*;

import java.util.List;

import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.JiteClass;

import org.dynjs.Config;
import org.dynjs.codegen.CodeGeneratingVisitor;
import org.dynjs.codegen.CodeGeneratingVisitorFactory;
import org.dynjs.compiler.CompilationContext;
import org.dynjs.parser.ast.BlockStatement;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.DynamicClassLoader;
import org.dynjs.runtime.ExecutionContext;
import me.qmx.jitescript.internal.org.objectweb.asm.Opcodes;

public class SingleClassCompiler extends AbstractPartialCompiler {

    private List<BlockStatement> chunks;

    public SingleClassCompiler(Config config, DynamicClassLoader classLoader, CodeGeneratingVisitorFactory factory, List<BlockStatement> chunks) {
        super(config, classLoader, factory);
        this.chunks = chunks;
    }

    public SingleClassCompiler(AbstractPartialCompiler parent, List<BlockStatement> chunks) {
        super(parent);
        this.chunks = chunks;
    }

    @Override
    public void define(JiteClass cls, CompilationContext context, boolean strict) {
        int i = 0;
        for (BlockStatement each : chunks) {
            CodeGeneratingVisitor visitor = createVisitor( context.getBlockManager() );
            each.accept(context, visitor, strict);
            cls.defineMethod("callChunk" + i, Opcodes.ACC_PROTECTED, sig(Completion.class, ExecutionContext.class), visitor.areturn());
            ++i;
        }

        cls.defineMethod("initializeCode", Opcodes.ACC_PRIVATE, sig(void.class), new CodeBlock().voidreturn());
        cls.defineMethod("call", Opcodes.ACC_PUBLIC, sig(Completion.class, ExecutionContext.class), new SingleClassCaller(cls.getClassName(), chunks.size()));
    }
}
