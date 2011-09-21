/**
 *  Copyright 2011 dynjs contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.dynjs.parser;

import me.qmx.internal.org.objectweb.asm.Opcodes;
import me.qmx.jitescript.CodeBlock;
import org.antlr.runtime.tree.CommonTree;
import org.dynjs.api.Scope;
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.parser.statement.BlockStatement;
import org.dynjs.parser.statement.PrintStatement;
import org.dynjs.runtime.DynAtom;
import org.dynjs.runtime.DynNumber;
import org.dynjs.runtime.DynThreadContext;
import org.dynjs.runtime.RT;
import org.dynjs.runtime.primitives.DynPrimitiveNumber;
import org.dynjs.runtime.primitives.DynPrimitiveUndefined;

import java.util.List;

import static me.qmx.jitescript.util.CodegenUtils.ci;
import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class Executor implements Opcodes {

    private DynJSCompiler compiler = new DynJSCompiler();

    public List<Statement> program(final List<Statement> blockContent) {
        return blockContent;
    }

    public Statement block(final List<Statement> blockContent) {
        return new BlockStatement(blockContent);
    }

    public Statement printStatement(final Statement expr) {
        return new Statement(){
            @Override
            public CodeBlock getCodeBlock() {
                return CodeBlock.newCodeBlock(expr.getCodeBlock())
                        .aprintln();
            }
        };
    }

    public Statement declareVar(final CommonTree id) {
        return declareVar(id, new Statement() {
            @Override
            public CodeBlock getCodeBlock() {
                return CodeBlock.newCodeBlock()
                        .getstatic(p(DynPrimitiveUndefined.class), "UNDEFINED", ci(DynPrimitiveUndefined.class));
            }
        });
    }

    public Statement declareVar(final CommonTree id, final Statement expr) {
        return new Statement() {
            @Override
            public CodeBlock getCodeBlock() {
                return CodeBlock.newCodeBlock(expr.getCodeBlock())
                        .astore(3)
                        .aload(2)
                        .ldc(id.getText())
                        .aload(3)
                        .invokeinterface(p(Scope.class), "define", sig(void.class, String.class, DynAtom.class));
            }
        };
    }

    public Statement defineAddOp(final Statement l, final Statement r) {
        return defineNumOp("add", l, r);
    }

    public Statement defineSubOp(final Statement l, final Statement r) {
        return defineNumOp("sub", l, r);
    }

    public Statement defineMulOp(final Statement l, final Statement r) {
        return defineNumOp("mul", l, r);
    }

    public Statement defineNumOp(final String op, final Statement l, final Statement r) {
        return new Statement() {
            @Override
            public CodeBlock getCodeBlock() {
                String instruction = "dynjs:bop:" + op;
                return CodeBlock.newCodeBlock()
                        .append(l.getCodeBlock())
                        .append(r.getCodeBlock())
                        .invokedynamic(instruction, sig(DynNumber.class, DynAtom.class, DynAtom.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS);
            }
        };
    }

    public Statement defineStringLiteral(final String literal) {
        return new Statement() {
            @Override
            public CodeBlock getCodeBlock() {
                return CodeBlock.newCodeBlock()
                        .aload(1)
                        .ldc(literal)
                        .invokevirtual(p(DynThreadContext.class), "defineStringLiteral", sig(DynAtom.class, String.class));
            }
        };
    }

    public Statement defineOctalLiteral(final String value) {
        return new Statement() {
            @Override
            public CodeBlock getCodeBlock() {
                return CodeBlock.newCodeBlock()
                        .aload(1)
                        .ldc(value)
                        .invokevirtual(p(DynThreadContext.class), "defineOctalLiteral", sig(DynPrimitiveNumber.class, String.class));
            }
        };
    }

    public Statement defineNumberLiteral(final String value) {
        return new Statement() {
            @Override
            public CodeBlock getCodeBlock() {
                return CodeBlock.newCodeBlock()
                        .aload(1)
                        .ldc(value)
                        .invokevirtual(p(DynThreadContext.class), "defineDecimalLiteral", sig(DynPrimitiveNumber.class, String.class));
            }
        };
    }

}
