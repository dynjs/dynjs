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
import org.dynjs.parser.ast.AdditiveExpression;
import org.dynjs.parser.ast.ArrayLiteralExpression;
import org.dynjs.parser.ast.AssignmentExpression;
import org.dynjs.parser.ast.BitwiseExpression;
import org.dynjs.parser.ast.BitwiseInversionOperatorExpression;
import org.dynjs.parser.ast.BlockStatement;
import org.dynjs.parser.ast.BooleanLiteralExpression;
import org.dynjs.parser.ast.BreakStatement;
import org.dynjs.parser.ast.CaseClause;
import org.dynjs.parser.ast.CatchClause;
import org.dynjs.parser.ast.CompoundAssignmentExpression;
import org.dynjs.parser.ast.ContinueStatement;
import org.dynjs.parser.ast.DefaultCaseClause;
import org.dynjs.parser.ast.DeleteOpExpression;
import org.dynjs.parser.ast.DoWhileStatement;
import org.dynjs.parser.ast.EmptyStatement;
import org.dynjs.parser.ast.EqualityOperatorExpression;
import org.dynjs.parser.ast.Expression;
import org.dynjs.parser.ast.ExpressionList;
import org.dynjs.parser.ast.ExpressionStatement;
import org.dynjs.parser.ast.ForExprInStatement;
import org.dynjs.parser.ast.ForExprStatement;
import org.dynjs.parser.ast.ForVarDeclInStatement;
import org.dynjs.parser.ast.ForVarDeclStatement;
import org.dynjs.parser.ast.FunctionCallExpression;
import org.dynjs.parser.ast.FunctionDeclaration;
import org.dynjs.parser.ast.FunctionDescriptor;
import org.dynjs.parser.ast.FunctionExpression;
import org.dynjs.parser.ast.IdentifierReferenceExpression;
import org.dynjs.parser.ast.IfStatement;
import org.dynjs.parser.ast.InOperatorExpression;
import org.dynjs.parser.ast.InstanceofExpression;
import org.dynjs.parser.ast.LogicalExpression;
import org.dynjs.parser.ast.LogicalNotOperatorExpression;
import org.dynjs.parser.ast.MemberExpression;
import org.dynjs.parser.ast.MultiplicativeExpression;
import org.dynjs.parser.ast.NamedValue;
import org.dynjs.parser.ast.NewOperatorExpression;
import org.dynjs.parser.ast.NullLiteralExpression;
import org.dynjs.parser.ast.NumberLiteralExpression;
import org.dynjs.parser.ast.ObjectLiteralExpression;
import org.dynjs.parser.ast.PostOpExpression;
import org.dynjs.parser.ast.PreOpExpression;
import org.dynjs.parser.ast.PrintStatement;
import org.dynjs.parser.ast.Program;
import org.dynjs.parser.ast.PropertyAssignment;
import org.dynjs.parser.ast.PropertyGet;
import org.dynjs.parser.ast.PropertySet;
import org.dynjs.parser.ast.RegexpLiteralExpression;
import org.dynjs.parser.ast.RelationalExpression;
import org.dynjs.parser.ast.ReturnStatement;
import org.dynjs.parser.ast.StrictEqualityOperatorExpression;
import org.dynjs.parser.ast.StringLiteralExpression;
import org.dynjs.parser.ast.SwitchStatement;
import org.dynjs.parser.ast.TernaryExpression;
import org.dynjs.parser.ast.ThisExpression;
import org.dynjs.parser.ast.ThrowStatement;
import org.dynjs.parser.ast.TryStatement;
import org.dynjs.parser.ast.TypeOfOpExpression;
import org.dynjs.parser.ast.UnaryMinusExpression;
import org.dynjs.parser.ast.UnaryPlusExpression;
import org.dynjs.parser.ast.VariableDeclaration;
import org.dynjs.parser.ast.VariableDeclarationStatement;
import org.dynjs.parser.ast.VoidOperatorExpression;
import org.dynjs.parser.ast.WhileStatement;
import org.dynjs.parser.ast.WithStatement;

public class ASTFactory {

    public ASTFactory() {
    }

    public Program program(boolean strict, final List<Statement> blockContent) {
        return new Program(strict, blockContent);
    }
    
    public EmptyStatement emptyStatement(final Tree tree) {
        return new EmptyStatement(tree);
    }

    public BlockStatement block(final Tree tree, final List<Statement> blockContent) {
        return new BlockStatement(tree, blockContent);
    }

    public PrintStatement printStatement(final Tree tree, final Expression expr) {
        return new PrintStatement(tree, expr);
    }

    public ReturnStatement returnStatement(final Tree tree, final Expression expr) {
        return new ReturnStatement(tree, expr);
    }

