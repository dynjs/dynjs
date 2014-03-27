/*
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dynjs.ir;

import org.dynjs.parser.CodeVisitor;
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
import org.dynjs.parser.ast.InstanceofExpression;
import org.dynjs.parser.ast.IntegerNumberExpression;
import org.dynjs.parser.ast.LogicalExpression;
import org.dynjs.parser.ast.LogicalNotOperatorExpression;
import org.dynjs.parser.ast.MultiplicativeExpression;
import org.dynjs.parser.ast.NamedValue;
import org.dynjs.parser.ast.NewOperatorExpression;
import org.dynjs.parser.ast.NullLiteralExpression;
import org.dynjs.parser.ast.ObjectLiteralExpression;
import org.dynjs.parser.ast.OfOperatorExpression;
import org.dynjs.parser.ast.PostOpExpression;
import org.dynjs.parser.ast.PreOpExpression;
import org.dynjs.parser.ast.ProgramTree;
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
import org.dynjs.runtime.JSProgram;

public class Builder implements CodeVisitor {
    public static JSProgram compile(ProgramTree program) {
        // FIXME: builder will be stateless so only construct one ever not one per compile
        Builder builder = new Builder();
        program.accept(null, builder, program.isStrict());
        return null;  // FIXME: Make JSPRogram of result.
    }

    @Override
    public Object visit(Object context, AdditiveExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, BitwiseExpression bitwiseExpression, boolean strict) {
        return unimplemented(context, bitwiseExpression, strict);
    }

    @Override
    public Object visit(Object context, ArrayLiteralExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, AssignmentExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, BitwiseInversionOperatorExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, BlockStatement statement, boolean strict) {
        return unimplemented(context, statement, strict);
    }

    @Override
    public Object visit(Object context, BooleanLiteralExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, BreakStatement statement, boolean strict) {
        return unimplemented(context, statement, strict);
    }

    @Override
    public Object visit(Object context, CaseClause clause, boolean strict) {
        return unimplemented(context, clause, strict);
    }

    @Override
    public Object visit(Object context, DefaultCaseClause clause, boolean strict) {
        return unimplemented(context, clause, strict);
    }

    @Override
    public Object visit(Object context, CatchClause clause, boolean strict) {
        return unimplemented(context, clause, strict);
    }

    @Override
    public Object visit(Object context, CompoundAssignmentExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, ContinueStatement statement, boolean strict) {
        return unimplemented(context, statement, strict);
    }

    @Override
    public Object visit(Object context, DeleteOpExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, DoWhileStatement statement, boolean strict) {
        return unimplemented(context, statement, strict);
    }

    @Override
    public Object visit(Object context, EmptyStatement statement, boolean strict) {
        return unimplemented(context, statement, strict);
    }

    @Override
    public Object visit(Object context, EqualityOperatorExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, CommaOperator expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, ExpressionStatement statement, boolean strict) {
        return unimplemented(context, statement, strict);
    }

    @Override
    public Object visit(Object context, FloatingNumberExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, ForExprInStatement statement, boolean strict) {
        return unimplemented(context, statement, strict);
    }

    @Override
    public Object visit(Object context, ForExprOfStatement statement, boolean strict) {
        return unimplemented(context, statement, strict);
    }

    @Override
    public Object visit(Object context, ForExprStatement statement, boolean strict) {
        return unimplemented(context, statement, strict);
    }

    @Override
    public Object visit(Object context, ForVarDeclInStatement statement, boolean strict) {
        return unimplemented(context, statement, strict);
    }

    @Override
    public Object visit(Object context, ForVarDeclOfStatement statement, boolean strict) {
        return unimplemented(context, statement, strict);
    }

    @Override
    public Object visit(Object context, ForVarDeclStatement statement, boolean strict) {
        return unimplemented(context, statement, strict);
    }

    @Override
    public Object visit(Object context, FunctionCallExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, FunctionDeclaration statement, boolean strict) {
        return unimplemented(context, statement, strict);
    }

    @Override
    public Object visit(Object context, FunctionExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, IdentifierReferenceExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, IfStatement statement, boolean strict) {
        return unimplemented(context, statement, strict);
    }

    @Override
    public Object visit(Object context, InOperatorExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, OfOperatorExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, InstanceofExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, IntegerNumberExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, LogicalExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, LogicalNotOperatorExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, DotExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, BracketExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, MultiplicativeExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, NewOperatorExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, NullLiteralExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, ObjectLiteralExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, PostOpExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, PreOpExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, PropertyGet propertyGet, boolean strict) {
        return unimplemented(context, propertyGet, strict);
    }

    @Override
    public Object visit(Object context, PropertySet propertySet, boolean strict) {
        return unimplemented(context, propertySet, strict);
    }

    @Override
    public Object visit(Object context, NamedValue namedValue, boolean strict) {
        return unimplemented(context, namedValue, strict);
    }

    @Override
    public Object visit(Object context, RegexpLiteralExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, RelationalExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, ReturnStatement statement, boolean strict) {
        return unimplemented(context, statement, strict);
    }

    @Override
    public Object visit(Object context, StrictEqualityOperatorExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, StringLiteralExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, SwitchStatement statement, boolean strict) {
        return unimplemented(context, statement, strict);
    }

    @Override
    public Object visit(Object context, TernaryExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, ThisExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, ThrowStatement statement, boolean strict) {
        return unimplemented(context, statement, strict);
    }

    @Override
    public Object visit(Object context, TryStatement statement, boolean strict) {
        return unimplemented(context, statement, strict);
    }

    @Override
    public Object visit(Object context, TypeOfOpExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, UnaryMinusExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, UnaryPlusExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, VariableDeclaration expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, VariableStatement statement, boolean strict) {
        return unimplemented(context, statement, strict);
    }

    @Override
    public Object visit(Object context, VoidOperatorExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, WhileStatement statement, boolean strict) {
        return unimplemented(context, statement, strict);
    }

    @Override
    public Object visit(Object context, WithStatement statement, boolean strict) {
        return unimplemented(context, statement, strict);
    }

    private Object unimplemented(Object context, Object expr, boolean strict) {
        throw new RuntimeException("EXPR: '" + expr + "' is unimplemented.");
    }
}
