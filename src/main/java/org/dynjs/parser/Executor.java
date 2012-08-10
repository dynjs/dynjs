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

import java.util.List;

import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.statement.AdditiveExpression;
import org.dynjs.parser.statement.ArrayLiteralStatement;
import org.dynjs.parser.statement.AssignmentExpression;
import org.dynjs.parser.statement.BitwiseExpression;
import org.dynjs.parser.statement.BlockStatement;
import org.dynjs.parser.statement.BooleanLiteralExpression;
import org.dynjs.parser.statement.BreakStatement;
import org.dynjs.parser.statement.CatchClause;
import org.dynjs.parser.statement.CompoundAssignmentExpression;
import org.dynjs.parser.statement.ContinueStatement;
import org.dynjs.parser.statement.DeleteOpStatement;
import org.dynjs.parser.statement.DoWhileStatement;
import org.dynjs.parser.statement.EqualsOperationStatement;
import org.dynjs.parser.statement.Expression;
import org.dynjs.parser.statement.ExpressionListStatement;
import org.dynjs.parser.statement.ExpressionStatement;
import org.dynjs.parser.statement.ForStepExprStatement;
import org.dynjs.parser.statement.ForStepVarStatement;
import org.dynjs.parser.statement.FunctionCallExpression;
import org.dynjs.parser.statement.FunctionDeclaration;
import org.dynjs.parser.statement.IdentifierReferenceExpression;
import org.dynjs.parser.statement.IfStatement;
import org.dynjs.parser.statement.InstanceOfRelOpStatement;
import org.dynjs.parser.statement.LogicalExpression;
import org.dynjs.parser.statement.MultiplicativeExpression;
import org.dynjs.parser.statement.NamedValueStatement;
import org.dynjs.parser.statement.NewOperatorExpression;
import org.dynjs.parser.statement.NotEqualsOperationStatement;
import org.dynjs.parser.statement.NotOperationStatement;
import org.dynjs.parser.statement.NullLiteralExpression;
import org.dynjs.parser.statement.ObjectLiteralStatement;
import org.dynjs.parser.statement.PostDecrementStatement;
import org.dynjs.parser.statement.PostIncrementStatement;
import org.dynjs.parser.statement.PreDecrementStatement;
import org.dynjs.parser.statement.PreIncrementStatement;
import org.dynjs.parser.statement.PrintStatement;
import org.dynjs.parser.statement.RelationalOperationStatement;
import org.dynjs.parser.statement.ResolveByIndexStatement;
import org.dynjs.parser.statement.ReturnStatement;
import org.dynjs.parser.statement.StrictEqualOperationStatement;
import org.dynjs.parser.statement.StringLiteralExpression;
import org.dynjs.parser.statement.ThisExpression;
import org.dynjs.parser.statement.ThrowStatement;
import org.dynjs.parser.statement.TryStatement;
import org.dynjs.parser.statement.TypeOfOpExpressionStatement;
import org.dynjs.parser.statement.UnaryMinusStatement;
import org.dynjs.parser.statement.VariableDeclarationExpression;
import org.dynjs.parser.statement.VariableDeclarationStatement;
import org.dynjs.parser.statement.VoidOpStatement;
import org.dynjs.parser.statement.WhileStatement;
import org.dynjs.runtime.BlockManager;

public class Executor {

    private BlockManager blockManager;

    public Executor() {
    }

    public void setBlockManager(BlockManager blockManager) {
        this.blockManager = blockManager;
    }

    public BlockManager getBlockManager() {
        return this.blockManager;
    }

    public List<Statement> program(final List<Statement> blockContent) {
        return blockContent;
    }

    public BlockStatement block(final Tree tree, final List<Statement> blockContent) {
        return new BlockStatement( tree, blockContent );
    }

    public PrintStatement printStatement(final Tree tree, final Statement expr) {
        return new PrintStatement( tree, expr );
    }

    public ReturnStatement returnStatement(final Tree tree, final Expression expr) {
        return new ReturnStatement( tree, expr );
    }

    public Statement variableDeclarationStatement(final Tree tree, List<VariableDeclarationExpression> declExprs) {
        return new VariableDeclarationStatement( tree, declExprs );
    }

    public VariableDeclarationExpression variableDeclarationExpression(final Tree tree, String identifier, Expression initializer) {
        return new VariableDeclarationExpression( tree, identifier, initializer );
    }

