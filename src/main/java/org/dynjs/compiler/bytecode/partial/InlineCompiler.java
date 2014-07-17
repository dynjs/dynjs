package org.dynjs.compiler.bytecode.partial;

import static me.qmx.jitescript.util.CodegenUtils.*;
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

public class InlineCompiler extends AbstractPartialCompiler {
    
    private BlockStatement block;

    public InlineCompiler(Config config, DynamicClassLoader classLoader, CodeGeneratingVisitorFactory factory, BlockStatement block) {
        super( config, classLoader, factory );
        this.block = block;
    }
    
    @Override
    public void define(JiteClass cls, CompilationContext context, boolean strict) {
        CodeGeneratingVisitor visitor = createVisitor( context.getBlockManager() );
        block.accept(context, visitor, strict);
        cls.defineMethod("call", Opcodes.ACC_PUBLIC, sig(Completion.class, ExecutionContext.class), visitor.areturn());
        cls.defineMethod("initializeCode", Opcodes.ACC_PRIVATE, sig(void.class), new CodeBlock().voidreturn());
    }

}
