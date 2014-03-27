package org.dynjs.parser;

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

public interface CodeVisitor {


    Object visit(Object context, AdditiveExpression expr, boolean strict);

    Object visit(Object context, BitwiseExpression bitwiseExpression, boolean strict);

    Object visit(Object context, ArrayLiteralExpression expr, boolean strict);

    Object visit(Object context, AssignmentExpression expr, boolean strict);

    Object visit(Object context, BitwiseInversionOperatorExpression expr, boolean strict);

    Object visit(Object context, BlockStatement statement, boolean strict);

    Object visit(Object context, BooleanLiteralExpression expr, boolean strict);

    Object visit(Object context, BreakStatement statement, boolean strict);

    Object visit(Object context, CaseClause clause, boolean strict);
    
    Object visit(Object context, DefaultCaseClause clause, boolean strict);

    Object visit(Object context, CatchClause clause, boolean strict);

    Object visit(Object context, CompoundAssignmentExpression expr, boolean strict);

    Object visit(Object context, ContinueStatement statement, boolean strict);

    Object visit(Object context, DeleteOpExpression expr, boolean strict);

    Object visit(Object context, DoWhileStatement statement, boolean strict);

    Object visit(Object context, EmptyStatement statement, boolean strict);

    Object visit(Object context, EqualityOperatorExpression expr, boolean strict);

    Object visit(Object context, CommaOperator expr, boolean strict);

    Object visit(Object context, ExpressionStatement statement, boolean strict);

    Object visit(Object context, FloatingNumberExpression expr, boolean strict);

    Object visit(Object context, ForExprInStatement statement, boolean strict);

    Object visit(Object context, ForExprOfStatement statement, boolean strict);

    Object visit(Object context, ForExprStatement statement, boolean strict);

    Object visit(Object context, ForVarDeclInStatement statement, boolean strict);

    Object visit(Object context, ForVarDeclOfStatement statement, boolean strict);

    Object visit(Object context, ForVarDeclStatement statement, boolean strict);

    Object visit(Object context, FunctionCallExpression expr, boolean strict);

    Object visit(Object context, FunctionDeclaration statement, boolean strict);

    Object visit(Object context, FunctionExpression expr, boolean strict);

    Object visit(Object context, IdentifierReferenceExpression expr, boolean strict);

    Object visit(Object context, IfStatement statement, boolean strict);

    Object visit(Object context, InOperatorExpression expr, boolean strict);

    Object visit(Object context, OfOperatorExpression expr, boolean strict);

    Object visit(Object context, InstanceofExpression expr, boolean strict);

    Object visit(Object context, IntegerNumberExpression expr, boolean strict);

    Object visit(Object context, LogicalExpression expr, boolean strict);

    Object visit(Object context, LogicalNotOperatorExpression expr, boolean strict);

    Object visit(Object context, DotExpression expr, boolean strict);
    
    Object visit(Object context, BracketExpression expr, boolean strict);

    Object visit(Object context, MultiplicativeExpression expr, boolean strict);

    Object visit(Object context, NewOperatorExpression expr, boolean strict);

    Object visit(Object context, NullLiteralExpression expr, boolean strict);

    Object visit(Object context, ObjectLiteralExpression expr, boolean strict);

    Object visit(Object context, PostOpExpression expr, boolean strict);

    Object visit(Object context, PreOpExpression expr, boolean strict);

    Object visit(Object context, PropertyGet propertyGet, boolean strict);

    Object visit(Object context, PropertySet propertySet, boolean strict);
    
    Object visit(Object context, NamedValue namedValue, boolean strict);

    Object visit(Object context, RegexpLiteralExpression expr, boolean strict);

    Object visit(Object context, RelationalExpression expr, boolean strict);

    Object visit(Object context, ReturnStatement statement, boolean strict);

    Object visit(Object context, StrictEqualityOperatorExpression expr, boolean strict);

    Object visit(Object context, StringLiteralExpression expr, boolean strict);

    Object visit(Object context, SwitchStatement statement, boolean strict);

    Object visit(Object context, TernaryExpression expr, boolean strict);

    Object visit(Object context, ThisExpression expr, boolean strict);

    Object visit(Object context, ThrowStatement statement, boolean strict);

    Object visit(Object context, TryStatement statement, boolean strict);

    Object visit(Object context, TypeOfOpExpression expr, boolean strict);

    Object visit(Object context, UnaryMinusExpression expr, boolean strict);

    Object visit(Object context, UnaryPlusExpression expr, boolean strict);

    Object visit(Object context, VariableDeclaration expr, boolean strict);

    Object visit(Object context, VariableStatement statement, boolean strict);

    Object visit(Object context, VoidOperatorExpression expr, boolean strict);

    Object visit(Object context, WhileStatement statement, boolean strict);

    Object visit(Object context, WithStatement statement, boolean strict);


}
