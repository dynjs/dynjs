/**
 *  Copyright 2013 Douglas Campos, and individual contributors
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
package org.dynjs.parser.js;

import java.math.BigInteger;
import java.util.List;

import org.dynjs.parser.Statement;
import org.dynjs.parser.ast.AdditiveExpression;
import org.dynjs.parser.ast.ArrayLiteralExpression;
import org.dynjs.parser.ast.AssignmentExpression;
import org.dynjs.parser.ast.BitwiseExpression;
import org.dynjs.parser.ast.BitwiseInversionOperatorExpression;
import org.dynjs.parser.ast.BlockStatement;
import org.dynjs.parser.ast.BooleanLiteralExpression;
import org.dynjs.parser.ast.BracketExpression;
import org.dynjs.parser.ast.BreakStatement;
import org.dynjs.parser.ast.CaseClause;
import org.dynjs.parser.ast.CatchClause;
import org.dynjs.parser.ast.CommaOperator;
import org.dynjs.parser.ast.CompoundAssignmentExpression;
import org.dynjs.parser.ast.ContinueStatement;
import org.dynjs.parser.ast.DebuggerStatement;
import org.dynjs.parser.ast.DefaultCaseClause;
import org.dynjs.parser.ast.DeleteOpExpression;
import org.dynjs.parser.ast.DoWhileStatement;
import org.dynjs.parser.ast.DotExpression;
import org.dynjs.parser.ast.EmptyStatement;
import org.dynjs.parser.ast.EqualityOperatorExpression;
import org.dynjs.parser.ast.Expression;
import org.dynjs.parser.ast.ExpressionStatement;
import org.dynjs.parser.ast.FloatingNumberExpression;
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
import org.dynjs.parser.ast.IntegerNumberExpression;
import org.dynjs.parser.ast.LogicalExpression;
import org.dynjs.parser.ast.LogicalNotOperatorExpression;
import org.dynjs.parser.ast.MultiplicativeExpression;
import org.dynjs.parser.ast.NewOperatorExpression;
import org.dynjs.parser.ast.NullLiteralExpression;
import org.dynjs.parser.ast.NumberLiteralExpression;
import org.dynjs.parser.ast.ObjectLiteralExpression;
import org.dynjs.parser.ast.Parameter;
import org.dynjs.parser.ast.PostOpExpression;
import org.dynjs.parser.ast.PreOpExpression;
import org.dynjs.parser.ast.ProgramTree;
import org.dynjs.parser.ast.PropertyAssignment;
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
import org.dynjs.parser.ast.VariableStatement;
import org.dynjs.parser.ast.VoidOperatorExpression;
import org.dynjs.parser.ast.WhileStatement;
import org.dynjs.parser.ast.WithStatement;
import org.dynjs.runtime.Types;

public class ASTFactory {

    public ASTFactory() {
    }

    public ProgramTree program(List<Statement> statements, boolean strict) {
        return new ProgramTree(statements, strict);
    }

    public ThisExpression thisExpression(Position position) {
        return new ThisExpression(position);
    }

    public IdentifierReferenceExpression identifier(Position position, String identifier) {
        return new IdentifierReferenceExpression(position, identifier);
    }

    public StringLiteralExpression stringLiteral(Position position, String literal, boolean escaped, boolean continuedLine) {
        StringLiteralExpression expr = new StringLiteralExpression(position, literal);
        expr.setEscaped(escaped);
        expr.setContinuedLine(continuedLine);
        return expr;
    }

    public NumberLiteralExpression decimalLiteral(Position position, String text) {
        if (text.indexOf('.') == 0) {
            text = "0" + text;
            return new FloatingNumberExpression(position, text, 10, Double.valueOf(text));
        }

        if (text.indexOf('.') > 0) {
            double primaryValue = Double.valueOf(text);
            if (primaryValue == (long) primaryValue) {
                return new IntegerNumberExpression(position, text, 10, (long) primaryValue);
            } else {
                return new FloatingNumberExpression(position, text, 10, primaryValue);
            }
        }

        int eLoc = text.toLowerCase().indexOf('e');
        if (eLoc > 0) {

            String base = text.substring(0, eLoc);
            String exponent = text.substring(eLoc);

            String javafied = base + ".0" + exponent;

            return new FloatingNumberExpression(position, text, 10, Double.valueOf(javafied));
        } else {
            return new IntegerNumberExpression(position, text, 10, Long.valueOf(text));
        }
    }

    public NumberLiteralExpression hexLiteral(Position position, String text) {
        String javafied = text;
        if (javafied.startsWith("0x") || javafied.startsWith("0X")) {
            javafied = javafied.substring(2);
        }
        return new IntegerNumberExpression(position, text, 16, new BigInteger(javafied, 16).longValue());
    }

    public NumberLiteralExpression octalLiteral(Position position, String text) {
        return new IntegerNumberExpression(position, text, 16, new BigInteger(text, 8).longValue());
    }

    public RegexpLiteralExpression regexpLiteral(Position position, String text) {
        return new RegexpLiteralExpression(position, text);
    }

    public BooleanLiteralExpression trueLiteral(Position position) {
        return new BooleanLiteralExpression(position, true);
    }

    public BooleanLiteralExpression falseLiteral(Position position) {
        return new BooleanLiteralExpression(position, false);
    }

    public NullLiteralExpression nullLiteral(Position position) {
        return new NullLiteralExpression(position);
    }

    public ArrayLiteralExpression arrayLiteral(Position position, List<Expression> exprs) {
        return new ArrayLiteralExpression(position, exprs);
    }

    public ObjectLiteralExpression objectLiteral(Position position, List<PropertyAssignment> propAssignments) {
        return new ObjectLiteralExpression(position, propAssignments);
    }

    public FunctionCallExpression functionCall(Expression lhs, List<Expression> args) {
        return new FunctionCallExpression(lhs, args);
    }

    public Expression commaOperator(Expression lhs, Expression rhs) {
        return new CommaOperator(lhs, rhs);
    }

    public Expression assignmentOperator(Expression lhs, Expression rhs) {
        return new AssignmentExpression(lhs, rhs);
    }

    public Expression multiplicationAssignmentOperator(Expression lhs, Expression rhs) {
        return new CompoundAssignmentExpression(multiplicationOperator(lhs, rhs));
    }

    public MultiplicativeExpression multiplicationOperator(Expression lhs, Expression rhs) {
        return new MultiplicativeExpression(lhs, rhs, "*");
    }

    public Expression divisionAssignmentOperator(Expression lhs, Expression rhs) {
        return new CompoundAssignmentExpression(divisionOperator(lhs, rhs));
    }

    public MultiplicativeExpression divisionOperator(Expression lhs, Expression rhs) {
        return new MultiplicativeExpression(lhs, rhs, "/");
    }

    public Expression moduloAssignmentOperator(Expression lhs, Expression rhs) {
        return new CompoundAssignmentExpression(moduloOperator(lhs, rhs));
    }

    public MultiplicativeExpression moduloOperator(Expression lhs, Expression rhs) {
        return new MultiplicativeExpression(lhs, rhs, "%");
    }

    public Expression additionAssignmentOperator(Expression lhs, Expression rhs) {
        return new CompoundAssignmentExpression(additionOperator(lhs, rhs));
    }

    public AdditiveExpression additionOperator(Expression lhs, Expression rhs) {
        return new AdditiveExpression(lhs, rhs, "+");
    }

    public Expression subtractionAssignmentOperator(Expression lhs, Expression rhs) {
        return new CompoundAssignmentExpression(subtractionOperator(lhs, rhs));
    }

    public AdditiveExpression subtractionOperator(Expression lhs, Expression rhs) {
        return new AdditiveExpression(lhs, rhs, "-");
    }

    public Expression leftShiftAssignmentOperator(Expression lhs, Expression rhs) {
        return new CompoundAssignmentExpression(leftShiftOperator(lhs, rhs));
    }

    public BitwiseExpression leftShiftOperator(Expression lhs, Expression rhs) {
        return new BitwiseExpression(lhs, rhs, "<<");
    }

    public Expression rightShiftAssignmentOperator(Expression lhs, Expression rhs) {
        return new CompoundAssignmentExpression(rightShiftOperator(lhs, rhs));
    }

    public BitwiseExpression rightShiftOperator(Expression lhs, Expression rhs) {
        return new BitwiseExpression(lhs, rhs, ">>");
    }

    public Expression unsignedRightShiftAssignmentOperator(Expression lhs, Expression rhs) {
        return new CompoundAssignmentExpression(unsignedRightShiftOperator(lhs, rhs));
    }

    public BitwiseExpression unsignedRightShiftOperator(Expression lhs, Expression rhs) {
        return new BitwiseExpression(lhs, rhs, ">>>");
    }

    public Expression bitwiseAndAssignmentOperator(Expression lhs, Expression rhs) {
        return new CompoundAssignmentExpression(bitwiseAndOperator(lhs, rhs));
    }

    public BitwiseExpression bitwiseAndOperator(Expression lhs, Expression rhs) {
        return new BitwiseExpression(lhs, rhs, "&");
    }

    public Expression bitwiseOrAssignmentOperator(Expression lhs, Expression rhs) {
        return new CompoundAssignmentExpression(bitwiseOrOperator(lhs, rhs));
    }

    public BitwiseExpression bitwiseOrOperator(Expression lhs, Expression rhs) {
        return new BitwiseExpression(lhs, rhs, "|");
    }

    public Expression bitwiseXorAssignmentOperator(Expression lhs, Expression rhs) {
        return new CompoundAssignmentExpression(bitwiseXorOperator(lhs, rhs));
    }

    public BitwiseExpression bitwiseXorOperator(Expression lhs, Expression rhs) {
        return new BitwiseExpression(lhs, rhs, "^");
    }

    public TernaryExpression ternaryOperator(Expression testExpr, Expression thenExpr, Expression elseExpr) {
        return new TernaryExpression(testExpr, thenExpr, elseExpr);
    }

    public Expression logicalOrOperator(Expression lhs, Expression rhs) {
        return new LogicalExpression(lhs, rhs, "||");
    }

    public Expression logicalAndOperator(Expression lhs, Expression rhs) {
        return new LogicalExpression(lhs, rhs, "&&");
    }

    public Expression equalityOperator(Expression lhs, Expression rhs) {
        return new EqualityOperatorExpression(lhs, rhs, "==");
    }

    public Expression notEqualityOperator(Expression lhs, Expression rhs) {
        return new EqualityOperatorExpression(lhs, rhs, "!=");
    }

    public Expression strictEqualityOperator(Expression lhs, Expression rhs) {
        return new StrictEqualityOperatorExpression(lhs, rhs, "===");
    }

    public Expression strictNotEqualityOperator(Expression lhs, Expression rhs) {
        return new StrictEqualityOperatorExpression(lhs, rhs, "!==");
    }

    public Expression lessThanOperator(Expression lhs, Expression rhs) {
        return new RelationalExpression(lhs, rhs, "<");
    }

    public Expression lessThanEqualOperator(Expression lhs, Expression rhs) {
        return new RelationalExpression(lhs, rhs, "<=");
    }

    public Expression greaterThanOperator(Expression lhs, Expression rhs) {
        return new RelationalExpression(lhs, rhs, ">");
    }

    public Expression greaterThanEqualOperator(Expression lhs, Expression rhs) {
        return new RelationalExpression(lhs, rhs, ">=");
    }

    public Expression instanceofOperator(Expression lhs, Expression rhs) {
        return new InstanceofExpression(lhs, rhs);
    }

    public Expression inOperator(Expression lhs, Expression rhs) {
        return new InOperatorExpression(lhs, rhs);
    }

    public Expression deleteOperator(Expression expr) {
        return new DeleteOpExpression(expr);
    }

    public Expression voidOperator(Expression expr) {
        return new VoidOperatorExpression(expr);
    }

    public Expression typeofOperator(Expression expr) {
        return new TypeOfOpExpression(expr);
    }

    public Expression preIncrementOperator(Expression expr) {
        return new PreOpExpression(expr, "++");
    }

    public Expression preDecrementOperator(Expression expr) {
        return new PreOpExpression(expr, "--");
    }

    public Expression postIncrementOperator(Expression expr) {
        return new PostOpExpression(expr, "++");
    }

    public Expression postDecrementOperator(Expression expr) {
        return new PostOpExpression(expr, "--");
    }

    public Expression unaryPlusOperator(Expression expr) {
        return new UnaryPlusExpression(expr);
    }

    public Expression unaryMinusOperator(Expression expr) {
        return new UnaryMinusExpression(expr);
    }

    public Expression unaryNotOperator(Expression expr) {
        return new LogicalNotOperatorExpression(expr);
    }

    public Expression bitwiseInversionOperator(Expression expr) {
        return new BitwiseInversionOperatorExpression(expr);
    }

    public NewOperatorExpression newOperator(Expression expr) {
        return new NewOperatorExpression(expr);
    }

    public NewOperatorExpression newOperator(Expression expr, List<Expression> argExprs) {
        return new NewOperatorExpression(expr, argExprs);
    }

    public FunctionExpression functionExpression(Position position, String identifier, List<Parameter> params, BlockStatement body, boolean strict) {
        FunctionDescriptor descriptor = new FunctionDescriptor(position, identifier, params, body, strict);
        return new FunctionExpression(descriptor);
    }

    public DotExpression dotOperator(Expression lhs, String identifier) {
        return new DotExpression(lhs, identifier);
    }

    public BracketExpression bracketOperator(Expression lhs, Expression rhs) {
        return new BracketExpression(lhs, rhs);
    }

    // ----------------------------------------------------------------------
    // ----------------------------------------------------------------------

    public FunctionDeclaration functionDeclaration(Position position, String identifier, List<Parameter> params, BlockStatement body, boolean strict) {
        FunctionDescriptor descriptor = new FunctionDescriptor(position, identifier, params, body, strict);
        return new FunctionDeclaration(descriptor);
    }

    public VariableStatement variableStatement(Position position, List<VariableDeclaration> decls) {
        return new VariableStatement(position, decls);
    }

    public VariableDeclaration variableDeclaration(Position position, String identifier, Expression initializer) {
        return new VariableDeclaration(position, identifier, initializer);
    }

    public BlockStatement block(List<Statement> statements) {
        return new BlockStatement(statements);
    }

    public ContinueStatement continueStatement(Position position) {
        return continueStatement(position, null);
    }

    public ContinueStatement continueStatement(Position position, String target) {
        return new ContinueStatement(position, target);
    }

    public BreakStatement breakStatement(Position position) {
        return breakStatement(position, null);
    }

    public BreakStatement breakStatement(Position position, String target) {
        return new BreakStatement(position, target);
    }

    public ReturnStatement returnStatement(Position position) {
        return returnStatement(position, null);
    }

    public ReturnStatement returnStatement(Position position, Expression expr) {
        return new ReturnStatement(position, expr);
    }

    public ThrowStatement throwStatement(Position position, Expression expr) {
        return new ThrowStatement(position, expr);
    }

    public EmptyStatement emptyStatement(Position position) {
        return new EmptyStatement(position);
    }

    public ExpressionStatement expressionStatement(Expression expr) {
        return new ExpressionStatement(expr);
    }

    public IfStatement ifStatement(Position position, Expression testExpr, Statement thenStatement) {
        return ifStatement(position, testExpr, thenStatement, null);
    }

    public IfStatement ifStatement(Position position, Expression testExpr, Statement thenStatement, Statement elseStatement) {
        return new IfStatement(position, testExpr, thenStatement, elseStatement);
    }

    public SwitchStatement switchStatement(Position position, Expression expr, List<CaseClause> clauses) {
        return new SwitchStatement(position, expr, clauses);
    }

    public CaseClause caseClause(Position position, Expression expr, List<Statement> body) {
        return new CaseClause(position, expr, block(body));
    }

    public DefaultCaseClause defaultClause(Position position, List<Statement> body) {
        return new DefaultCaseClause(position, block(body));
    }

    public TryStatement tryStatement(Position position, BlockStatement tryBlock, CatchClause catchClause, BlockStatement finallyBlock) {
        return new TryStatement(position, tryBlock, catchClause, finallyBlock);
    }

    public CatchClause catchClause(Position position, String identifier, BlockStatement block) {
        return new CatchClause(position, identifier, block);
    }

    public DebuggerStatement debuggerStatement(Position position) {
        return new DebuggerStatement(position);
    }

    public ForExprStatement forStatement(Position position, Expression initExpr, Expression testExpr, Expression incrExpr, Statement body) {
        return new ForExprStatement(position, initExpr, testExpr, incrExpr, body);
    }

    public ForVarDeclStatement forStatement(Position position, List<VariableDeclaration> declList, Expression testExpr, Expression incrExpr, Statement body) {
        return new ForVarDeclStatement(position, declList, testExpr, incrExpr, body);
    }

    public ForExprInStatement forInStatement(Position position, Expression lhs, Expression rhs, Statement body) {
        return new ForExprInStatement(position, lhs, rhs, body);
    }

    public ForVarDeclInStatement forInStatement(Position position, VariableDeclaration decl, Expression rhs, Statement body) {
        return new ForVarDeclInStatement(position, decl, rhs, body);
    }

    public DoWhileStatement doWhileStatement(Position position, Statement body, Expression expr) {
        return new DoWhileStatement(position, body, expr);
    }

    public WhileStatement whileStatement(Position position, Expression expr, Statement body) {
        return new WhileStatement(position, expr, body);
    }

    // ----------------------------------------------------------------------
    // ----------------------------------------------------------------------

    public Parameter parameter(Position position, String identifier) {
        return new Parameter(position, identifier);
    }

    public WithStatement withStatement(Position position, Expression expr, Statement body) {
        return new WithStatement(position, expr, body);
    }

}
