package org.dynjs.compiler.toplevel;

import static me.qmx.jitescript.util.CodegenUtils.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.JiteClass;

import org.dynjs.Config;
import org.dynjs.codegen.CodeGeneratingVisitor.Arities;
import org.dynjs.codegen.CodeGeneratingVisitorFactory;
import org.dynjs.compiler.partial.CompilationPlanner;
import org.dynjs.compiler.partial.PartialCompiler;
import org.dynjs.parser.Statement;
import org.dynjs.parser.ast.Program;
import org.dynjs.runtime.AbstractProgram;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.DynamicClassLoader;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSProgram;
import org.objectweb.asm.Opcodes;

public class ProgramCompiler extends AbstractTopLevelCompiler {

    public ProgramCompiler(Config config, CodeGeneratingVisitorFactory factory) {
        super(config, factory, "Program");
    }

    public JSProgram compile(final ExecutionContext context, final Program body, boolean forceStrict) {
        String className = nextClassName();

        final boolean strict = body.isStrict() || forceStrict;

        final JiteClass cls = new JiteClass(className,
                p(AbstractProgram.class),
                new String[] { p(JSProgram.class), p(BasicBlock.class) });

        cls.defineMethod("<init>", Opcodes.ACC_PUBLIC, sig(void.class, Statement.class),
                new CodeBlock() {
                    {
                        aload(Arities.THIS);
                        // this
                        aload(1);
                        // this statements
                        if (strict) {
                            iconst_1();
                            i2b();
                        } else {
                            iconst_0();
                            i2b();
                        }
                        // this statements strict
                        invokespecial(p(AbstractProgram.class), "<init>", sig(void.class, Statement.class, boolean.class));
                        // <empty>
                        aload(Arities.THIS);
                        // this
                        invokevirtual(cls.getClassName().replace('.', '/'), "initializeCode", sig(void.class));
                        // <empty>
                        voidreturn();
                    }
                });

        cls.defineMethod("execute", Opcodes.ACC_PUBLIC, sig(Completion.class, ExecutionContext.class), new CodeBlock() {
            {
                aload( Arities.THIS );
                // this
                aload( Arities.EXECUTION_CONTEXT );
                // this context
                invokeinterface(p(BasicBlock.class), "call", sig(Completion.class, ExecutionContext.class));
                // completion
                areturn();
            }
        });

        DynamicClassLoader cl = new DynamicClassLoader(getConfig().getClassLoader());

        CompilationPlanner planner = new CompilationPlanner(getConfig(), cl, getFactory());

        PartialCompiler compiler = planner.plan(body);

        compiler.define(cls, context, strict);
        Class<JSProgram> programClass = defineClass(cl, cls);

        try {
            Constructor<JSProgram> ctor = programClass.getDeclaredConstructor(Statement.class);
            JSProgram compiledProgram = ctor.newInstance(body);
            return compiledProgram;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

}
