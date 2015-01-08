package org.dynjs.compiler.bytecode;

import static me.qmx.jitescript.util.CodegenUtils.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.JiteClass;

import org.dynjs.Config;
import org.dynjs.codegen.CodeGeneratingVisitor.Arities;
import org.dynjs.codegen.CodeGeneratingVisitorFactory;
import org.dynjs.compiler.BasicBlockCompiler;
import org.dynjs.compiler.CompilationContext;
import org.dynjs.compiler.bytecode.partial.CompilationPlanner;
import org.dynjs.compiler.bytecode.partial.PartialCompiler;
import org.dynjs.parser.Statement;
import org.dynjs.parser.ast.BlockStatement;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.BlockManager.Entry;
import org.dynjs.runtime.DynamicClassLoader;
import org.dynjs.runtime.ExecutionContext;
import me.qmx.jitescript.internal.org.objectweb.asm.Opcodes;

public class BytecodeBasicBlockCompiler extends AbstractBytecodeCompiler implements BasicBlockCompiler {
    
    private AtomicInteger counter = new AtomicInteger();

    public BytecodeBasicBlockCompiler(Config config, CodeGeneratingVisitorFactory factory) {
        super(config, factory);
    }

    @Override
    public BasicBlock compile(final CompilationContext context, final String grist, final Statement body, boolean strict) {
        
        int statementNumber = body.getStatementNumber();
        Entry entry = context.getBlockManager().retrieve(statementNumber);
        BasicBlock code = entry.getCompiled();
        if ( code instanceof BytecodeBasicBlock ) {
            return code;
        }
        
        String className = nextClassName(grist);

        final JiteClass cls = new JiteClass(className,
                p(BytecodeBasicBlock.class),
                new String[] {});

        cls.defineMethod("<init>", Opcodes.ACC_PUBLIC, sig(void.class, String.class, boolean.class, List.class, List.class ),
                new CodeBlock()
                    .aload(Arities.THIS)
                    // this
                    .aload(1)
                    // this filename
                    .iload(2)
                    // this filename strict
                    .aload(3)
                    // this filename strict vardecls
                    .aload(4)
                    // this filename strict vardecls fndecls
                    .invokespecial(p(BytecodeBasicBlock.class), "<init>",
                                  sig(void.class, String.class, boolean.class, List.class, List.class) )
                    // <empty>
                    .aload( Arities.THIS )
                    // this
                    .invokevirtual(cls.getClassName().replace('.', '/'), "initializeCode", sig(void.class))
                    // <empty>
                    .voidreturn()
                );

        CompilationPlanner planner = new CompilationPlanner(getConfig(), context.getClassLoader(), getFactory());

        PartialCompiler compiler = null;
        if (body instanceof BlockStatement) {
            compiler = planner.plan((BlockStatement) body);
        } else {
            compiler = planner.plan(new BlockStatement(Collections.singletonList(body)));
        }

        compiler.define(cls, context, false);
        Class<BytecodeBasicBlock> blockClass = defineClass(context.getClassLoader(), cls);

        Position position = body.getPosition();

        String fileName = (position != null ? position.getFileName() : "eval");
        
        try {
            Constructor<BytecodeBasicBlock> ctor = blockClass.getDeclaredConstructor(String.class, boolean.class, List.class, List.class);
            BasicBlock block = ctor.newInstance(fileName, strict, body.getVariableDeclarations(), body.getFunctionDeclarations());
            entry.setCompiled(block);
            return block;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }

    }
    
    public String nextClassName(String grist) {
        return getConfig().getBasePackage().replace('.', '/') + "/" + grist + nextCounterValue();
    }

    private int nextCounterValue() {
        return this.counter.getAndIncrement();
    }
}
