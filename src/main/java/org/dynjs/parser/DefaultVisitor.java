package org.dynjs.parser;

import org.dynjs.parser.ast.AbstractBinaryExpression;
import org.dynjs.parser.ast.AbstractUnaryOperatorExpression;
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
import org.dynjs.parser.ast.ForExprOfStatement;
import org.dynjs.parser.ast.ForExprStatement;
import org.dynjs.parser.ast.ForVarDeclInStatement;
import org.dynjs.parser.ast.ForVarDeclOfStatement;
import org.dynjs.parser.ast.ForVarDeclStatement;
import org.dynjs.parser.ast.FunctionCallExpression;
import org.dynjs.parser.ast.FunctionDeclaration;
import org.dynjs.parser.ast.FunctionExpression;
import org.dynjs.parser.ast.IdentifierReferenceExpression;
import org.dynjs.parser.ast.IfStatement;
import org.dynjs.parser.ast.InOperatorExpression;
import org.dynjs.parser.ast.OfOperatorExpression;
import org.dynjs.parser.ast.InstanceofExpression;
import org.dynjs.parser.ast.IntegerNumberExpression;
import org.dynjs.parser.ast.LogicalExpression;
import org.dynjs.parser.ast.LogicalNotOperatorExpression;
import org.dynjs.parser.ast.MultiplicativeExpression;
import org.dynjs.parser.ast.NamedValue;
import org.dynjs.parser.ast.NewOperatorExpression;
import org.dynjs.parser.ast.NullLiteralExpression;
import org.dynjs.parser.ast.ObjectLiteralExpression;
import org.dynjs.parser.ast.PostOpExpression;
import org.dynjs.parser.ast.PreOpExpression;
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
import org.dynjs.parser.ast.VariableStatement;
import org.dynjs.parser.ast.VoidOperatorExpression;
import org.dynjs.parser.ast.WhileStatement;
import org.dynjs.parser.ast.WithStatement;
import org.dynjs.runtime.ExecutionContext;

public class DefaultVisitor implements CodeVisitor {

    protected void walkBinaryExpression(Object context, AbstractBinaryExpression expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        expr.getRhs().accept(context, this, strict);
    }

    protected void walkUnaryExpression(Object context, AbstractUnaryOperatorExpression expr, boolean strict) {
        expr.getExpr().accept(context, this, strict);

    }

    @Override
    public void visit(Object context, AdditiveExpression expr, boolean strict) {
        walkBinaryExpression(context, expr, strict);
    }

    @Override
    public void visit(Object context, BitwiseExpression expr, boolean strict) {
        walkBinaryExpression(context, expr, strict);
    }

    @Override
    public void visit(Object context, ArrayLiteralExpression expr, boolean strict) {
        for (Expression each : expr.getExprs()) {
            each.accept(context, this, strict);
        }
    }

    @Override
    public void visit(Object context, AssignmentExpression expr, boolean strict) {
        walkBinaryExpression(context, expr, strict);
    }

    @Override
    public void visit(Object context, BitwiseInversionOperatorExpression expr, boolean strict) {
        walkUnaryExpression(context, expr, strict);
    }

    @Override
    public void visit(Object context, BlockStatement statement, boolean strict) {
        for (Statement each : statement.getBlockContent()) {
            each.accept(context, this, strict);
        }
    }

    @Override
    public void visit(Object context, BooleanLiteralExpression expr, boolean strict) {
        // no-op
    }

    @Override
    public void visit(Object context, BreakStatement statement, boolean strict) {
        // no-op
    }

    @Override
    public void visit(Object context, CaseClause clause, boolean strict) {
        clause.getExpression().accept(context, this, strict);
        clause.getBlock().accept(context, this, strict);
    }

    @Override
    public void visit(Object context, DefaultCaseClause clause, boolean strict) {
        clause.getBlock().accept(context, this, strict);
    }

    @Override
    public void visit(Object context, CatchClause clause, boolean strict) {
        clause.getBlock().accept(context, this, strict);
    }

    @Override
    public void visit(Object context, CompoundAssignmentExpression expr, boolean strict) {
        expr.getRootExpr().accept(context, this, strict);
    }

    @Override
    public void visit(Object context, ContinueStatement statement, boolean strict) {
        // no-op
    }

    @Override
    public void visit(Object context, DeleteOpExpression expr, boolean strict) {
        walkUnaryExpression(context, expr, strict);
    }

    @Override
    public void visit(Object context, DoWhileStatement statement, boolean strict) {
        statement.getTest().accept(context, this, strict);
        statement.getBlock().accept(context, this, strict);
    }

