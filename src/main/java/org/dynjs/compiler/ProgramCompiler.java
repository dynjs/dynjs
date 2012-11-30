package org.dynjs.compiler;

import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.JiteClass;

import org.dynjs.Config;
import org.dynjs.codegen.AbstractCodeGeneratingVisitor;
import org.dynjs.codegen.BasicBytecodeGeneratingVisitor;
import org.dynjs.parser.Position;
import org.dynjs.parser.Statement;
import org.dynjs.parser.ast.Program;
import org.dynjs.runtime.BaseProgram;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSProgram;

public class ProgramCompiler extends AbstractCompiler {

    public ProgramCompiler(Class<? extends AbstractCodeGeneratingVisitor> codeGenClass, DynJS runtime, Config config) {
        super(codeGenClass, runtime, config, "Program");
    }

    public JSProgram compile(final ExecutionContext context, final Program program, boolean forceStrict) {
        
        final boolean strict = program.isStrict() || forceStrict;
        
        final AbstractCodeGeneratingVisitor byteCodeGenerator = newCodeGeneratingVisitor(context);
        program.accept(context, byteCodeGenerator, strict);
        
        JiteClass jiteClass = new JiteClass(nextClassName(), p(BaseProgram.class), new String[0]) {
            {
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
                                invokespecial(p(BaseProgram.class), "<init>", sig(void.class, Statement.class, boolean.class));
                                voidreturn();
                            }
                        });

                defineMethod("execute", ACC_PUBLIC | ACC_VARARGS, sig(Completion.class, ExecutionContext.class), getCodeBlock());
            }

            private CodeBlock getCodeBlock() {
                return new CodeBlock() {
                    {
                        append( byteCodeGenerator );
                        areturn();
                    }
                };
            }
        };
        
        String sourceFile = "<eval>";
        Position position = program.getPosition();
        if ( position != null ) {
            if ( position.getFileName() != null ) {
                sourceFile = position.getFileName();
            }
        }
        
        jiteClass.setSourceFile(sourceFile);
        
        Class<BaseProgram> cls = (Class<BaseProgram>) defineClass(jiteClass);
        try {
            Constructor<BaseProgram> ctor = cls.getDeclaredConstructor(Statement.class);
            BaseProgram compiledProgram = ctor.newInstance(program);
            return compiledProgram;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }
}
