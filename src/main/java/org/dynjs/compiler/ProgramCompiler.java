package org.dynjs.compiler;

import static me.qmx.jitescript.util.CodegenUtils.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.JiteClass;

import org.dynjs.Config;
import org.dynjs.parser.Statement;
import org.dynjs.parser.ast.BlockStatement;
import org.dynjs.parser.ast.Expression;
import org.dynjs.parser.ast.ExpressionStatement;
import org.dynjs.parser.ast.StringLiteralExpression;
import org.dynjs.runtime.BaseProgram;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSProgram;

public class ProgramCompiler extends AbstractCompiler {

    public ProgramCompiler(Config config) {
        super(config, "Program");
    }

    public JSProgram compile(final Statement statement) {
        JiteClass jiteClass = new JiteClass(nextClassName(), p(BaseProgram.class), new String[0]) {
            {
                defineMethod("<init>", ACC_PUBLIC | ACC_VARARGS, sig(void.class, Statement.class),
                        new CodeBlock() {
                            {
                                aload(0);
                                aload(1);
                                if (isStrict(statement)) {
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
                        append(statement.getCodeBlock());
                        areturn();
                    }
                };
            }
        };

        Class<BaseProgram> cls = (Class<BaseProgram>) defineClass(jiteClass);
        try {
            Constructor<BaseProgram> ctor = cls.getDeclaredConstructor(Statement.class);
            return (JSProgram) ctor.newInstance(statement);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    public boolean isStrict(Statement statement) {
        boolean isStrict = false;

        if (statement instanceof BlockStatement) {
            BlockStatement block = (BlockStatement) statement;
            for (Statement each : block.getBlockContent()) {
                if (each instanceof ExpressionStatement) {
                    Expression expr = ((ExpressionStatement) each).getExpr();
                    if (expr instanceof StringLiteralExpression) {
                        if (((StringLiteralExpression) expr).getLiteral().equals("use strict")) {
                            isStrict = true;
                        }
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
        }

        return isStrict;
    }
}