    @Override
    public void visit(Object context, EmptyStatement statement, boolean strict) {
        // no-op
    }

    @Override
    public void visit(Object context, EqualityOperatorExpression expr, boolean strict) {
        walkBinaryExpression(context, expr, strict);
    }

    @Override
    public void visit(Object context, CommaOperator expr, boolean strict) {
        expr.getLhs().accept( context, this, strict );
        expr.getRhs().accept( context, this, strict );
    }

    @Override
    public void visit(Object context, ExpressionStatement statement, boolean strict) {
        statement.getExpr().accept(context, this, strict);
    }

    @Override
    public void visit(Object context, FloatingNumberExpression expr, boolean strict) {
        // no-op
    }

    @Override
    public void visit(Object context, ForExprInStatement statement, boolean strict) {
        statement.getExpr().accept(context, this, strict);
        statement.getRhs().accept(context, this, strict);
        statement.getBlock().accept(context, this, strict);
    }

    @Override
    public void visit(Object context, ForExprOfStatement statement, boolean strict) {
        statement.getExpr().accept(context, this, strict);
        statement.getRhs().accept(context, this, strict);
        statement.getBlock().accept(context, this, strict);
    }

    @Override
    public void visit(Object context, ForExprStatement statement, boolean strict) {
        if (statement.getExpr() != null) {
            statement.getExpr().accept(context, this, strict);
        }

        if (statement.getTest() != null) {
            statement.getTest().accept(context, this, strict);
        }

        if (statement.getIncrement() != null) {
            statement.getIncrement().accept(context, this, strict);
        }

        statement.getBlock().accept(context, this, strict);
    }

    @Override
    public void visit(Object context, ForVarDeclInStatement statement, boolean strict) {
        statement.getDeclaration().accept(context, this, strict);
        statement.getRhs().accept(context, this, strict);
    }

    @Override
    public void visit(Object context, ForVarDeclOfStatement statement, boolean strict) {
        statement.getDeclaration().accept(context, this, strict);
        statement.getRhs().accept(context, this, strict);
    }

    @Override
    public void visit(Object context, ForVarDeclStatement statement, boolean strict) {
        if (statement.getDeclarationList() != null) {
            for ( VariableDeclaration each : statement.getDeclarationList() ) {
                each.accept(context, this, strict);
            }
        }

        if (statement.getTest() != null) {
            statement.getTest().accept(context, this, strict);
        }

        if (statement.getIncrement() != null) {
            statement.getIncrement().accept(context, this, strict);
        }

        statement.getBlock().accept(context, this, strict);
    }

    @Override
    public void visit(Object context, FunctionCallExpression expr, boolean strict) {
        expr.getMemberExpression().accept(context, this, strict);

        for (Expression each : expr.getArgumentExpressions()) {
            each.accept(context, this, strict);
        }
    }

    @Override
    public void visit(Object context, FunctionDeclaration statement, boolean strict) {
        statement.getBlock().accept(context, this, strict);
    }

    @Override
    public void visit(Object context, FunctionExpression expr, boolean strict) {
        expr.getDescriptor().getBlock().accept(context, this, strict);
    }

    @Override
    public void visit(Object context, IdentifierReferenceExpression expr, boolean strict) {
        // no-op
    }

    @Override
    public void visit(Object context, IfStatement statement, boolean strict) {
        statement.getTest().accept(context, this, strict);
        statement.getThenBlock().accept(context, this, strict);
        if (statement.getElseBlock() != null) {
            statement.getElseBlock().accept(context, this, strict);
        }
    }

    @Override
    public void visit(Object context, InOperatorExpression expr, boolean strict) {
        walkBinaryExpression(context, expr, strict);
    }

    @Override
    public void visit(Object context, OfOperatorExpression expr, boolean strict) {
        walkBinaryExpression(context, expr, strict);
    }

    @Override
    public void visit(Object context, InstanceofExpression expr, boolean strict) {
        walkBinaryExpression(context, expr, strict);
    }

    @Override
    public void visit(Object context, IntegerNumberExpression expr, boolean strict) {
        // no-op
    }

    @Override
    public void visit(Object context, LogicalExpression expr, boolean strict) {
        walkBinaryExpression(context, expr, strict);
    }

    @Override
    public void visit(Object context, LogicalNotOperatorExpression expr, boolean strict) {
        expr.getExpr().accept(context, this, strict);
    }

    @Override
    public void visit(Object context, DotExpression expr, boolean strict) {
        expr.getLhs().accept( context, this, strict );
    }
    
    @Override
    public void visit(Object context, BracketExpression expr, boolean strict) {
        expr.getLhs().accept( context, this, strict );
        expr.getRhs().accept( context, this, strict );
    }

