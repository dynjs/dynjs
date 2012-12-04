package org.dynjs.compiler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.JiteClass;

import org.dynjs.Config;
import org.dynjs.codegen.AbstractCodeGeneratingVisitor;
import org.dynjs.compiler.jite.FunctionJiteClass;
import org.dynjs.parser.Statement;
import org.dynjs.parser.ast.BlockStatement;
import org.dynjs.runtime.AbstractFunction;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.LexicalEnvironment;

public class FunctionCompiler extends AbstractCompiler<Class<AbstractFunction>> {

    public FunctionCompiler(Class<? extends AbstractCodeGeneratingVisitor> codeGenClass, DynJS runtime, Config config) {
        super(codeGenClass, runtime, config, "Function");
    }

    public JSFunction compile(final ExecutionContext context, final String[] formalParameters, final BlockStatement body, final boolean strict) {

        final List<CodeBlock> bodies = new ArrayList<>();
        final List<BlockStatement> chunks = body.getAsChunks(250);

        for (BlockStatement eachChunk : chunks) {
            AbstractCodeGeneratingVisitor byteCodeGenerator = newCodeGeneratingVisitor(context);
            eachChunk.accept(context, byteCodeGenerator, strict);
            bodies.add( byteCodeGenerator );
        }

        JiteClass jiteClass = new FunctionJiteClass(nextClassName(), bodies, strict, body.getPosition());

        Class<AbstractFunction> functionClass = defineClass(jiteClass);

        try {
            Constructor<AbstractFunction> ctor = functionClass.getDeclaredConstructor(Statement.class, LexicalEnvironment.class, String[].class);
            AbstractFunction function = ctor.newInstance(body, context.getLexicalEnvironment(), formalParameters);
            function.setDebugContext("<anonymous>");
            return function;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }
}
