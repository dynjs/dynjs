package org.dynjs.compiler.toplevel;

import static me.qmx.jitescript.util.CodegenUtils.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.JiteClass;

import org.dynjs.Config;
import org.dynjs.codegen.CodeGeneratingVisitorFactory;
import org.dynjs.codegen.CodeGeneratingVisitor.Arities;
import org.dynjs.compiler.partial.CompilationPlanner;
import org.dynjs.compiler.partial.PartialCompiler;
import org.dynjs.parser.Statement;
import org.dynjs.parser.ast.BlockStatement;
import org.dynjs.runtime.AbstractJavascriptFunction;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.DynamicClassLoader;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.LexicalEnvironment;
import org.objectweb.asm.Opcodes;

public class FunctionCompiler extends AbstractTopLevelCompiler {

    public FunctionCompiler(Config config, CodeGeneratingVisitorFactory factory) {
        super(config, factory, "Function");
    }

    public JSFunction compile(final ExecutionContext context, final String[] formalParameters, final BlockStatement body, final boolean strict) {
        String className = nextClassName();

        final JiteClass cls = new JiteClass(className,
                p(AbstractJavascriptFunction.class),
                new String[] { p(JSFunction.class), p(BasicBlock.class) });

        cls.defineMethod("<init>", Opcodes.ACC_PUBLIC, sig(void.class, Statement.class, LexicalEnvironment.class, String[].class),
                new CodeBlock() {
                    {
                        aload(Arities.THIS);
                        // this
                        aload(1);
                        // this statements
                        aload(2);
                        // this statements scope
                        pushBoolean(strict);
                        // this statements scope strict
                        aload(3);
                        // this statements scope strict formal-parameters
                        invokespecial(p(AbstractJavascriptFunction.class), "<init>", sig(void.class, Statement.class, LexicalEnvironment.class, boolean.class, String[].class));
                        // empty
                        aload( Arities.THIS );
                        // this
                        invokevirtual(cls.getClassName().replace('.', '/'), "initializeCode", sig(void.class));
                        // <empty>
                        voidreturn();
                    }
                });
        
        cls.defineMethod("call", Opcodes.ACC_PUBLIC, sig( Object.class, ExecutionContext.class ), new FunctionCaller() );

        DynamicClassLoader cl = new DynamicClassLoader(getConfig().getClassLoader());

        CompilationPlanner planner = new CompilationPlanner(getConfig(), cl, getFactory());

        PartialCompiler compiler = planner.plan(body);

        compiler.define(cls, context, strict);
        Class<JSFunction> functionClass = defineClass(cl, cls);

        try {
            Constructor<JSFunction> ctor = functionClass.getDeclaredConstructor(Statement.class, LexicalEnvironment.class, String[].class);
            JSFunction function = ctor.newInstance(body, context.getLexicalEnvironment(), formalParameters);
            function.setDebugContext("<anonymous>");
            return function;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

}