    public AdditiveExpression defineAddOp(final Tree tree, final Expression l, final Expression r) {
        return new AdditiveExpression( tree, l, r, "+" );
    }

    public AdditiveExpression defineSubOp(final Tree tree, final Expression l, final Expression r) {
        return new AdditiveExpression( tree, l, r, "-" );
    }

    public MultiplicativeExpression defineMulOp(final Tree tree, final Expression l, final Expression r) {
        return new MultiplicativeExpression( tree, l, r, "*" );
    }

    public MultiplicativeExpression defineDivOp(final Tree tree, Expression l, Expression r) {
        return new MultiplicativeExpression( tree, l, r, "/" );
    }

    public MultiplicativeExpression defineModOp(final Tree tree, Expression l, Expression r) {
        return new MultiplicativeExpression( tree, l, r, "%" );
    }

    public StringLiteralExpression defineStringLiteral(final Tree tree, final String literal) {
        return new StringLiteralExpression( tree, literal );
    }

    public Expression resolveIdentifier(final Tree tree, final String id) {
        return new IdentifierReferenceExpression( tree, id );
    }

    public Statement defineNumberLiteral(final Tree tree, String value, final int radix) {
        return new NumberLiteralStatement( tree, value, radix );
    }

    public Statement defineFunction(final Tree tree, final String identifier, final List<String> args, final Statement block) {
        return new FunctionDeclaration( tree, identifier, args, block );
    }

    public BitwiseExpression defineShlOp(final Tree tree, Expression l, Expression r) {
        return new BitwiseExpression( tree, l, r, "<<" );
    }

    public BitwiseExpression defineShrOp(final Tree tree, Expression l, Expression r) {
        return new BitwiseExpression( tree, l, r, ">>" );
    }

    public BitwiseExpression defineShuOp(final Tree tree, Expression l, Expression r) {
        return new BitwiseExpression( tree, l, r, ">>>" );
    }

    public Statement defineDeleteOp(final Tree tree, final Statement expression) {
        return new DeleteOpStatement( tree, expression );
    }

    public Statement defineVoidOp(final Tree tree, final Statement expression) {
        return new VoidOpStatement( tree, expression );
    }

    public Statement defineTypeOfOp(final Tree tree, final Statement expression) {
        return new TypeOfOpExpressionStatement( tree, expression );
    }

    public Statement defineIncOp(final Tree tree, Statement expression) {
        return new PreIncrementStatement( tree, expression );
    }

    public Statement defineDecOp(final Tree tree, Statement expression) {
        return new PreDecrementStatement( tree, expression );
    }

    public Statement definePosOp(final Tree tree, Statement expression) {
        throw new ParserException( "not implemented yet", tree );
    }

    public Statement defineNegOp(final Tree tree, Statement expression) {
        return new UnaryMinusStatement( tree, expression );
    }

    public Statement defineInvOp(final Tree tree, Statement expression) {
        throw new ParserException( "not implemented yet", tree );
    }

    public Statement defineNotOp(final Tree tree, Statement expression) {
        return new NotOperationStatement( tree, expression );
    }

    public Statement definePIncOp(final Tree tree, Statement expression) {
        return new PostIncrementStatement( tree, expression );
    }

    public Statement definePDecOp(final Tree tree, Statement expression) {
        return new PostDecrementStatement( tree, expression );
    }

    public Statement defineLtRelOp(final Tree tree, Statement l, Statement r) {
        return new RelationalOperationStatement( tree, "lt", l, r );
    }

    public Statement defineGtRelOp(final Tree tree, final Statement l, final Statement r) {
        return new RelationalOperationStatement( tree, "gt", l, r );
    }

    public Statement defineLteRelOp(final Tree tree, Statement l, Statement r) {
        return new RelationalOperationStatement( tree, "le", l, r );
    }

    public Statement defineGteRelOp(final Tree tree, Statement l, Statement r) {
        return new RelationalOperationStatement( tree, "ge", l, r );
    }

    public Statement defineInstanceOfRelOp(final Tree tree, final Statement l, final Statement r) {
        return new InstanceOfRelOpStatement( tree, l, r );
    }

    public Statement defineInRelOp(final Tree tree, Statement l, Statement r) {
        throw new ParserException( "not implemented yet", tree );
    }

    public LogicalExpression defineLorOp(final Tree tree, final Expression l, final Expression r) {
        return new LogicalExpression( tree, l, r, "||" );
    }

