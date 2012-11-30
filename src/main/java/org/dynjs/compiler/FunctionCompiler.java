package org.dynjs.compiler;

import static me.qmx.jitescript.util.CodegenUtils.ci;
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
import org.dynjs.runtime.AbstractFunction;
import org.dynjs.runtime.AbstractJavascriptFunction;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.LexicalEnvironment;
import org.dynjs.runtime.Types;
import org.objectweb.asm.tree.LabelNode;

public class FunctionCompiler extends AbstractCompiler {

    private final String CALL = sig(Object.class, ExecutionContext.class);

    public FunctionCompiler(Class<? extends AbstractCodeGeneratingVisitor> codeGenClass, DynJS runtime, Config config) {
        super(codeGenClass, runtime, config, "Function");
    }

    public JSFunction compile(final ExecutionContext context, final String[] formalParameters, final Statement body, final boolean strict) {
        
        final AbstractCodeGeneratingVisitor byteCodeGenerator = newCodeGeneratingVisitor(context);
        body.accept(context, byteCodeGenerator, strict);

        JiteClass jiteClass = new JiteClass(nextClassName(), p(AbstractJavascriptFunction.class), new String[0]) {
            {
                defineMethod("<init>", ACC_PUBLIC, sig(void.class, Statement.class, LexicalEnvironment.class, String[].class),
                        new CodeBlock() {
                            {
                                aload(0);
                                // this
                                aload(1);
                                // this statements
                                aload(2);
                                // this statements scope
                                pushBoolean(strict);
                                // this statements scope strict
                                aload(3);
                                // this statements scope strict formal-parameters
                                invokespecial(p(AbstractJavascriptFunction.class), "<init>",
                                        sig(void.class, Statement.class, LexicalEnvironment.class, boolean.class, String[].class));
                                voidreturn();
                            }
                        });
                defineMethod("call", ACC_PUBLIC, CALL, new CodeBlock() {
                    {
                        append(byteCodeGenerator);
                        // completion
                        dup();
                        // completion completion
                        getfield(p(Completion.class), "type", ci(Completion.Type.class));
                        // completion type
                        invokevirtual(p(Completion.Type.class), "ordinal", sig(int.class));
                        // completion type
                        ldc(Completion.Type.RETURN.ordinal());
                        // completion type RETURN

                        LabelNode returnValue = new LabelNode();
                        if_icmpeq(returnValue);
                        // completion
                        pop();
                        getstatic(p(Types.class), "UNDEFINED", ci(Types.Undefined.class));
                        // UNDEF
                        areturn();

                        label(returnValue);
                        // completion
                        getfield(p(Completion.class), "value", ci(Object.class));
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

        Class<AbstractFunction> functionClass = (Class<AbstractFunction>) defineClass(jiteClass);
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
