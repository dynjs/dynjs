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
import org.dynjs.runtime.DynAtom;
import org.dynjs.runtime.DynNumber;
import org.dynjs.runtime.DynThreadContext;
import org.dynjs.runtime.RT;
import org.dynjs.runtime.primitives.DynPrimitiveNumber;
import org.dynjs.runtime.primitives.DynPrimitiveUndefined;

import java.util.List;

import static me.qmx.jitescript.util.CodegenUtils.*;

public class Executor implements Opcodes {

    private DynJSCompiler compiler = new DynJSCompiler();

    public List<Statement> program(final List<Statement> blockContent) {
        return blockContent;
    }

    public Statement block(final List<Statement> blockContent) {
        return new BlockStatement(blockContent);
    }

    public Statement printStatement(final Statement expr) {
        return new Statement() {
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
        return declareVar(id.getText(), expr);
    }

    public Statement declareVar(final String id, final Statement expr) {
        return new Statement() {
            @Override
            public CodeBlock getCodeBlock() {
                return CodeBlock.newCodeBlock(expr.getCodeBlock())
                        .astore(3)
                        .aload(2)
                        .ldc(id)
                        .aload(3)
                        .invokedynamic("dynjs:scope:define", sig(void.class, Scope.class, String.class, DynAtom.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS);
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

    public Statement resolveIdentifier(final CommonTree id) {
        return new Statement() {
            @Override
            public CodeBlock getCodeBlock() {
                return CodeBlock.newCodeBlock()
                        .aload(2)
                        .ldc(id.getText())
                        .invokedynamic("dynjs:scope:resolve", sig(DynAtom.class, Scope.class, String.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS);
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

    public Statement defineFunction(String identifier, List<String> args) {
        return null;
    }

    public Statement defineShlOp(Statement l, Statement r) {
        return null;
    }

    public Statement defineShrOp(Statement l, Statement r) {
        return null;
    }

    public Statement defineShuOp(Statement l, Statement r) {
        return null;
    }

    public Statement defineDivOp(Statement l, Statement r) {
        return null;
    }

    public Statement defineModOp(Statement l, Statement r) {
        return null;
    }

    public Statement defineDeleteOp(Statement expression) {
        return null;
    }

    public Statement defineVoidOp(Statement expression) {
        return null;
    }

    public Statement defineTypeOfOp(Statement expression) {
        return null;
    }

    public Statement defineIncOp(Statement expression) {
        return null;
    }

    public Statement defineDecOp(Statement expression) {
        return null;
    }

    public Statement definePosOp(Statement expression) {
        return null;
    }

    public Statement defineNegOp(Statement expression) {
        return null;
    }

    public Statement defineInvOp(Statement expression) {
        return null;
    }

    public Statement defineNotOp(Statement expression) {
        return null;
    }

    public Statement definePIncOp(Statement expression) {
        return null;
    }

    public Statement definePDecOp(Statement expression) {
        return null;
    }

    public Statement defineLtRelOp(Statement l, Statement r) {
        return null;
    }

    public Statement defineGtRelOp(Statement l, Statement r) {
        return null;
    }

    public Statement defineLteRelOp(Statement l, Statement r) {
        return null;
    }

    public Statement defineGteRelOp(Statement l, Statement r) {
        return null;
    }

    public Statement defineInstanceOfRelOp(Statement l, Statement r) {
        return null;
    }

    public Statement defineInRelOp(Statement l, Statement r) {
        return null;
    }

    public Statement defineLorOp(Statement l, Statement r) {
        return null;
    }

    public Statement defineLandOp(Statement l, Statement r) {
        return null;
    }

    public Statement defineAndBitOp(Statement l, Statement r) {
        return null;
    }

    public Statement defineOrBitOp(Statement l, Statement r) {
        return null;
    }

    public Statement defineXorBitOp(Statement l, Statement r) {
        return null;
    }

    public Statement defineEqOp(Statement l, Statement r) {
        return null;
    }

    public Statement defineNEqOp(Statement l, Statement r) {
        return null;
    }

    public Statement defineSameOp(Statement l, Statement r) {
        return null;
    }

    public Statement defineNSameOp(Statement l, Statement r) {
        return null;
    }

    public Statement defineAssOp(Statement l, Statement r) {
        return null;
    }

    public Statement defineMulAssOp(Statement l, Statement r) {
        return null;
    }

    public Statement defineDivAssOp(Statement l, Statement r) {
        return null;
    }

    public Statement defineModAssOp(Statement l, Statement r) {
        return null;
    }

    public Statement defineAddAssOp(Statement l, Statement r) {
        return null;
    }

    public Statement defineSubAssOp(Statement l, Statement r) {
        return null;
    }

    public Statement defineShlAssOp(Statement l, Statement r) {
        return null;
    }

    public Statement defineShrAssOp(Statement l, Statement r) {
        return null;
    }

    public Statement defineShuAssOp(Statement l, Statement r) {
        return null;
    }

    public Statement defineAndAssOp(Statement l, Statement r) {
        return null;
    }

    public Statement defineXorAssOp(Statement l, Statement r) {
        return null;
    }

    public Statement defineOrAssOp(Statement l, Statement r) {
        return null;
    }

    public Statement defineQueOp(Statement ex1, Statement ex2, Statement ex3) {
        return null;
    }

    public Statement defineThisLiteral() {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    public Statement defineNullLiteral() {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    public Statement defineRegExLiteral(String s) {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    public Statement defineTrueLiteral() {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    public Statement defineFalseLiteral() {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    public Statement defineHexaLiteral(String s) {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    public Statement executeNew(Statement leftHandSideExpression10) {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    public Statement resolveByField(String s, Statement leftHandSideExpression13) {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    public Statement defineByIndex(Statement leftHandSideExpression11) {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }
}