    public VariableDeclarationStatement variableDeclarationStatement(final Tree tree, List<VariableDeclaration> declExprs) {
        return new VariableDeclarationStatement(tree, declExprs);
    }

    public VariableDeclaration variableDeclaration(final Tree tree, String identifier, Expression initializer) {
        return new VariableDeclaration(tree, identifier, initializer);
    }

    public AdditiveExpression defineAddOp(final Tree tree, final Expression l, final Expression r) {
        return new AdditiveExpression(tree, l, r, "+");
    }

    public AdditiveExpression defineSubOp(final Tree tree, final Expression l, final Expression r) {
        return new AdditiveExpression(tree, l, r, "-");
    }

    public MultiplicativeExpression defineMulOp(final Tree tree, final Expression l, final Expression r) {
        return new MultiplicativeExpression(tree, l, r, "*");
    }

    public MultiplicativeExpression defineDivOp(final Tree tree, Expression l, Expression r) {
        return new MultiplicativeExpression(tree, l, r, "/");
    }

    public MultiplicativeExpression defineModOp(final Tree tree, Expression l, Expression r) {
        return new MultiplicativeExpression(tree, l, r, "%");
    }

    public StringLiteralExpression defineStringLiteral(final Tree tree, final String literal) {
        return new StringLiteralExpression(tree, literal);
    }

    public Expression resolveIdentifier(final Tree tree, final String id) {
        return new IdentifierReferenceExpression(tree, id);
    }

    public NumberLiteralExpression defineNumberLiteral(final Tree tree, String value, final int radix) {
        return new NumberLiteralExpression(tree, value, radix);
    }

    public BitwiseExpression defineShlOp(final Tree tree, Expression l, Expression r) {
        return new BitwiseExpression(tree, l, r, "<<");
    }

    public BitwiseExpression defineShrOp(final Tree tree, Expression l, Expression r) {
        return new BitwiseExpression(tree, l, r, ">>");
    }

    public BitwiseExpression defineShuOp(final Tree tree, Expression l, Expression r) {
        return new BitwiseExpression(tree, l, r, ">>>");
    }

    public DeleteOpExpression defineDeleteOp(final Tree tree, final Expression expression) {
        return new DeleteOpExpression(tree, expression);
    }

    public VoidOperatorExpression defineVoidOp(final Tree tree, final Expression expression) {
        return new VoidOperatorExpression(tree, expression);
    }

    public TypeOfOpExpression defineTypeOfOp(final Tree tree, final Expression expression) {
        return new TypeOfOpExpression(tree, expression);
    }

    public Expression definePreIncOp(final Tree tree, Expression expression) {
        return new PreOpExpression(tree, expression, "++");
    }

    public Expression definePreDecOp(final Tree tree, Expression expression) {
        return new PreOpExpression(tree, expression, "--");
    }

    public Expression definePosOp(final Tree tree, Expression expression) {
        return new UnaryPlusExpression(tree, expression);
    }

    public Expression defineNegOp(final Tree tree, Expression expression) {
        return new UnaryMinusExpression(tree, expression);
    }

    public Expression defineInvOp(final Tree tree, Expression expression) {
        return new BitwiseInversionOperatorExpression(tree, expression);
    }

    public Expression defineNotOp(final Tree tree, Expression expression) {
        return new LogicalNotOperatorExpression(tree, expression);
    }

    public Expression definePostIncOp(final Tree tree, Expression expression) {
        return new PostOpExpression(tree, expression, "++");
    }

    public Expression definePostDecOp(final Tree tree, Expression expression) {
        return new PostOpExpression(tree, expression, "--");
    }

    public Expression defineLtRelOp(final Tree tree, Expression l, Expression r) {
        return new RelationalExpression(tree, l, r, "<");
    }

    public Expression defineGtRelOp(final Tree tree, final Expression l, final Expression r) {
        return new RelationalExpression(tree, l, r, ">");
    }

    public Expression defineLteRelOp(final Tree tree, Expression l, Expression r) {
        return new RelationalExpression(tree, l, r, "<=");
    }

    public Expression defineGteRelOp(final Tree tree, Expression l, Expression r) {
        return new RelationalExpression(tree, l, r, ">=");
    }

    public Expression defineInstanceOfRelOp(final Tree tree, final Expression l, final Expression r) {
        return new InstanceofExpression(tree, l, r);
    }

    public Expression defineInRelOp(final Tree tree, Expression l, Expression r) {
        return new InOperatorExpression(tree, l, r);
    }

    public LogicalExpression defineLorOp(final Tree tree, final Expression l, final Expression r) {
        return new LogicalExpression(tree, l, r, "||");
    }

