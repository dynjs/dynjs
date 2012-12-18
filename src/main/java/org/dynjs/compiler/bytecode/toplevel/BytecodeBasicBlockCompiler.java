package org.dynjs.compiler.bytecode.toplevel;

import static me.qmx.jitescript.util.CodegenUtils.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.JiteClass;

import org.dynjs.Config;
import org.dynjs.codegen.CodeGeneratingVisitor.Arities;
import org.dynjs.codegen.CodeGeneratingVisitorFactory;
import org.dynjs.compiler.BasicBlockCompiler;
import org.dynjs.compiler.bytecode.partial.CompilationPlanner;
import org.dynjs.compiler.bytecode.partial.PartialCompiler;
import org.dynjs.parser.Statement;
import org.dynjs.parser.ast.BlockStatement;
import org.dynjs.runtime.AbstractBasicBlock;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.DynamicClassLoader;
import org.dynjs.runtime.ExecutionContext;
import org.objectweb.asm.Opcodes;

public class BytecodeBasicBlockCompiler extends AbstractTopLevelCompiler implements BasicBlockCompiler {

    public BytecodeBasicBlockCompiler(Config config, CodeGeneratingVisitorFactory factory) {
        super(config, factory, "BasicBlock");
    }

    /* (non-Javadoc)
     * @see org.dynjs.compiler.bytecode.toplevel.BasicBlockCompiler#compile(org.dynjs.runtime.ExecutionContext, java.lang.String, org.dynjs.parser.Statement)
     */
    @Override
    public BasicBlock compile(ExecutionContext context, final String grist, final Statement body) {
        String className = nextClassName(grist);

        final JiteClass cls = new JiteClass(className,
                p(AbstractBasicBlock.class),
                new String[] { p(BasicBlock.class) });

        cls.defineMethod("<init>", Opcodes.ACC_PUBLIC, sig(void.class, Statement.class),
                new CodeBlock() {
                    {
                        aload(Arities.THIS);
                        // this
                        aload(1);
                        // this statements
                        invokespecial(p(AbstractBasicBlock.class), "<init>", sig(void.class, Statement.class));
                        // empty
                        aload(Arities.THIS);
                        // this
                        invokevirtual(cls.getClassName().replace('.', '/'), "initializeCode", sig(void.class));
                        // <empty>
                        voidreturn();
                    }
                });

        DynamicClassLoader cl = new DynamicClassLoader(getConfig().getClassLoader());

        CompilationPlanner planner = new CompilationPlanner(getConfig(), cl, getFactory());

        PartialCompiler compiler = null;
        if (body instanceof BlockStatement) {
            compiler = planner.plan((BlockStatement) body);
        } else {
            compiler = planner.plan(new BlockStatement(Collections.singletonList(body)));
        }

        compiler.define(cls, context, false);
        Class<BasicBlock> blockClass = defineClass(cl, cls);

        try {
            Constructor<BasicBlock> ctor = blockClass.getDeclaredConstructor(Statement.class);
            BasicBlock block = ctor.newInstance(body);
            return block;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }

    }
}
