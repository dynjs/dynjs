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

import org.dynjs.Config;
import org.dynjs.ir.instructions.Add;
import org.dynjs.ir.instructions.BEQ;
import org.dynjs.ir.instructions.Call;
import org.dynjs.ir.instructions.Copy;
import org.dynjs.ir.instructions.DefineFunction;
import org.dynjs.ir.instructions.Jump;
import org.dynjs.ir.instructions.LE;
import org.dynjs.ir.instructions.LT;
import org.dynjs.ir.instructions.LabelInstr;
import org.dynjs.ir.instructions.Mul;
import org.dynjs.ir.instructions.PropertyLookup;
import org.dynjs.ir.instructions.ReceiveFunctionParameter;
import org.dynjs.ir.instructions.Return;
import org.dynjs.ir.instructions.Sub;
import org.dynjs.ir.operands.BooleanLiteral;
import org.dynjs.ir.operands.DynamicVariable;
import org.dynjs.ir.operands.FloatNumber;
import org.dynjs.ir.operands.Fn;
import org.dynjs.ir.operands.IntegerNumber;
import org.dynjs.ir.operands.Label;
import org.dynjs.ir.operands.Null;
import org.dynjs.ir.operands.StringLiteral;
import org.dynjs.ir.operands.TemporaryVariable;
import org.dynjs.ir.operands.This;
import org.dynjs.ir.operands.Undefined;
import org.dynjs.ir.operands.Variable;
import org.dynjs.parser.CodeVisitor;
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

import java.util.List;

public class Builder implements CodeVisitor {
    private static Builder BUILDER = new Builder();

    public static JSProgram compile(ProgramTree program, Config.CompileMode mode) {
        Scope scope = new Scope(null, program.getPosition().getFileName(), program.isStrict());
        program.accept(scope, BUILDER, program.isStrict());

        // FIXME: Add processing stage here/somewhere to do instr process/cfg/passes.

        if (mode == Config.CompileMode.IRC) {
            return new IRByteCodeCompiler(scope, program.getPosition().getFileName(), program.isStrict()).compile();
        }
        return new IRJSProgram(scope);
    }

