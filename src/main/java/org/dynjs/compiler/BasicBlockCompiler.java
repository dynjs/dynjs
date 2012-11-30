package org.dynjs.compiler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import me.qmx.jitescript.JiteClass;

import org.dynjs.Config;
import org.dynjs.codegen.AbstractCodeGeneratingVisitor;
import org.dynjs.compiler.jite.BasicBlockJiteClass;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.AbstractBasicBlock;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.ExecutionContext;

public class BasicBlockCompiler extends AbstractCompiler<Class<AbstractBasicBlock>> {

    public BasicBlockCompiler(Class<? extends AbstractCodeGeneratingVisitor> codeGenClass, DynJS runtime, Config config) {
        super(codeGenClass, runtime, config, "Block");
    }

    public BasicBlock compile(ExecutionContext context, final String grist, final Statement body) {

        final AbstractCodeGeneratingVisitor byteCodeGenerator = newCodeGeneratingVisitor(context);
        body.accept(context, byteCodeGenerator, false);

        JiteClass jiteClass = new BasicBlockJiteClass(nextClassName(grist), byteCodeGenerator, body.getPosition() );

        Class<AbstractBasicBlock> blockClass = defineClass(jiteClass);
        try {
            Constructor<AbstractBasicBlock> ctor = blockClass.getDeclaredConstructor(Statement.class);
            AbstractBasicBlock block = ctor.newInstance(body);
            return block;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }
}
