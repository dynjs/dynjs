package org.dynjs.compiler;

import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.JiteClass;

import org.dynjs.Config;
import org.dynjs.codegen.AbstractCodeGeneratingVisitor;
import org.dynjs.parser.Position;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.AbstractBasicBlock;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.ExecutionContext;

public class BasicBlockCompiler extends AbstractCompiler {

    private final String INVOKE = sig(Completion.class, ExecutionContext.class);

    public BasicBlockCompiler(Class<? extends AbstractCodeGeneratingVisitor> codeGenClass, DynJS runtime, Config config) {
        super(codeGenClass, runtime, config, "Block");
    }

    public BasicBlock compile(ExecutionContext context, final String grist, final Statement body) {

        final AbstractCodeGeneratingVisitor byteCodeGenerator = newCodeGeneratingVisitor(context);
        body.accept(context, byteCodeGenerator, false);

        JiteClass jiteClass = new JiteClass(nextClassName(grist), p(AbstractBasicBlock.class), new String[0]) {
            {
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
                defineMethod("call", ACC_PUBLIC, INVOKE,
                        new CodeBlock() {
                            {
                                append(byteCodeGenerator);
                                areturn();
                            }
                        });

            }
        };
        
        String sourceFile = "<eval>";
        Position position = body.getPosition();
        if ( position != null ) {
            if ( position.getFileName() != null ) {
                sourceFile = position.getFileName();
            }
        }
        
        jiteClass.setSourceFile(sourceFile);

        Class<AbstractBasicBlock> blockClass = (Class<AbstractBasicBlock>) defineClass(jiteClass);
        try {
            Constructor<AbstractBasicBlock> ctor = blockClass.getDeclaredConstructor(Statement.class);
            AbstractBasicBlock block = ctor.newInstance(body);
            return block;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }
}
