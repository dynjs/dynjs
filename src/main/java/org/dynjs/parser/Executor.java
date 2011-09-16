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
import org.dynjs.api.Function;
import org.dynjs.api.Scope;
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.parser.statement.BlockStatement;
import org.dynjs.parser.statement.PrintStatement;
import org.dynjs.runtime.*;
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
                return CodeBlock.newCodeBlock()
                        .getstatic(p(DynPrimitiveUndefined.class), "UNDEFINED", ci(DynPrimitiveUndefined.class))
                        .aload(2)
                        .swap()
                        .ldc(id.getText())
                        .swap()
                        .invokeinterface(p(Scope.class), "define", sig(void.class, String.class, DynAtom.class));
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

    public Statement printStatement(final DynAtom expression) {
        return new PrintStatement(expression);
    }

    public DynString createDynString(final CommonTree stringLiteral) {
        return new DynString(stringLiteral.getText());
    }

    public Statement block(final List<Statement> blockContent) {
        return new BlockStatement(blockContent);
    }

    public Function createFunction(List<String> args, final Statement statement) {
        DynFunction function = new DynFunction(args.toArray(new String[]{})) {
            public CodeBlock getCodeBlock() {
                return CodeBlock.newCodeBlock(statement.getCodeBlock())
                        .aconst_null()
                        .areturn();
            }
        };
        return compiler.compile(function);
    }

    public Function createNewObject(Function function) {
        Function constructor = (Function) Functions.GET_PROPERTY.call(null, null, function, new DynString("construct"));
        return constructor;
    }

    public DynAtom constructNewObject(Function lhs) {
        Function ctor = (Function) lhs.call(null, null, new DynAtom[]{lhs, new DynString("construct")});
        return ctor.call(null, null);
    }

    public DynAtom callExpression(Function lhs, List<DynAtom> args) {
        return lhs.call(new DynThreadContext(), new DynObject(), args.toArray(new DynAtom[]{}));
    }
}
