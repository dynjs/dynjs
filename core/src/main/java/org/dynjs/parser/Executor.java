/**
 *  Copyright 2011 Douglas Campos
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
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.parser.statement.BlockStatement;
import org.dynjs.parser.statement.BooleanLiteralStatement;
import org.dynjs.parser.statement.CallStatement;
import org.dynjs.parser.statement.DeclareVarStatement;
import org.dynjs.parser.statement.DefineNumOpStatement;
import org.dynjs.parser.statement.DoWhileStatement;
import org.dynjs.parser.statement.EqualsOperationStatement;
import org.dynjs.parser.statement.ForStepVarStatement;
import org.dynjs.parser.statement.FunctionStatement;
import org.dynjs.parser.statement.IfStatement;
import org.dynjs.parser.statement.LogicalOperationStatement;
import org.dynjs.parser.statement.NotEqualsOperationStatement;
import org.dynjs.parser.statement.NotOperationStatement;
import org.dynjs.parser.statement.NullLiteralStatement;
import org.dynjs.parser.statement.NumberLiteralStatement;
import org.dynjs.parser.statement.OperationAssignmentStatement;
import org.dynjs.parser.statement.PostDecrementStatement;
import org.dynjs.parser.statement.PostIncrementStatement;
import org.dynjs.parser.statement.PreDecrementStatement;
import org.dynjs.parser.statement.PreIncrementStatement;
import org.dynjs.parser.statement.RelationalOperationStatement;
import org.dynjs.parser.statement.ResolveIdentifierStatement;
import org.dynjs.parser.statement.ReturnStatement;
import org.dynjs.parser.statement.StringLiteralStatement;
import org.dynjs.parser.statement.WhileStatement;
import org.dynjs.runtime.DynThreadContext;

import java.util.List;

import static me.qmx.jitescript.CodeBlock.newCodeBlock;
import static me.qmx.jitescript.util.CodegenUtils.ci;
import static me.qmx.jitescript.util.CodegenUtils.p;

public class Executor implements Opcodes {

    private DynJSCompiler compiler = new DynJSCompiler();
    private final DynThreadContext context;

    public Executor(DynThreadContext context) {
        this.context = context;
    }

    public DynThreadContext getContext() {
        return context;
    }

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
                return newCodeBlock(expr.getCodeBlock())
                        .aprintln();
            }
        };
    }

    public Statement returnStatement(final Statement expr) {
        return new ReturnStatement(expr);
    }

    public Statement declareVar(final CommonTree id) {
        return declareVar(id, new Statement() {
            @Override
            public CodeBlock getCodeBlock() {
                return newCodeBlock()
                        .getstatic(p(DynThreadContext.class), "UNDEFINED", ci(Object.class));
            }
        });
    }

    public Statement declareVar(final CommonTree id, final Statement expr) {
        return declareVar(id.getText(), expr);
    }

    public Statement declareVar(final String id, final Statement expr) {
        return new DeclareVarStatement(expr, id);
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
        return new DefineNumOpStatement(op, l, r);
    }

    public Statement defineStringLiteral(final String literal) {
        return new StringLiteralStatement(literal);
    }

    public Statement resolveIdentifier(final CommonTree id) {
        return new ResolveIdentifierStatement(id.getText());
    }

    public Statement defineNumberLiteral(final String value, final int radix) {
        return new NumberLiteralStatement(value, radix);
    }

    public Statement defineFunction(final String identifier, final List<String> args, final Statement block) {
        return new FunctionStatement(getContext(), identifier, args, block);
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
        return defineNumOp("div", l, r);
    }

    public Statement defineModOp(Statement l, Statement r) {
        return defineNumOp("mod", l, r);
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
        return new PreIncrementStatement(expression);
    }

    public Statement defineDecOp(Statement expression) {
        return new PreDecrementStatement(expression);
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
        return new NotOperationStatement(expression);
    }

    public Statement definePIncOp(Statement expression) {
        return new PostIncrementStatement(expression);
    }

    public Statement definePDecOp(Statement expression) {
        return new PostDecrementStatement(expression);
    }

    public Statement defineLtRelOp(Statement l, Statement r) {
        return new RelationalOperationStatement("lt", l, r);
    }

    public Statement defineGtRelOp(final Statement l, final Statement r) {
        return new RelationalOperationStatement("gt", l, r);
    }

    public Statement defineLteRelOp(Statement l, Statement r) {
        return new RelationalOperationStatement("le", l, r);
    }

    public Statement defineGteRelOp(Statement l, Statement r) {
        return new RelationalOperationStatement("ge", l, r);
    }

    public Statement defineInstanceOfRelOp(Statement l, Statement r) {
        return null;
    }

    public Statement defineInRelOp(Statement l, Statement r) {
        return null;
    }

    public Statement defineLorOp(final Statement l, final Statement r) {
        return new LogicalOperationStatement("lor", l, r);
    }

    public Statement defineLandOp(Statement l, Statement r) {
        return new LogicalOperationStatement("land", l, r);
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

    public Statement defineEqOp(final Statement l, final Statement r) {
        return new EqualsOperationStatement(l, r);
    }

    public Statement defineNEqOp(final Statement l, final Statement r) {
        return new NotEqualsOperationStatement(l, r);
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
        return new OperationAssignmentStatement("mul", l, r);
    }

    public Statement defineDivAssOp(Statement l, Statement r) {
        return new OperationAssignmentStatement("div", l, r);
    }

    public Statement defineModAssOp(Statement l, Statement r) {
        return new OperationAssignmentStatement("mod", l, r);
    }

    public Statement defineAddAssOp(final Statement l, final Statement r) {
        return new OperationAssignmentStatement("add", l, r);
    }

    public Statement defineSubAssOp(Statement l, Statement r) {
        return new OperationAssignmentStatement("sub", l, r);
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
        return new IfStatement(context, ex1, ex2, ex3);
    }

    public Statement defineThisLiteral() {
        return null;
    }

    public Statement defineNullLiteral() {
        return new NullLiteralStatement();
    }

    public Statement defineRegExLiteral(String s) {
        return null;
    }

    public Statement defineTrueLiteral() {
        return new BooleanLiteralStatement("TRUE");
    }

    public Statement defineFalseLiteral() {
        return new BooleanLiteralStatement("FALSE");
    }

    public Statement executeNew(Statement leftHandSideExpression10) {
        return null;
    }

    public Statement resolveByField(Statement lhs, String field) {
        return null;
    }

    public Statement defineByIndex(Statement lhs, Statement index) {
        return null;
    }

    public Statement ifStatement(Statement vbool, Statement vthen, Statement velse) {
        return new IfStatement(getContext(), vbool, vthen, velse);
    }

    public Statement doStatement(Statement vbool, Statement vloop) {
        return new DoWhileStatement(vbool, vloop);
    }

    public Statement whileStatement(final Statement vbool, final Statement vloop) {
        return new WhileStatement(vbool, vloop);
    }

    public Statement forStepVar(Statement varDef, Statement expr1, Statement expr2, Statement statement) {
        return new ForStepVarStatement(varDef, expr1, expr2, statement);
    }

    public Statement forStepExpr(Statement expr1, Statement expr2, Statement expr3, Statement statement) {
        return null;
    }

    public Statement forIterVar(Statement varDef, Statement expr1, Statement statement) {
        return null;
    }

    public Statement forIterExpr(Statement expr1, Statement expr2, Statement statement) {
        return null;
    }

    public Statement continueStatement(String id) {
        return null;
    }

    public Statement breakStatement(String id) {
        return null;
    }

    public Statement exprListStatement(List<Statement> exprList) {
        return null;
    }

    public Statement resolveCallExpr(Statement lhs, List<Statement> args) {
        return new CallStatement(getContext(), lhs, args);
    }

    public Statement switchStatement(Statement expr, Statement _default, List<Statement> cases) {
        return null;
    }

    public Statement switchCaseClause(Statement expr, List<Statement> statements) {
        return null;
    }

    public Statement switchDefaultClause(List<Statement> statements) {
        return null;
    }

    public Statement throwStatement(Statement expression) {
        return null;
    }

    public Statement tryStatement(Statement block, Statement _catch, Statement _finally) {
        return null;
    }

    public Statement tryCatchClause(String id, Statement block) {
        return null;
    }

    public Statement tryFinallyClause(Statement block) {
        return null;
    }

    public Statement withStatement(Statement expression, Statement statement) {
        return null;
    }

    public Statement labelledStatement(String label, Statement statement) {
        return null;
    }

    public Statement objectValue(List<Statement> namedValues) {
        return null;
    }

    public Statement propertyNameId(String id) {
        return null;
    }

    public Statement propertyNameString(String string) {
        return null;
    }

    public Statement propertyNameNumeric(Statement numericLiteral) {
        return null;
    }

    public Statement namedValue(Statement propertyName, Statement expr) {
        return null;
    }

    public Statement arrayLiteral(List<Statement> exprs) {
        return null;
    }

}