    public LogicalExpression defineLandOp(final Tree tree, Expression l, Expression r) {
        return new LogicalExpression(tree, l, r, "&&");
    }

    public BitwiseExpression defineAndBitOp(final Tree tree, Expression l, Expression r) {
        return new BitwiseExpression(tree, l, r, "&");
    }

    public BitwiseExpression defineOrBitOp(final Tree tree, Expression l, Expression r) {
        return new BitwiseExpression(tree, l, r, "|");
    }

    public BitwiseExpression defineXorBitOp(final Tree tree, Expression l, Expression r) {
        return new BitwiseExpression(tree, l, r, "^");
    }

    public Expression defineEqOp(final Tree tree, final Expression l, final Expression r) {
        return new EqualityOperatorExpression(tree, l, r, "==");
    }

    public Expression defineNEqOp(final Tree tree, final Expression l, final Expression r) {
        return new EqualityOperatorExpression(tree, l, r, "!=");
    }

    public Expression defineSameOp(final Tree tree, Expression l, Expression r) {
        return new StrictEqualityOperatorExpression(tree, l, r, "===");
    }

    public Expression defineNSameOp(final Tree tree, Expression l, Expression r) {
        return new StrictEqualityOperatorExpression(tree, l, r, "!==");
    }

    public Expression defineAssOp(final Tree tree, final Expression l, final Expression r) {
        return new AssignmentExpression(tree, l, r);
    }

    public CompoundAssignmentExpression defineMulAssOp(final Tree tree, Expression l, Expression r) {
        return new CompoundAssignmentExpression(tree, defineMulOp(tree, l, r));
    }

    public CompoundAssignmentExpression defineDivAssOp(final Tree tree, Expression l, Expression r) {
        return new CompoundAssignmentExpression(tree, defineDivOp(tree, l, r));
    }

    public CompoundAssignmentExpression defineModAssOp(final Tree tree, Expression l, Expression r) {
        return new CompoundAssignmentExpression(tree, defineModOp(tree, l, r));
    }

    public CompoundAssignmentExpression defineAddAssOp(final Tree tree, final Expression l, final Expression r) {
        return new CompoundAssignmentExpression(tree, defineAddOp(tree, l, r));
    }

    public CompoundAssignmentExpression defineSubAssOp(final Tree tree, Expression l, Expression r) {
        return new CompoundAssignmentExpression(tree, defineSubOp(tree, l, r));
    }

    public CompoundAssignmentExpression defineShlAssOp(final Tree tree, Expression l, Expression r) {
        return new CompoundAssignmentExpression(tree, defineShlOp(tree, l, r));
    }

    public CompoundAssignmentExpression defineShrAssOp(final Tree tree, Expression l, Expression r) {
        return new CompoundAssignmentExpression(tree, defineShrOp(tree, l, r));
    }

    public CompoundAssignmentExpression defineShuAssOp(final Tree tree, Expression l, Expression r) {
        return new CompoundAssignmentExpression(tree, defineShuOp(tree, l, r));
    }

    public CompoundAssignmentExpression defineAndAssOp(final Tree tree, Expression l, Expression r) {
        return new CompoundAssignmentExpression(tree, defineAndBitOp(tree, l, r));
    }

    public CompoundAssignmentExpression defineXorAssOp(final Tree tree, Expression l, Expression r) {
        return new CompoundAssignmentExpression(tree, defineXorBitOp(tree, l, r));
    }

    public CompoundAssignmentExpression defineOrAssOp(final Tree tree, Expression l, Expression r) {
        return new CompoundAssignmentExpression(tree, defineOrBitOp(tree, l, r));
    }

    public TernaryExpression defineQueOp(final Tree tree, Expression vbool, Expression thenExpr, Expression elseExpr) {
        return new TernaryExpression(tree, vbool, thenExpr, elseExpr);
    }

    public Expression defineThisLiteral(final Tree tree) {
        return new ThisExpression(tree);
    }

    public Expression defineNullLiteral(final Tree tree) {
        return new NullLiteralExpression(tree);
    }

    public Expression defineRegExLiteral(final Tree tree, String text) {
        return new RegexpLiteralExpression(tree, text);
    }

    public BooleanLiteralExpression defineTrueLiteral(final Tree tree) {
        return new BooleanLiteralExpression(tree, true);
    }

    public BooleanLiteralExpression defineFalseLiteral(final Tree tree) {
        return new BooleanLiteralExpression(tree, false);
    }

    public Statement expressionStatement(Expression expr) {
        if (expr instanceof FunctionExpression) {
            return new FunctionDeclaration(((FunctionExpression) expr).getDescriptor());
        }
        return new ExpressionStatement(expr);
    }

