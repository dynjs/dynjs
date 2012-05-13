/**
 *  Copyright 2012 Douglas Campos, and individual contributors
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

import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.statement.ArrayLiteralStatement;
import org.dynjs.parser.statement.AssignmentOperationStatement;
import org.dynjs.parser.statement.BlockStatement;
import org.dynjs.parser.statement.BooleanLiteralStatement;
import org.dynjs.parser.statement.CallStatement;
import org.dynjs.parser.statement.ContinueStatement;
import org.dynjs.parser.statement.DeclareVarStatement;
import org.dynjs.parser.statement.DefineNumOpStatement;
import org.dynjs.parser.statement.DeleteOpStatement;
import org.dynjs.parser.statement.DoWhileStatement;
import org.dynjs.parser.statement.EqualsOperationStatement;
import org.dynjs.parser.statement.ExpressionListStatement;
import org.dynjs.parser.statement.ForStepVarStatement;
import org.dynjs.parser.statement.FunctionStatement;
import org.dynjs.parser.statement.IfStatement;
import org.dynjs.parser.statement.InstanceOfRelOpStatement;
import org.dynjs.parser.statement.LogicalOperationStatement;
import org.dynjs.parser.statement.NamedValueStatement;
import org.dynjs.parser.statement.NewStatement;
import org.dynjs.parser.statement.NotEqualsOperationStatement;
import org.dynjs.parser.statement.NotOperationStatement;
import org.dynjs.parser.statement.NullLiteralStatement;
import org.dynjs.parser.statement.NumberLiteralStatement;
import org.dynjs.parser.statement.ObjectLiteralStatement;
import org.dynjs.parser.statement.OperationAssignmentStatement;
import org.dynjs.parser.statement.PostDecrementStatement;
import org.dynjs.parser.statement.PostIncrementStatement;
import org.dynjs.parser.statement.PreDecrementStatement;
import org.dynjs.parser.statement.PreIncrementStatement;
import org.dynjs.parser.statement.PrintStatement;
import org.dynjs.parser.statement.RelationalOperationStatement;
import org.dynjs.parser.statement.ResolveByIndexStatement;
import org.dynjs.parser.statement.ResolveIdentifierStatement;
import org.dynjs.parser.statement.ReturnStatement;
import org.dynjs.parser.statement.StringLiteralStatement;
import org.dynjs.parser.statement.ThrowStatement;
import org.dynjs.parser.statement.TypeOfOpExpressionStatement;
import org.dynjs.parser.statement.UndefinedValueStatement;
import org.dynjs.parser.statement.VoidOpStatement;
import org.dynjs.parser.statement.WhileStatement;
import org.dynjs.runtime.DynThreadContext;
import org.objectweb.asm.tree.LabelNode;

import java.util.List;
import java.util.Stack;

public class Executor {

    private final DynThreadContext context;
    private final Stack<LabelNode> labelStack = new Stack<>();

    public Executor(DynThreadContext context) {
        this.context = context;
    }

    public DynThreadContext getContext() {
        return context;
    }

    public List<Statement> program(final List<Statement> blockContent) {
        return blockContent;
    }

    public Statement block(final Tree tree, final List<Statement> blockContent) {
        return new BlockStatement(tree, blockContent);
    }

    public Statement printStatement(final Tree tree, final Statement expr) {
        return new PrintStatement(tree, expr);
    }

    public Statement returnStatement(final Tree tree, final Statement expr) {
        return new ReturnStatement(tree, expr);
    }

    public Statement declareVar(final Tree tree, final Tree treeId, final String id) {
        return declareVar(tree, treeId, id, new UndefinedValueStatement());
    }

    public Statement declareVar(final Tree tree, final Tree treeId, final String id, final Statement expr) {
        return new DeclareVarStatement(tree, treeId, expr, id);
    }

    public Statement defineAddOp(final Tree tree, final Statement l, final Statement r) {
        return defineNumOp(tree, "add", l, r);
    }

    public Statement defineSubOp(final Tree tree, final Statement l, final Statement r) {
        return defineNumOp(tree, "sub", l, r);
    }

    public Statement defineMulOp(final Tree tree, final Statement l, final Statement r) {
        return defineNumOp(tree, "mul", l, r);
    }

    public Statement defineNumOp(final Tree tree, final String op, final Statement l, final Statement r) {
        return new DefineNumOpStatement(tree, op, l, r);
    }

    public Statement defineStringLiteral(final Tree tree, final String literal) {
        return new StringLiteralStatement(tree, literal);
    }

    public Statement resolveIdentifier(final Tree tree, final String id) {
        return new ResolveIdentifierStatement(tree, id);
    }

    public Statement defineNumberLiteral(final Tree tree, String value, final int radix) {
        return new NumberLiteralStatement(tree, value, radix);
    }

    public Statement defineFunction(final Tree tree, final String identifier, final List<String> args, final Statement block) {
        return new FunctionStatement(tree, getContext(), identifier, args, block);
    }

    public Statement defineShlOp(final Tree tree, Statement l, Statement r) {
        throw new ParserException("not implemented yet", tree);
    }

    public Statement defineShrOp(final Tree tree, Statement l, Statement r) {
        throw new ParserException("not implemented yet", tree);
    }

    public Statement defineShuOp(final Tree tree, Statement l, Statement r) {
        throw new ParserException("not implemented yet", tree);
    }

    public Statement defineDivOp(final Tree tree, Statement l, Statement r) {
        return defineNumOp(tree, "div", l, r);
    }

    public Statement defineModOp(final Tree tree, Statement l, Statement r) {
        return defineNumOp(tree, "mod", l, r);
    }

    public Statement defineDeleteOp(final Tree tree, final Statement expression) {
        return new DeleteOpStatement(tree, expression);
    }

    public Statement defineVoidOp(final Tree tree, final Statement expression) {
        return new VoidOpStatement(tree, expression);
    }

    public Statement defineTypeOfOp(final Tree tree, final Statement expression) {
        return new TypeOfOpExpressionStatement(tree, expression);
    }

    public Statement defineIncOp(final Tree tree, Statement expression) {
        return new PreIncrementStatement(tree, expression);
    }

    public Statement defineDecOp(final Tree tree, Statement expression) {
        return new PreDecrementStatement(tree, expression);
    }

    public Statement definePosOp(final Tree tree, Statement expression) {
        throw new ParserException("not implemented yet", tree);
    }

    public Statement defineNegOp(final Tree tree, Statement expression) {
        throw new ParserException("not implemented yet", tree);
    }

    public Statement defineInvOp(final Tree tree, Statement expression) {
        throw new ParserException("not implemented yet", tree);
    }

    public Statement defineNotOp(final Tree tree, Statement expression) {
        return new NotOperationStatement(tree, expression);
    }

    public Statement definePIncOp(final Tree tree, Statement expression) {
        return new PostIncrementStatement(tree, expression);
    }

    public Statement definePDecOp(final Tree tree, Statement expression) {
        return new PostDecrementStatement(tree, expression);
    }

    public Statement defineLtRelOp(final Tree tree, Statement l, Statement r) {
        return new RelationalOperationStatement(tree, "lt", l, r);
    }

    public Statement defineGtRelOp(final Tree tree, final Statement l, final Statement r) {
        return new RelationalOperationStatement(tree, "gt", l, r);
    }

    public Statement defineLteRelOp(final Tree tree, Statement l, Statement r) {
        return new RelationalOperationStatement(tree, "le", l, r);
    }

    public Statement defineGteRelOp(final Tree tree, Statement l, Statement r) {
        return new RelationalOperationStatement(tree, "ge", l, r);
    }

    public Statement defineInstanceOfRelOp(final Tree tree, final Statement l, final Statement r) {
        return new InstanceOfRelOpStatement(tree, l, r);
    }

    public Statement defineInRelOp(final Tree tree, Statement l, Statement r) {
        throw new ParserException("not implemented yet", tree);
    }

    public Statement defineLorOp(final Tree tree, final Statement l, final Statement r) {
        return new LogicalOperationStatement(tree, "lor", l, r);
    }

    public Statement defineLandOp(final Tree tree, Statement l, Statement r) {
        return new LogicalOperationStatement(tree, "land", l, r);
    }

    public Statement defineAndBitOp(final Tree tree, Statement l, Statement r) {
        throw new ParserException("not implemented yet", tree);
    }

    public Statement defineOrBitOp(final Tree tree, Statement l, Statement r) {
        throw new ParserException("not implemented yet", tree);
    }

    public Statement defineXorBitOp(final Tree tree, Statement l, Statement r) {
        throw new ParserException("not implemented yet", tree);
    }

    public Statement defineEqOp(final Tree tree, final Statement l, final Statement r) {
        return new EqualsOperationStatement(tree, l, r);
    }

    public Statement defineNEqOp(final Tree tree, final Statement l, final Statement r) {
        return new NotEqualsOperationStatement(tree, l, r);
    }

    public Statement defineSameOp(final Tree tree, Statement l, Statement r) {
        return new EqualsOperationStatement(tree, l, r);
    }

    public Statement defineNSameOp(final Tree tree, Statement l, Statement r) {
        return new NotEqualsOperationStatement(tree, l, r);
    }

    public Statement defineAssOp(final Tree tree, final Statement l, final Statement r) {
        return new AssignmentOperationStatement(tree, l, r);
    }

    public Statement defineMulAssOp(final Tree tree, Statement l, Statement r) {
        return new OperationAssignmentStatement(tree, "mul", l, r);
    }

    public Statement defineDivAssOp(final Tree tree, Statement l, Statement r) {
        return new OperationAssignmentStatement(tree, "div", l, r);
    }

    public Statement defineModAssOp(final Tree tree, Statement l, Statement r) {
        return new OperationAssignmentStatement(tree, "mod", l, r);
    }

    public Statement defineAddAssOp(final Tree tree, final Statement l, final Statement r) {
        return new OperationAssignmentStatement(tree, "add", l, r);
    }

    public Statement defineSubAssOp(final Tree tree, Statement l, Statement r) {
        return new OperationAssignmentStatement(tree, "sub", l, r);
    }

    public Statement defineShlAssOp(final Tree tree, Statement l, Statement r) {
        throw new ParserException("not implemented yet", tree);
    }

    public Statement defineShrAssOp(final Tree tree, Statement l, Statement r) {
        throw new ParserException("not implemented yet", tree);
    }

    public Statement defineShuAssOp(final Tree tree, Statement l, Statement r) {
        throw new ParserException("not implemented yet", tree);
    }

    public Statement defineAndAssOp(final Tree tree, Statement l, Statement r) {
        throw new ParserException("not implemented yet", tree);
    }

    public Statement defineXorAssOp(final Tree tree, Statement l, Statement r) {
        throw new ParserException("not implemented yet", tree);
    }

    public Statement defineOrAssOp(final Tree tree, Statement l, Statement r) {
        throw new ParserException("not implemented yet", tree);
    }

    public Statement defineQueOp(final Tree tree, Statement ex1, Statement ex2, Statement ex3) {
        return new IfStatement(tree, context, ex1, ex2, ex3);
    }

    public Statement defineThisLiteral(final Tree tree) {
        throw new ParserException("not implemented yet", tree);
    }

    public Statement defineNullLiteral(final Tree tree) {
        return new NullLiteralStatement(tree);
    }

    public Statement defineRegExLiteral(final Tree tree) {
        throw new ParserException("not implemented yet", tree);
    }

    public Statement defineTrueLiteral(final Tree tree) {
        return new BooleanLiteralStatement(tree, "TRUE");
    }

    public Statement defineFalseLiteral(final Tree tree) {
        return new BooleanLiteralStatement(tree, "FALSE");
    }

    public Statement executeNew(final Tree tree, final Statement statement) {
        return new NewStatement(tree, statement);
    }

    public Statement resolveByField(final Tree tree, final Statement lhs, final Tree treeField, final String field) {
        return new ResolveByIndexStatement(tree, lhs, treeField, field);
    }

    public Statement resolveByIndex(final Tree tree, final Statement lhs, final Statement index) {
        return new ResolveByIndexStatement(tree, lhs, index);
    }

    public Statement ifStatement(final Tree tree, Statement vbool, Statement vthen, Statement velse) {
        return new IfStatement(tree, getContext(), vbool, vthen, velse);
    }

    public Statement doStatement(final Tree tree, final Statement vbool, final Statement vloop) {
        return new DoWhileStatement(tree, vbool, vloop);
    }

    public Statement whileStatement(final Tree tree, final Statement vbool, final Statement vloop) {
        return new WhileStatement(tree, vbool, vloop);
    }

    public Statement forStepVar(final Tree tree, final Statement varDef, final Statement expr1, final Statement expr2, Statement statement) {
        return new ForStepVarStatement(labelStack, tree, varDef, expr1, expr2, statement);
    }

    public Statement forStepExpr(final Tree tree, final Statement expr1, final Statement expr2, final Statement expr3, Statement statement) {
        throw new ParserException("not implemented yet", tree);
    }

    public Statement forIterVar(final Tree tree, final Statement varDef, final Statement expr1, final Statement statement) {
        throw new ParserException("not implemented yet", tree);
    }

    public Statement forIterExpr(final Tree tree, final Statement expr1, final Statement expr2, final Statement statement) {
        throw new ParserException("not implemented yet", tree);
    }

    public Statement continueStatement(final Tree tree, String id) {
        return new ContinueStatement(labelStack, tree, id);
    }

    public Statement breakStatement(final Tree tree, String id) {
        throw new ParserException("not implemented yet", tree);
    }

    public Statement exprListStatement(final List<Statement> exprList) {
        return new ExpressionListStatement(null, exprList);
    }

    public Statement resolveCallExpr(final Tree tree, Statement lhs, List<Statement> args) {
        return new CallStatement(tree, getContext(), lhs, args);
    }

    public Statement switchStatement(final Tree tree, Statement expr, Statement _default, List<Statement> cases) {
        throw new ParserException("not implemented yet", tree);
    }

    public Statement switchCaseClause(final Tree tree, Statement expr, List<Statement> statements) {
        throw new ParserException("not implemented yet", tree);
    }

    public Statement switchDefaultClause(final Tree tree, List<Statement> statements) {
        throw new ParserException("not implemented yet", tree);
    }

    public Statement throwStatement(final Tree tree, final Statement expression) {
        return new ThrowStatement(tree);
    }

    public Statement tryStatement(final Tree tree, Statement block, Statement _catch, Statement _finally) {
        throw new ParserException("not implemented yet", tree);
    }

    public Statement tryCatchClause(final Tree tree, String id, Statement block) {
        throw new ParserException("not implemented yet", tree);
    }

    public Statement tryFinallyClause(final Tree tree, Statement block) {
        throw new ParserException("not implemented yet", tree);
    }

    public Statement withStatement(final Tree tree, Statement expression, Statement statement) {
        throw new ParserException("not implemented yet", tree);
    }

    public Statement labelledStatement(final Tree tree, String label, Statement statement) {
        throw new ParserException("not implemented yet", tree);
    }

    public Statement objectValue(final Tree tree, List<Statement> namedValues) {
        return new ObjectLiteralStatement(tree, namedValues);
    }

    public Statement propertyNameId(final Tree tree, final String id) {
        return new StringLiteralStatement(tree, id);
    }

    public Statement propertyNameString(final Tree tree, final String string) {
        return new StringLiteralStatement(tree, string);
    }

    public Statement propertyNameNumeric(Statement numericLiteral) {
        throw new ParserException("not implemented yet", numericLiteral.getPosition());
    }

    public Statement namedValue(final Tree tree, final Statement propertyName, final Statement expr) {
        return new NamedValueStatement(tree, propertyName, expr);
    }

    public Statement arrayLiteral(final Tree tree, final List<Statement> exprs) {
        return new ArrayLiteralStatement(tree, exprs);
    }

}