    public LogicalExpression defineLandOp(final Tree tree, Expression l, Expression r) {
        return new LogicalExpression( tree, l, r, "&&" );
    }

    public BitwiseExpression defineAndBitOp(final Tree tree, Expression l, Expression r) {
        return new BitwiseExpression( tree, l, r, "&" );
    }

    public BitwiseExpression defineOrBitOp(final Tree tree, Expression l, Expression r) {
        return new BitwiseExpression( tree, l, r, "|" );
    }

    public BitwiseExpression defineXorBitOp(final Tree tree, Expression l, Expression r) {
        return new BitwiseExpression( tree, l, r, "^" );
    }

    public Statement defineEqOp(final Tree tree, final Statement l, final Statement r) {
        return new EqualsOperationStatement( tree, l, r );
    }

    public Statement defineNEqOp(final Tree tree, final Statement l, final Statement r) {
        return new NotEqualsOperationStatement( tree, l, r );
    }

    public Statement defineSameOp(final Tree tree, Statement l, Statement r) {
        return new StrictEqualOperationStatement( tree, l, r );
    }

    public Statement defineNSameOp(final Tree tree, Statement l, Statement r) {
        return new NotEqualsOperationStatement( tree, l, r );
    }

    public Expression defineAssOp(final Tree tree, final Expression l, final Expression r) {
        return new AssignmentExpression( tree, l, r );
    }

    public CompoundAssignmentExpression defineMulAssOp(final Tree tree, Expression l, Expression r) {
        return new CompoundAssignmentExpression( tree, defineMulOp( tree, l, r ) );
    }

    public CompoundAssignmentExpression defineDivAssOp(final Tree tree, Expression l, Expression r) {
        return new CompoundAssignmentExpression( tree, defineDivOp( tree, l, r ) );
    }

    public CompoundAssignmentExpression defineModAssOp(final Tree tree, Expression l, Expression r) {
        return new CompoundAssignmentExpression( tree, defineModOp( tree, l, r ) );
    }

    public CompoundAssignmentExpression defineAddAssOp(final Tree tree, final Expression l, final Expression r) {
        return new CompoundAssignmentExpression( tree, defineAddOp( tree, l, r ) );
    }

    public CompoundAssignmentExpression defineSubAssOp(final Tree tree, Expression l, Expression r) {
        return new CompoundAssignmentExpression( tree, defineSubOp( tree, l, r ) );
    }

    public CompoundAssignmentExpression defineShlAssOp(final Tree tree, Expression l, Expression r) {
        return new CompoundAssignmentExpression( tree, defineShlOp( tree, l, r ) );
    }

    public CompoundAssignmentExpression defineShrAssOp(final Tree tree, Expression l, Expression r) {
        return new CompoundAssignmentExpression( tree, defineShrOp( tree, l, r ) );
    }

    public CompoundAssignmentExpression defineShuAssOp(final Tree tree, Expression l, Expression r) {
        return new CompoundAssignmentExpression( tree, defineShuOp( tree, l, r ) );
    }

    public CompoundAssignmentExpression defineAndAssOp(final Tree tree, Expression l, Expression r) {
        return new CompoundAssignmentExpression( tree, defineAndBitOp( tree, l, r ) );
    }

    public CompoundAssignmentExpression defineXorAssOp(final Tree tree, Expression l, Expression r) {
        return new CompoundAssignmentExpression( tree, defineXorBitOp( tree, l, r ) );
    }

    public CompoundAssignmentExpression defineOrAssOp(final Tree tree, Expression l, Expression r) {
        return new CompoundAssignmentExpression( tree, defineOrBitOp( tree, l, r ) );
    }

    public Statement defineQueOp(final Tree tree, Statement ex1, Statement ex2, Statement ex3) {
        return new IfStatement( tree, getBlockManager(), ex1, ex2, ex3 );
    }

    public Expression defineThisLiteral(final Tree tree) {
        return new ThisExpression( tree );
    }

    public Expression defineNullLiteral(final Tree tree) {
        return new NullLiteralExpression( tree );
    }

    public Statement defineRegExLiteral(final Tree tree) {
        throw new ParserException( "not implemented yet", tree );
    }

    public BooleanLiteralExpression defineTrueLiteral(final Tree tree) {
        return new BooleanLiteralExpression( tree, true );
    }

    public BooleanLiteralExpression defineFalseLiteral(final Tree tree) {
        return new BooleanLiteralExpression( tree, false );
    }