    public FunctionDescriptor functionDescriptor(final Tree tree, String identifier, final List<String> formalParameters, final Statement block) {
        return new FunctionDescriptor(tree, identifier, formalParameters.toArray(new String[formalParameters.size()]), block);
    }

    public FunctionExpression functionExpression(FunctionDescriptor descriptor) {
        return new FunctionExpression(descriptor);
    }

    public NewOperatorExpression newOperatorExpression(final Tree tree, final Expression expr, final List<Expression> argExprs) {
        return new NewOperatorExpression(tree, expr, argExprs);
    }

    public IfStatement ifStatement(final Tree tree, Expression vbool, Statement vthen, Statement velse) {
        return new IfStatement(tree, vbool, vthen, velse);
    }

    public Statement doStatement(final Tree tree, final Expression vbool, final Statement vloop) {
        return new DoWhileStatement(tree, vbool, vloop);
    }

    public Statement whileStatement(final Tree tree, final Expression vbool, final Statement vloop) {
        return new WhileStatement(tree, vbool, vloop);
    }

    public Statement forStepVar(final Tree tree, final VariableDeclarationStatement varDef, final Expression test, final Expression incr, Statement block) {
        return new ForVarDeclStatement(tree, varDef, test, incr, block);
    }

    public Statement forStepExpr(final Tree tree, final Expression init, final Expression test, final Expression incr, final Statement block) {
        return new ForExprStatement(tree, init, test, incr, block);
    }

    public Statement forIterVar(final Tree tree, final VariableDeclarationStatement decl, final Expression rhs, final Statement block) {
        return new ForVarDeclInStatement(tree, decl, rhs, block);
    }

    public Statement forIterExpr(final Tree tree, final Expression init, final Expression rhs, final Statement block) {
        return new ForExprInStatement(tree, init, rhs, block);
    }

    public Statement continueStatement(final Tree tree, String id) {
        return new ContinueStatement(tree, id);
    }

    public Statement breakStatement(final Tree tree, String id) {
        return new BreakStatement(tree, id);
    }

    public Expression exprList(final List<Expression> exprList) {
        return new ExpressionList(null, exprList);
    }

    public MemberExpression memberExpression(final Tree tree, Expression memberExpr, Expression identifierExpr) {
        return new MemberExpression(tree, memberExpr, identifierExpr);
    }

    public Expression resolveCallExpr(final Tree tree, Expression lhs, List<Expression> args) {
        return new FunctionCallExpression(tree, lhs, args);
    }

    public Statement switchStatement(final Tree tree, Expression expr, List<CaseClause> caseClauses) {
        return new SwitchStatement(tree, expr, caseClauses);
    }

    public CaseClause switchCaseClause(final Tree tree, Expression expr, List<Statement> block) {
        return new CaseClause(expr, new BlockStatement(tree, block));
    }
    
    public DefaultCaseClause switchDefaultClause(final Tree tree, List<Statement> block) {
        return new DefaultCaseClause( new BlockStatement( tree, block) );
    }

    public Statement throwStatement(final Tree tree, final Expression expression) {
        return new ThrowStatement(tree, expression);
    }

    public TryStatement tryStatement(final Tree tree, Statement tryBlock, CatchClause catchBlock, Statement finallyBlock) {
        return new TryStatement(tree, tryBlock, catchBlock, finallyBlock);
    }

    public CatchClause tryCatchClause(final Tree tree, String id, Statement block) {
        return new CatchClause(tree, id, block);
    }

    public Statement tryFinallyClause(final Tree tree, Statement block) {
        return block;
    }

    public Statement withStatement(final Tree tree, Expression expression, Statement statement) {
        return new WithStatement(tree, expression, statement);
    }

    public Statement labelledStatement(String label, Statement statement) {
        statement.addLabel(label);
        return statement;
    }

    public Expression objectValue(final Tree tree, List<PropertyAssignment> propAssignments) {
        return new ObjectLiteralExpression(tree, propAssignments);
    }

    public Statement propertyNameNumeric(Statement numericLiteral) {
        throw new ParserException("not implemented yet", numericLiteral.getPosition());
    }

    public NamedValue namedValue(final Tree tree, final String name, final Expression expr) {
        return new NamedValue(name, expr);
    }

    public PropertySet propertySet(final Tree tree, final String name, final String identifier, Statement block) {
        return new PropertySet(name, identifier, block);
    }

    public PropertyGet propertyGet(final Tree tree, final String name, Statement block) {
        return new PropertyGet( name, block);
    }

    public Expression arrayLiteral(final Tree tree, final List<Expression> exprs) {
        return new ArrayLiteralExpression(tree, exprs);
    }

}