    @Override
    public Object visit(Object context, AdditiveExpression expr, boolean strict) {
        Scope scope = (Scope) context;
        Operand lhs = (Operand) expr.getLhs().accept(context, this, strict);
        Operand rhs = (Operand) expr.getRhs().accept(context, this, strict);
        boolean subtract = expr.getOp().equals("-");
        Operand value;

        // FIXME: Review numeric representation of JS to figure out overflow etc..
        if (lhs instanceof IntegerNumber) {
            if (rhs instanceof IntegerNumber) {
                if (subtract) {
                    value = new IntegerNumber(((IntegerNumber) lhs).getValue() - ((IntegerNumber) rhs).getValue());
                } else {
                    value = new IntegerNumber(((IntegerNumber) lhs).getValue() + ((IntegerNumber) rhs).getValue());
                }
            } else if (rhs instanceof FloatNumber) {
                if (subtract) {
                    value = new FloatNumber(((FloatNumber) rhs).getValue() - ((IntegerNumber) lhs).getValue());
                } else {
                    value = new FloatNumber(((FloatNumber) rhs).getValue() + ((IntegerNumber) lhs).getValue());
                }
            } else {
                Variable tmp = scope.createTemporaryVariable();

                if (subtract) {
                    scope.addInstruction(new Sub(tmp, lhs, rhs));
                } else {
                    scope.addInstruction(new Add(tmp, lhs, rhs));
                }

                value = tmp;
            }
        } else {
            Variable tmp = scope.createTemporaryVariable();

            if (subtract) {
                scope.addInstruction(new Sub(tmp, lhs, rhs));
            } else {
                scope.addInstruction(new Add(tmp, lhs, rhs));
            }

            value = tmp;
        }

        return value;
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
    public Object visit(Object context, BlockStatement block, boolean strict) {
        Scope scope = (Scope) context;

        // FIXME: How can we use what the parser provides to good effect?
        Operand value = Undefined.UNDEFINED;
        for (Statement statement: block.getBlockContent()) {
            value = (Operand) statement.accept(context, this, strict);
        }

        return value;
    }

    @Override
    public Object visit(Object context, BooleanLiteralExpression expr, boolean strict) {
        return expr.getValue() ? BooleanLiteral.TRUE : BooleanLiteral.FALSE;
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
        Scope scope = (Scope) context;

        // FIXME: If name of lhs is 'eval' or 'assignments' then generate a raise error instance of doing copy.
        // This is a little out of order with basicinterp where we check this before eval'ing value below.

        // Of s += 1 this is (s + 1)
        Operand value = (Operand) expr.getRootExpr().accept(context, this, strict);
        Variable lhs = (Variable) expr.getRootExpr().getLhs().accept(context, this, strict);

        scope.addInstruction(new Copy(lhs, value));

        return value;
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
        Scope scope = (Scope) context;
        final Label startLabel = scope.getNewLabel();
        final Label doneLabel = scope.getNewLabel();

        scope.addInstruction(new LabelInstr(startLabel));

        // BODY
        statement.getBlock().accept(context, this, strict);

        // TEST
        scope.addInstruction(new BEQ((Operand) statement.getTest().accept(context, this, strict),
                BooleanLiteral.FALSE, doneLabel));
        scope.addInstruction(new Jump(startLabel));

        // END
        scope.addInstruction(new LabelInstr(doneLabel));

        return Undefined.UNDEFINED;
    }

    @Override
    public Object visit(Object context, EmptyStatement statement, boolean strict) {
        return Undefined.UNDEFINED;
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
        return acceptOrUndefined(context, statement.getExpr(), strict);
    }

    @Override
    public Object visit(Object context, FloatingNumberExpression expr, boolean strict) {
        return new FloatNumber(expr.getValue());
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

    // FIXME: flow control may or may not be an issue but old runtime handles it explicitly after accept.
    @Override
    public Object visit(Object context, ForVarDeclStatement statement, boolean strict) {
        Scope scope = (Scope) context;
        List<VariableDeclaration> decls = statement.getDeclarationList();
        for (VariableDeclaration each : decls) {
            each.accept(context, this, strict);
        }

        final Label startLabel = scope.getNewLabel();
        final Label doneLabel = scope.getNewLabel();

        scope.addInstruction(new LabelInstr(startLabel));
        // TEST
        scope.addInstruction(new BEQ((Operand) statement.getTest().accept(context, this, strict),
                BooleanLiteral.FALSE, doneLabel));

        // BODY
        statement.getBlock().accept(context, this, strict);

        // INCREMENT
        statement.getIncrement().accept(context, this, strict);

        scope.addInstruction(new Jump(startLabel));

        // END
        scope.addInstruction(new LabelInstr(doneLabel));

        return Undefined.UNDEFINED;
    }

    @Override
    public Object visit(Object context, FunctionCallExpression expr, boolean strict) {
        Scope scope = (Scope) context;
        Variable result = scope.createTemporaryVariable();
        List<Expression> argumentExpressions = expr.getArgumentExpressions();
        int argsLength = argumentExpressions.size();
        Operand[] args = new Operand[argsLength];

        for (int i = 0; i < argsLength; i++) {
            args[i] = (Operand) acceptOrUndefined(context, argumentExpressions.get(i), strict);
        }

        final Operand name = (Operand) acceptOrUndefined(context, expr.getMemberExpression(), strict);

        scope.addInstruction(new Call(result, This.THIS, name, args));

        return result;
    }

    @Override
    public Object visit(Object context, FunctionDeclaration statement, boolean strict) {
        return unimplemented(context, statement, strict);
    }

    @Override
    public Object visit(Object context, FunctionExpression expr, boolean strict) {
        Scope scope = (Scope) context;
        FunctionDescriptor descriptor = expr.getDescriptor();
        Variable result = scope.createTemporaryVariable();
        String[] parameterNames = descriptor.getFormalParameterNames();
        FunctionScope functionScope = new FunctionScope(scope, descriptor.getPosition().getFileName(),
                descriptor.isStrict(), parameterNames);

        // Recieve all declared parameters
        int paramsLength = parameterNames.length;
        for (int i = 0; i < paramsLength; i++) {
            functionScope.addInstruction(new ReceiveFunctionParameter(functionScope.acquireLocalVariable(parameterNames[i]), i));
        }

        descriptor.getBlock().accept(functionScope, this, strict);

        scope.addInstruction(new DefineFunction(result, functionScope));

        return result;
    }

    @Override
    public Object visit(Object context, IdentifierReferenceExpression expr, boolean strict) {
        Scope scope = (Scope) context;

        Variable variable = scope.findVariable(expr.getIdentifier());

        // FIXME: Should this error out in strict mode?
        if (variable == null) {
            return new DynamicVariable(expr.getIdentifier());
        }

        return variable;
    }

    @Override
    public Object visit(Object context, IfStatement ifNode, boolean strict) {
        Scope scope = (Scope) context;
        Label elseLabel = scope.getNewLabel();
        Label doneLabel  = scope.getNewLabel();

        // IF
        scope.addInstruction(new BEQ((Operand) ifNode.getTest().accept(context, this, strict),
                BooleanLiteral.FALSE, elseLabel));

        // THEN
        ifNode.getThenBlock().accept(context, this, strict);
        scope.addInstruction(new Jump(doneLabel));

        // ELSE
        scope.addInstruction(new LabelInstr(elseLabel));
        if (ifNode.getElseBlock() != null) {
            ifNode.getElseBlock().accept(context, this, strict);
        }

        // END
        scope.addInstruction(new LabelInstr(doneLabel));

        return Undefined.UNDEFINED;
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
        return new IntegerNumber(expr.getValue());
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
        Scope scope = (Scope) context;
        Variable result = scope.createTemporaryVariable();
        Operand base = (Operand) expr.getLhs().accept(context, this, strict);

        scope.addInstruction(new PropertyLookup(result, base, expr.getIdentifier()));

        return result;
    }

    @Override
    public Object visit(Object context, BracketExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, MultiplicativeExpression expr, boolean strict) {
        Scope scope = (Scope) context;
        Operand lhs = (Operand) expr.getLhs().accept(context, this, strict);
        Operand rhs = (Operand) expr.getRhs().accept(context, this, strict);
        Operand value;

        // FIXME: Review numeric representation of JS to figure out overflow etc..
        if (lhs instanceof IntegerNumber) {
            if (rhs instanceof IntegerNumber) {
                value = new IntegerNumber(((IntegerNumber) lhs).getValue() * ((IntegerNumber) rhs).getValue());
            } else if (rhs instanceof FloatNumber) {
                value = new FloatNumber(((FloatNumber) rhs).getValue() * ((IntegerNumber) lhs).getValue());
            } else {
                Variable tmp = scope.createTemporaryVariable();

                scope.addInstruction(new Mul(tmp, lhs, rhs));

                value = tmp;
            }
        } else {
            Variable tmp = scope.createTemporaryVariable();

            scope.addInstruction(new Mul(tmp, lhs, rhs));

            value = tmp;
        }

        return value;
    }

    @Override
    public Object visit(Object context, NewOperatorExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, NullLiteralExpression expr, boolean strict) {
        return Null.NULL;
    }

    @Override
    public Object visit(Object context, ObjectLiteralExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, PostOpExpression expr, boolean strict) {
        Scope scope = (Scope) context;
        Variable tmp = scope.createTemporaryVariable();
        Variable variable = (Variable) expr.getExpr().accept(context, this, strict);
        boolean subtract = expr.getOp().equals("-");

        if (subtract) {
            scope.addInstruction(new Sub(tmp, variable, new IntegerNumber(1)));
        } else {
            scope.addInstruction(new Add(tmp, variable, new IntegerNumber(1)));
        }
        scope.addInstruction(new Copy(variable, tmp));

        return variable;
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
        Scope scope = (Scope) context;
        Variable result = scope.createTemporaryVariable();
        Operand lhsValue = (Operand) expr.getLhs().accept(context, this, strict);
        Operand rhsValue = (Operand) expr.getRhs().accept(context, this, strict);

        switch (expr.getOp()) {
            case "<":
                scope.addInstruction(new LT(result, lhsValue, rhsValue));
                break;
            case ">":
                scope.addInstruction(new LT(result, rhsValue, lhsValue));
                break;
            case "<=":
                scope.addInstruction(new LE(result, lhsValue, rhsValue));
                break;
            case ">=":
                scope.addInstruction(new LE(result, rhsValue, lhsValue));
                break;
        }

        return result;
    }

    @Override
    public Object visit(Object context, ReturnStatement statement, boolean strict) {
        Scope scope = (Scope) context;
        Operand returnValue = (Operand) acceptOrUndefined(context, statement.getExpr(), strict);

        scope.addInstruction(new Return(returnValue));

        return returnValue;
    }

    @Override
    public Object visit(Object context, StrictEqualityOperatorExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, StringLiteralExpression expr, boolean strict) {
        return new StringLiteral(expr.getLiteral());
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
        return This.THIS;
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
        Scope scope = (Scope) context;
        Variable variable = scope.acquireLocalVariable(expr.getIdentifier());
        Operand value = (Operand) acceptOrUndefined(context, expr.getExpr(), strict);

        scope.addInstruction(new Copy(variable, value));

        return value;
    }

    @Override
    public Object visit(Object context, VariableStatement statement, boolean strict) {
        for (VariableDeclaration decl: statement.getVariableDeclarations()) {
            decl.accept(context, this, strict);
        }

        return Undefined.UNDEFINED;
    }

    @Override
    public Object visit(Object context, VoidOperatorExpression expr, boolean strict) {
        return unimplemented(context, expr, strict);
    }

    @Override
    public Object visit(Object context, WhileStatement statement, boolean strict) {
        Scope scope = (Scope) context;
        final Label startLabel = scope.getNewLabel();
        final Label doneLabel = scope.getNewLabel();

        scope.addInstruction(new LabelInstr(startLabel));
        // TEST
        scope.addInstruction(new BEQ((Operand) statement.getTest().accept(context, this, strict),
                BooleanLiteral.FALSE, doneLabel));

        // BODY
        statement.getBlock().accept(context, this, strict);
        scope.addInstruction(new Jump(startLabel));

        // END
        scope.addInstruction(new LabelInstr(doneLabel));

        return Undefined.UNDEFINED;
    }

    @Override
    public Object visit(Object context, WithStatement statement, boolean strict) {
        return unimplemented(context, statement, strict);
    }

    private Object unimplemented(Object context, Object expr, boolean strict) {
        throw new RuntimeException("EXPR: '" + expr + "' is unimplemented.");
    }

    private Object acceptOrUndefined(Object context, Expression expr, boolean strict) {
        return expr != null ? expr.accept(context, this, strict) : Undefined.UNDEFINED;
    }

    private Variable copyAndReturnValue(Scope scope, Operand value) {
        Variable variable = scope.createTemporaryVariable();

        scope.addInstruction(new Copy(variable, value));

        return variable;
    }

    private Variable getValueInTemporaryVariable(Scope scope, Operand value) {
        if (value != null && value instanceof TemporaryVariable) return (Variable) value;

        return copyAndReturnValue(scope, value);
    }
}
