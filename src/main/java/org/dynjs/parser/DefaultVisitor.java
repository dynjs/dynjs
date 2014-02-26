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
import org.dynjs.parser.ast.NumberLiteralExpression;
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

    protected void walkBinaryExpression(ExecutionContext context, AbstractBinaryExpression expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        expr.getRhs().accept(context, this, strict);
    }

    protected void walkUnaryExpression(ExecutionContext context, AbstractUnaryOperatorExpression expr, boolean strict) {
        expr.getExpr().accept(context, this, strict);

    }

    @Override
    public void visit(ExecutionContext context, AdditiveExpression expr, boolean strict) {
        walkBinaryExpression(context, expr, strict);
    }

    @Override
    public void visit(ExecutionContext context, BitwiseExpression expr, boolean strict) {
        walkBinaryExpression(context, expr, strict);
    }

    @Override
    public void visit(ExecutionContext context, ArrayLiteralExpression expr, boolean strict) {
        for (Expression each : expr.getExprs()) {
            each.accept(context, this, strict);
        }
    }

    @Override
    public void visit(ExecutionContext context, AssignmentExpression expr, boolean strict) {
        walkBinaryExpression(context, expr, strict);
    }

    @Override
    public void visit(ExecutionContext context, BitwiseInversionOperatorExpression expr, boolean strict) {
        walkUnaryExpression(context, expr, strict);
    }

    @Override
    public void visit(ExecutionContext context, BlockStatement statement, boolean strict) {
        for (Statement each : statement.getBlockContent()) {
            each.accept(context, this, strict);
        }
    }

    @Override
    public void visit(ExecutionContext context, BooleanLiteralExpression expr, boolean strict) {
        // no-op
    }

    @Override
    public void visit(ExecutionContext context, BreakStatement statement, boolean strict) {
        // no-op
    }

    @Override
    public void visit(ExecutionContext context, CaseClause clause, boolean strict) {
        clause.getExpression().accept(context, this, strict);
        clause.getBlock().accept(context, this, strict);
    }

    @Override
    public void visit(ExecutionContext context, DefaultCaseClause clause, boolean strict) {
        clause.getBlock().accept(context, this, strict);
    }

    @Override
    public void visit(ExecutionContext context, CatchClause clause, boolean strict) {
        clause.getBlock().accept(context, this, strict);
    }

    @Override
    public void visit(ExecutionContext context, CompoundAssignmentExpression expr, boolean strict) {
        expr.getRootExpr().accept(context, this, strict);
    }

    @Override
    public void visit(ExecutionContext context, ContinueStatement statement, boolean strict) {
        // no-op
    }

    @Override
    public void visit(ExecutionContext context, DeleteOpExpression expr, boolean strict) {
        walkUnaryExpression(context, expr, strict);
    }

    @Override
    public void visit(ExecutionContext context, DoWhileStatement statement, boolean strict) {
        statement.getTest().accept(context, this, strict);
        statement.getBlock().accept(context, this, strict);
    }

    @Override
    public void visit(ExecutionContext context, EmptyStatement statement, boolean strict) {
        // no-op
    }

    @Override
    public void visit(ExecutionContext context, EqualityOperatorExpression expr, boolean strict) {
        walkBinaryExpression(context, expr, strict);
    }

    @Override
    public void visit(ExecutionContext context, CommaOperator expr, boolean strict) {
        expr.getLhs().accept( context, this, strict );
        expr.getRhs().accept( context, this, strict );
    }

    @Override
    public void visit(ExecutionContext context, ExpressionStatement statement, boolean strict) {
        statement.getExpr().accept(context, this, strict);
    }

    @Override
    public void visit(ExecutionContext context, FloatingNumberExpression expr, boolean strict) {
        // no-op
    }

    @Override
    public void visit(ExecutionContext context, ForExprInStatement statement, boolean strict) {
        statement.getExpr().accept(context, this, strict);
        statement.getRhs().accept(context, this, strict);
        statement.getBlock().accept(context, this, strict);
    }

    @Override
    public void visit(ExecutionContext context, ForExprOfStatement statement, boolean strict) {
        statement.getExpr().accept(context, this, strict);
        statement.getRhs().accept(context, this, strict);
        statement.getBlock().accept(context, this, strict);
    }

    @Override
    public void visit(ExecutionContext context, ForExprStatement statement, boolean strict) {
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
    public void visit(ExecutionContext context, ForVarDeclInStatement statement, boolean strict) {
        statement.getDeclaration().accept(context, this, strict);
        statement.getRhs().accept(context, this, strict);
    }

    @Override
    public void visit(ExecutionContext context, ForVarDeclOfStatement statement, boolean strict) {
        statement.getDeclaration().accept(context, this, strict);
        statement.getRhs().accept(context, this, strict);
    }

    @Override
    public void visit(ExecutionContext context, ForVarDeclStatement statement, boolean strict) {
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
    public void visit(ExecutionContext context, FunctionCallExpression expr, boolean strict) {
        expr.getMemberExpression().accept(context, this, strict);

        for (Expression each : expr.getArgumentExpressions()) {
            each.accept(context, this, strict);
        }
    }

    @Override
    public void visit(ExecutionContext context, FunctionDeclaration statement, boolean strict) {
        statement.getBlock().accept(context, this, strict);
    }

    @Override
    public void visit(ExecutionContext context, FunctionExpression expr, boolean strict) {
        expr.getDescriptor().getBlock().accept(context, this, strict);
    }

    @Override
    public void visit(ExecutionContext context, IdentifierReferenceExpression expr, boolean strict) {
        // no-op
    }

    @Override
    public void visit(ExecutionContext context, IfStatement statement, boolean strict) {
        statement.getTest().accept(context, this, strict);
        statement.getThenBlock().accept(context, this, strict);
        if (statement.getElseBlock() != null) {
            statement.getElseBlock().accept(context, this, strict);
        }
    }

    @Override
    public void visit(ExecutionContext context, InOperatorExpression expr, boolean strict) {
        walkBinaryExpression(context, expr, strict);
    }

    @Override
    public void visit(ExecutionContext context, OfOperatorExpression expr, boolean strict) {
        walkBinaryExpression(context, expr, strict);
    }

    @Override
    public void visit(ExecutionContext context, InstanceofExpression expr, boolean strict) {
        walkBinaryExpression(context, expr, strict);
    }

    @Override
    public void visit(ExecutionContext context, IntegerNumberExpression expr, boolean strict) {
        // no-op
    }

    @Override
    public void visit(ExecutionContext context, LogicalExpression expr, boolean strict) {
        walkBinaryExpression(context, expr, strict);
    }

    @Override
    public void visit(ExecutionContext context, LogicalNotOperatorExpression expr, boolean strict) {
        expr.getExpr().accept(context, this, strict);
    }

    @Override
    public void visit(ExecutionContext context, DotExpression expr, boolean strict) {
        expr.getLhs().accept( context, this, strict );
    }
    
    @Override
    public void visit(ExecutionContext context, BracketExpression expr, boolean strict) {
        expr.getLhs().accept( context, this, strict );
        expr.getRhs().accept( context, this, strict );
    }

    @Override
    public void visit(ExecutionContext context, MultiplicativeExpression expr, boolean strict) {
        walkBinaryExpression(context, expr, strict);
    }

    @Override
    public void visit(ExecutionContext context, NewOperatorExpression expr, boolean strict) {
        walkUnaryExpression(context, expr, strict);
    }

    @Override
    public void visit(ExecutionContext context, NullLiteralExpression expr, boolean strict) {
        // no-op
    }

    @Override
    public void visit(ExecutionContext context, ObjectLiteralExpression expr, boolean strict) {
        for (PropertyAssignment each : expr.getPropertyAssignments()) {
            each.accept(context, this, strict);
        }
    }

    @Override
    public void visit(ExecutionContext context, PostOpExpression expr, boolean strict) {
        walkUnaryExpression(context, expr, strict);
    }

    @Override
    public void visit(ExecutionContext context, PreOpExpression expr, boolean strict) {
        walkUnaryExpression(context, expr, strict);

    }

    @Override
    public void visit(ExecutionContext context, NamedValue namedValue, boolean strict) {
        namedValue.getExpr().accept(context, this, strict);
    }

    @Override
    public void visit(ExecutionContext context, PropertyGet propertyGet, boolean strict) {
        propertyGet.getBlock().accept(context, this, strict);
    }

    @Override
    public void visit(ExecutionContext context, PropertySet propertySet, boolean strict) {
        propertySet.getBlock().accept(context, this, strict);
    }

    @Override
    public void visit(ExecutionContext context, RegexpLiteralExpression expr, boolean strict) {
        // no-op
    }

    @Override
    public void visit(ExecutionContext context, RelationalExpression expr, boolean strict) {
        walkBinaryExpression(context, expr, strict);
    }

    @Override
    public void visit(ExecutionContext context, ReturnStatement statement, boolean strict) {
        if (statement.getExpr() != null) {
            statement.getExpr().accept(context, this, strict);
        }
    }

    @Override
    public void visit(ExecutionContext context, StrictEqualityOperatorExpression expr, boolean strict) {
        walkBinaryExpression(context, expr, strict);
    }

    @Override
    public void visit(ExecutionContext context, StringLiteralExpression expr, boolean strict) {
        // no-op
    }

    @Override
    public void visit(ExecutionContext context, SwitchStatement statement, boolean strict) {
        statement.getExpr().accept(context, this, strict);
        for (CaseClause each : statement.getCaseClauses()) {
            each.accept(context, this, strict);
        }
    }

    @Override
    public void visit(ExecutionContext context, TernaryExpression expr, boolean strict) {
        expr.getTest().accept(context, this, strict);
        expr.getThenExpr().accept(context, this, strict);
        expr.getElseExpr().accept(context, this, strict);
    }

    @Override
    public void visit(ExecutionContext context, ThisExpression expr, boolean strict) {
        // no-op
    }

    @Override
    public void visit(ExecutionContext context, ThrowStatement statement, boolean strict) {
        statement.getExpr().accept(context, this, strict);

    }

    @Override
    public void visit(ExecutionContext context, TryStatement statement, boolean strict) {
        statement.getTryBlock().accept(context, this, strict);
        if (statement.getCatchClause() != null) {
            statement.getCatchClause().accept(context, this, strict);
        }
        if (statement.getFinallyBlock() != null) {
            statement.getFinallyBlock().accept(context, this, strict);
        }

    }

    @Override
    public void visit(ExecutionContext context, TypeOfOpExpression expr, boolean strict) {
        walkUnaryExpression(context, expr, strict);
    }

    @Override
    public void visit(ExecutionContext context, UnaryMinusExpression expr, boolean strict) {
        walkUnaryExpression(context, expr, strict);
    }

    @Override
    public void visit(ExecutionContext context, UnaryPlusExpression expr, boolean strict) {
        walkUnaryExpression(context, expr, strict);
    }

    @Override
    public void visit(ExecutionContext context, VariableDeclaration expr, boolean strict) {
        if (expr.getExpr() != null) {
            expr.getExpr().accept(context, this, strict);
        }
    }

    @Override
    public void visit(ExecutionContext context, VariableStatement statement, boolean strict) {
        for (VariableDeclaration each : statement.getVariableDeclarations()) {
            each.accept(context, this, strict);
        }
    }

    @Override
    public void visit(ExecutionContext context, VoidOperatorExpression expr, boolean strict) {
        walkUnaryExpression(context, expr, strict);
    }

    @Override
    public void visit(ExecutionContext context, WhileStatement statement, boolean strict) {
        statement.getTest().accept(context, this, strict);
        statement.getBlock().accept(context, this, strict);
    }

    @Override
    public void visit(ExecutionContext context, WithStatement statement, boolean strict) {
        statement.getExpr().accept(context, this, strict);
        statement.getBlock().accept(context, this, strict);
    }

}