    @Override
    public void visit(Object context, MultiplicativeExpression expr, boolean strict) {
        walkBinaryExpression(context, expr, strict);
    }

    @Override
    public void visit(Object context, NewOperatorExpression expr, boolean strict) {
        walkUnaryExpression(context, expr, strict);
    }

    @Override
    public void visit(Object context, NullLiteralExpression expr, boolean strict) {
        // no-op
    }

    @Override
    public void visit(Object context, ObjectLiteralExpression expr, boolean strict) {
        for (PropertyAssignment each : expr.getPropertyAssignments()) {
            each.accept(context, this, strict);
        }
    }

    @Override
    public void visit(Object context, PostOpExpression expr, boolean strict) {
        walkUnaryExpression(context, expr, strict);
    }

    @Override
    public void visit(Object context, PreOpExpression expr, boolean strict) {
        walkUnaryExpression(context, expr, strict);

    }

    @Override
    public void visit(Object context, NamedValue namedValue, boolean strict) {
        namedValue.getExpr().accept(context, this, strict);
    }

    @Override
    public void visit(Object context, PropertyGet propertyGet, boolean strict) {
        propertyGet.getBlock().accept(context, this, strict);
    }

    @Override
    public void visit(Object context, PropertySet propertySet, boolean strict) {
        propertySet.getBlock().accept(context, this, strict);
    }

    @Override
    public void visit(Object context, RegexpLiteralExpression expr, boolean strict) {
        // no-op
    }

    @Override
    public void visit(Object context, RelationalExpression expr, boolean strict) {
        walkBinaryExpression(context, expr, strict);
    }

    @Override
    public void visit(Object context, ReturnStatement statement, boolean strict) {
        if (statement.getExpr() != null) {
            statement.getExpr().accept(context, this, strict);
        }
    }

    @Override
    public void visit(Object context, StrictEqualityOperatorExpression expr, boolean strict) {
        walkBinaryExpression(context, expr, strict);
    }

    @Override
    public void visit(Object context, StringLiteralExpression expr, boolean strict) {
        // no-op
    }

    @Override
    public void visit(Object context, SwitchStatement statement, boolean strict) {
        statement.getExpr().accept(context, this, strict);
        for (CaseClause each : statement.getCaseClauses()) {
            each.accept(context, this, strict);
        }
    }

    @Override
    public void visit(Object context, TernaryExpression expr, boolean strict) {
        expr.getTest().accept(context, this, strict);
        expr.getThenExpr().accept(context, this, strict);
        expr.getElseExpr().accept(context, this, strict);
    }

    @Override
    public void visit(Object context, ThisExpression expr, boolean strict) {
        // no-op
    }

    @Override
    public void visit(Object context, ThrowStatement statement, boolean strict) {
        statement.getExpr().accept(context, this, strict);

    }

    @Override
    public void visit(Object context, TryStatement statement, boolean strict) {
        statement.getTryBlock().accept(context, this, strict);
        if (statement.getCatchClause() != null) {
            statement.getCatchClause().accept(context, this, strict);
        }
        if (statement.getFinallyBlock() != null) {
            statement.getFinallyBlock().accept(context, this, strict);
        }

    }

    @Override
    public void visit(Object context, TypeOfOpExpression expr, boolean strict) {
        walkUnaryExpression(context, expr, strict);
    }

    @Override
    public void visit(Object context, UnaryMinusExpression expr, boolean strict) {
        walkUnaryExpression(context, expr, strict);
    }

    @Override
    public void visit(Object context, UnaryPlusExpression expr, boolean strict) {
        walkUnaryExpression(context, expr, strict);
    }

    @Override
    public void visit(Object context, VariableDeclaration expr, boolean strict) {
        if (expr.getExpr() != null) {
            expr.getExpr().accept(context, this, strict);
        }
    }

    @Override
    public void visit(Object context, VariableStatement statement, boolean strict) {
        for (VariableDeclaration each : statement.getVariableDeclarations()) {
            each.accept(context, this, strict);
        }
    }

    @Override
    public void visit(Object context, VoidOperatorExpression expr, boolean strict) {
        walkUnaryExpression(context, expr, strict);
    }

    @Override
    public void visit(Object context, WhileStatement statement, boolean strict) {
        statement.getTest().accept(context, this, strict);
        statement.getBlock().accept(context, this, strict);
    }

    @Override
    public void visit(Object context, WithStatement statement, boolean strict) {
        statement.getExpr().accept(context, this, strict);
        statement.getBlock().accept(context, this, strict);
    }

}
