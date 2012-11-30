package org.dynjs.compiler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import me.qmx.jitescript.JiteClass;

import org.dynjs.Config;
import org.dynjs.codegen.AbstractCodeGeneratingVisitor;
import org.dynjs.compiler.jite.ProgramJiteClass;
import org.dynjs.parser.Statement;
import org.dynjs.parser.ast.Program;
import org.dynjs.runtime.AbstractProgram;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSProgram;

public class ProgramCompiler extends AbstractCompiler<Class<AbstractProgram>> {

    public ProgramCompiler(Class<? extends AbstractCodeGeneratingVisitor> codeGenClass, DynJS runtime, Config config) {
        super(codeGenClass, runtime, config, "Program");
    }

    public JSProgram compile(final ExecutionContext context, final Program program, boolean forceStrict) {
        
        final boolean strict = program.isStrict() || forceStrict;
        
        final AbstractCodeGeneratingVisitor byteCodeGenerator = newCodeGeneratingVisitor(context);
        program.accept(context, byteCodeGenerator, strict);
        
        JiteClass jiteClass = new ProgramJiteClass(nextClassName(), byteCodeGenerator, strict, program.getPosition() );
        Class<AbstractProgram> cls = defineClass(jiteClass);
        
        try {
            Constructor<AbstractProgram> ctor = cls.getDeclaredConstructor(Statement.class);
            AbstractProgram compiledProgram = ctor.newInstance(program);
            return compiledProgram;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }
}