    public ExpressionStatement expressionStatement(Expression expr) {
        return new ExpressionStatement( expr );
    }

    public Statement executeNew(final Tree tree, final Statement statement) {
        return new NewOperatorExpression( tree, statement );
    }

    public Statement resolveByField(final Tree tree, final Statement lhs, final Tree treeField, final String field) {
        return new ResolveByIndexStatement( tree, lhs, treeField, field );
    }

    public Statement resolveByIndex(final Tree tree, final Statement lhs, final Statement index) {
        return new ResolveByIndexStatement( tree, lhs, index );
    }

    public Statement ifStatement(final Tree tree, Statement vbool, Statement vthen, Statement velse) {
        return new IfStatement( tree, vbool, vthen, velse );
    }

    public Statement doStatement(final Tree tree, final Statement vbool, final Statement vloop) {
        return new DoWhileStatement( tree, vbool, vloop );
    }

    public Statement whileStatement(final Tree tree, final Statement vbool, final Statement vloop) {
        return new WhileStatement( tree, vbool, vloop );
    }

    public Statement forStepVar(final Tree tree, final Statement varDef, final Statement expr1, final Statement expr2, Statement statement) {
        return new ForStepVarStatement( tree, varDef, expr1, expr2, statement );
    }

    public Statement forStepExpr(final Tree tree, final Statement initialize, final Statement test, final Statement increment, Statement statement) {
        return new ForStepExprStatement( tree, initialize, test, increment, statement );
    }

    public Statement forIterVar(final Tree tree, final Statement varDef, final Statement expr1, final Statement statement) {
        throw new ParserException( "not implemented yet", tree );
    }

    public Statement forIterExpr(final Tree tree, final Statement expr1, final Statement expr2, final Statement statement) {
        throw new ParserException( "not implemented yet", tree );
    }

    public Statement continueStatement(final Tree tree, String id) {
        return new ContinueStatement( tree, id );
    }

    public Statement breakStatement(final Tree tree, String id) {
        return new BreakStatement( tree, id );
    }

    public Statement exprListStatement(final List<Statement> exprList) {
        return new ExpressionListStatement( null, exprList );
    }

    public Expression resolveCallExpr(final Tree tree, Expression lhs, List<Expression> args) {
        return new FunctionCallExpression( tree, lhs, args );
    }

    public Statement switchStatement(final Tree tree, Statement expr, Statement _default, List<Statement> cases) {
        throw new ParserException( "not implemented yet", tree );
    }

    public Statement switchCaseClause(final Tree tree, Statement expr, List<Statement> statements) {
        throw new ParserException( "not implemented yet", tree );
    }

    public Statement switchDefaultClause(final Tree tree, List<Statement> statements) {
        throw new ParserException( "not implemented yet", tree );
    }

    public Statement throwStatement(final Tree tree, final Statement expression) {
        return new ThrowStatement( tree, expression );
    }

    public Statement tryStatement(final Tree tree, Statement tryBlock, Statement catchBlock, Statement finallyBlock) {
        return new TryStatement( tree, tryBlock, catchBlock, finallyBlock );
    }

    public Statement tryCatchClause(final Tree tree, String id, Statement block) {
        return new CatchClause( tree, id, block );
    }

    public Statement tryFinallyClause(final Tree tree, Statement block) {
        return new FinallyClause( tree, block );
    }

    public Statement withStatement(final Tree tree, Statement expression, Statement statement) {
        throw new ParserException( "not implemented yet", tree );
    }

    public Statement labelledStatement(final Tree tree, String label, Statement statement) {
        throw new ParserException( "not implemented yet", tree );
    }

    public Statement objectValue(final Tree tree, List<Statement> namedValues) {
        return new ObjectLiteralStatement( tree, namedValues );
    }

    public Statement propertyNameId(final Tree tree, final String id) {
        return new StringLiteralExpression( tree, id );
    }

    public Statement propertyNameString(final Tree tree, final String string) {
        return new StringLiteralExpression( tree, string );
    }

    public Statement propertyNameNumeric(Statement numericLiteral) {
        throw new ParserException( "not implemented yet", numericLiteral.getPosition() );
    }

    public Statement namedValue(final Tree tree, final Statement propertyName, final Statement expr) {
        return new NamedValueStatement( tree, propertyName, expr );
    }

    public Statement arrayLiteral(final Tree tree, final List<Statement> exprs) {
        return new ArrayLiteralStatement( tree, exprs );
    }

}
