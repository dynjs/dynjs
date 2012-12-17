package org.dynjs.runtime.interp;

import java.util.ArrayList;
import java.util.List;

import org.dynjs.exception.ThrowException;
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
import org.dynjs.parser.ast.ForExprInStatement;
import org.dynjs.parser.ast.ForExprStatement;
import org.dynjs.parser.ast.ForVarDeclInStatement;
import org.dynjs.parser.ast.ForVarDeclStatement;
import org.dynjs.parser.ast.FunctionCallExpression;
import org.dynjs.parser.ast.FunctionDeclaration;
import org.dynjs.parser.ast.FunctionExpression;
import org.dynjs.parser.ast.IdentifierReferenceExpression;
import org.dynjs.parser.ast.IfStatement;
import org.dynjs.parser.ast.InOperatorExpression;
import org.dynjs.parser.ast.InstanceofExpression;
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
import org.dynjs.parser.ast.PrintStatement;
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
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.DynArray;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.EnvironmentRecord;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.BuiltinArray;
import org.dynjs.runtime.builtins.types.BuiltinObject;

public class InterpretingVisitor implements CodeVisitor {

    private List<Object> stack = new ArrayList<>();

    public InterpretingVisitor() {

    }

    public void push(Object value) {
        this.stack.add(value);
    }

    public Object pop() {
        return this.stack.remove(this.stack.size() - 1);
    }

    @Override
    public void visit(ExecutionContext context, AdditiveExpression expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        expr.getRhs().accept(context, this, strict);

        Object rhs = Types.toPrimitive(context, pop());
        Object lhs = Types.toPrimitive(context, pop());

        if (lhs instanceof String || rhs instanceof String) {
            push(Types.toString(context, lhs) + Types.toString(context, rhs));
            return;
        }

        Number lhsNum = Types.toNumber(context, lhs);
        Number rhsNum = Types.toNumber(context, rhs);

        if (lhs instanceof Double || rhs instanceof Double) {
            push(lhsNum.doubleValue() + rhsNum.doubleValue());
            return;
        }

        push(lhsNum.longValue() + rhsNum.longValue());
    }

    @Override
    public void visit(ExecutionContext context, BitwiseExpression expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        Object lhs = Types.getValue(context, pop());

        expr.getRhs().accept(context, this, strict);
        Long rhsNum = Types.toInt32(context, Types.getValue(context, pop()));

        Long lhsNum = null;

        if (expr.getOp().equals(">>>")) {
            lhsNum = Types.toUint32(context, lhs);
        } else {
            lhsNum = Types.toInt32(context, lhs);
        }

        if (expr.getOp().equals("<<")) {
            push(lhsNum.longValue() << rhsNum.intValue());
        } else if (expr.getOp().equals(">>")) {
            push(lhsNum.longValue() >> rhsNum.intValue());
        } else if (expr.getOp().equals(">>>")) {
            push(lhsNum.longValue() >>> rhsNum.intValue());
        } else if (expr.getOp().equals("&")) {
            push(lhsNum.longValue() & rhsNum.longValue());
        } else if (expr.getOp().equals("|")) {
            push(lhsNum.longValue() | rhsNum.longValue());
        } else if (expr.getOp().equals("^")) {
            push(lhsNum.longValue() ^ rhsNum.longValue());
        }
    }

    @Override
    public void visit(ExecutionContext context, ArrayLiteralExpression expr, boolean strict) {
        DynArray array = BuiltinArray.newArray(context);

        int i = 0;
        for (Expression each : expr.getExprs()) {
            each.accept(context, this, strict);
            array.defineOwnProperty(context, "" + i, PropertyDescriptor.newPropertyDescriptorForObjectInitializer(Types.getValue(context, pop())), false);
            ++i;
        }

        push(array);
    }

    @Override
    public void visit(ExecutionContext context, AssignmentExpression expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        Object lhs = pop();
        if (!(lhs instanceof Reference)) {
            throw new ThrowException(context, context.createTypeError(expr.getLhs() + " is not a reference"));
        }

        Reference lhsRef = (Reference) lhs;

        expr.getRhs().accept(context, this, strict);
        Object rhs = Types.getValue(context, pop());

        lhsRef.putValue(context, rhs);
        push(rhs);
    }

    @Override
    public void visit(ExecutionContext context, BitwiseInversionOperatorExpression expr, boolean strict) {
        expr.getExpr().accept(context, this, strict);
        push(~Types.toInt32(context, Types.getValue(context, pop())));
    }

    @Override
    public void visit(ExecutionContext context, BlockStatement statement, boolean strict) {
        List<Statement> content = statement.getBlockContent();

        Object completionValue = Types.UNDEFINED;

        for (Statement each : content) {
            Position position = each.getPosition();
            if (position != null) {
                context.setLineNumber(position.getLine());
            }

            each.accept(context, this, strict);
            Completion completion = (Completion) pop();
            if (completion.type == Completion.Type.NORMAL) {
                if (completion.value != null) {
                    completionValue = completion.value;
                }
                continue;
            }
            if (completion.type == Completion.Type.CONTINUE) {
                continue;
            }
            if (completion.type == Completion.Type.RETURN) {
                push(completion);
                return;
            }
            if (completion.type == Completion.Type.BREAK) {
                break;
            }
        }

        push(Completion.createNormal(completionValue));
    }

    @Override
    public void visit(ExecutionContext context, BooleanLiteralExpression expr, boolean strict) {
        push(expr.getValue());
    }

    @Override
    public void visit(ExecutionContext context, BreakStatement statement, boolean strict) {
        push(Completion.createBreak(statement.getTarget()));
    }

    @Override
    public void visit(ExecutionContext context, CaseClause clause, boolean strict) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ExecutionContext context, DefaultCaseClause clause, boolean strict) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ExecutionContext context, CatchClause clause, boolean strict) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ExecutionContext context, CompoundAssignmentExpression expr, boolean strict) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ExecutionContext context, ContinueStatement statement, boolean strict) {
        push(Completion.createContinue(statement.getTarget()));
    }

    @Override
    public void visit(ExecutionContext context, DeleteOpExpression expr, boolean strict) {
        expr.getExpr().accept(context, this, strict);
        Object result = pop();
        if (!(result instanceof Reference)) {
            push(true);
            return;
        }

        Reference ref = (Reference) result;
        if (ref.isUnresolvableReference()) {
            if (strict) {
                throw new ThrowException(context, context.createSyntaxError("cannot delete unresolvable reference"));
            } else {
                push(true);
                return;
            }
        }

        if (ref.isPropertyReference()) {
            push(Types.toObject(context, ref.getBase()).delete(context, ref.getReferencedName(), ref.isStrictReference()));
            return;
        }

        if (ref.isStrictReference()) {
            throw new ThrowException(context, context.createSyntaxError("cannot delete from environment record binding"));
        }

        EnvironmentRecord bindings = (EnvironmentRecord) ref.getBase();

        push(bindings.deleteBinding(context, ref.getReferencedName()));
    }

    @Override
    public void visit(ExecutionContext context, DoWhileStatement statement, boolean strict) {

    }

    @Override
    public void visit(ExecutionContext context, EmptyStatement statement, boolean strict) {
        push(Completion.createNormal());
    }

    @Override
    public void visit(ExecutionContext context, EqualityOperatorExpression expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        expr.getRhs().accept(context, this, strict);

        Object rhs = Types.getValue(context, pop());
        Object lhs = Types.getValue(context, pop());

        if (expr.getOp().equals("==")) {
            push(Types.compareEquality(context, lhs, rhs));
        } else {
            push(!Types.compareEquality(context, lhs, rhs));
        }
    }

    @Override
    public void visit(ExecutionContext context, CommaOperator expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        pop();
        expr.getRhs().accept(context, this, strict);
        // leave RHS on the stack
    }

    @Override
    public void visit(ExecutionContext context, ExpressionStatement statement, boolean strict) {
        push(Completion.createNormal(pop()));
    }

    @Override
    public void visit(ExecutionContext context, ForExprInStatement statement, boolean strict) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ExecutionContext context, ForExprStatement statement, boolean strict) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ExecutionContext context, ForVarDeclInStatement statement, boolean strict) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ExecutionContext context, ForVarDeclStatement statement, boolean strict) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ExecutionContext context, FunctionCallExpression expr, boolean strict) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ExecutionContext context, FunctionDeclaration statement, boolean strict) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ExecutionContext context, FunctionExpression expr, boolean strict) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ExecutionContext context, IdentifierReferenceExpression expr, boolean strict) {
        push(context.resolve(expr.getIdentifier()));
    }

    @Override
    public void visit(ExecutionContext context, IfStatement statement, boolean strict) {
        statement.getTest().accept(context, this, strict);

        Boolean result = Types.toBoolean(pop());
        if (result) {
            statement.getThenBlock().accept(context, this, strict);
        } else if (statement.getElseBlock() != null) {
            statement.getElseBlock().accept(context, this, strict);
        } else {
            push(Completion.createNormal());
        }
    }

    @Override
    public void visit(ExecutionContext context, InOperatorExpression expr, boolean strict) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ExecutionContext context, InstanceofExpression expr, boolean strict) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ExecutionContext context, LogicalExpression expr, boolean strict) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ExecutionContext context, LogicalNotOperatorExpression expr, boolean strict) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ExecutionContext context, DotExpression expr, boolean strict) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ExecutionContext context, BracketExpression expr, boolean strict) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ExecutionContext context, MultiplicativeExpression expr, boolean strict) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ExecutionContext context, NewOperatorExpression expr, boolean strict) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ExecutionContext context, NullLiteralExpression expr, boolean strict) {
        push(Types.NULL);
    }

    @Override
    public void visit(ExecutionContext context, NumberLiteralExpression expr, boolean strict) {
        // TODO Auto-generated method stub
    }

    @Override
    public void visit(ExecutionContext context, ObjectLiteralExpression expr, boolean strict) {
        DynObject obj = BuiltinObject.newObject(context);

        List<PropertyAssignment> assignments = expr.getPropertyAssignments();

        for (PropertyAssignment each : assignments) {
            each.accept(context, this, strict);
            Object value = Types.getValue(context, pop());
            Object original = obj.getOwnProperty(context, each.getName());
            PropertyDescriptor desc = null;
            if (each instanceof PropertyGet) {
                desc = PropertyDescriptor.newPropertyDescriptorForObjectInitializerGet(original, each.getName(), (JSFunction) value);
            } else if (each instanceof PropertySet) {
                desc = PropertyDescriptor.newPropertyDescriptorForObjectInitializerSet(original, each.getName(), (JSFunction) value);
            } else {
                desc = PropertyDescriptor.newPropertyDescriptorForObjectInitializer(each.getName(), value);
            }
            obj.defineOwnProperty(context, each.getName(), desc, false);
        }

        push(obj);
    }

    @Override
    public void visit(ExecutionContext context, PostOpExpression expr, boolean strict) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ExecutionContext context, PreOpExpression expr, boolean strict) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ExecutionContext context, PrintStatement statement, boolean strict) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ExecutionContext context, PropertyGet propertyGet, boolean strict) {
        // TODO Auto-generated method stub
    }

    @Override
    public void visit(ExecutionContext context, PropertySet propertySet, boolean strict) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ExecutionContext context, NamedValue namedValue, boolean strict) {
        namedValue.getExpr().accept(context, this, strict);
    }

    @Override
    public void visit(ExecutionContext context, RegexpLiteralExpression expr, boolean strict) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ExecutionContext context, RelationalExpression expr, boolean strict) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ExecutionContext context, ReturnStatement statement, boolean strict) {
        if (statement.getExpr() != null) {
            statement.getExpr().accept(context, this, strict);
            push(Completion.createReturn(pop()));
        } else {
            push(Completion.createReturn(Types.UNDEFINED));
        }
    }

    @Override
    public void visit(ExecutionContext context, StrictEqualityOperatorExpression expr, boolean strict) {
        // TODO Auto-generated method stub
    }

    @Override
    public void visit(ExecutionContext context, StringLiteralExpression expr, boolean strict) {
        push(expr.getLiteral());
    }

    @Override
    public void visit(ExecutionContext context, SwitchStatement statement, boolean strict) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ExecutionContext context, TernaryExpression expr, boolean strict) {
        expr.getTest().accept(context, this, strict);
        if (Types.toBoolean(pop())) {
            expr.getThenExpr().accept(context, this, strict);
        } else {
            expr.getElseExpr().accept(context, this, strict);
        }
    }

    @Override
    public void visit(ExecutionContext context, ThisExpression expr, boolean strict) {
        push(context.getThisBinding());
    }

    @Override
    public void visit(ExecutionContext context, ThrowStatement statement, boolean strict) {
        statement.getExpr().accept(context, this, strict);
        throw new ThrowException(context, pop());
    }

    @Override
    public void visit(ExecutionContext context, TryStatement statement, boolean strict) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ExecutionContext context, TypeOfOpExpression expr, boolean strict) {
        expr.getExpr().accept(context, this, strict);
        push(Types.typeof(context, pop()));
    }

    @Override
    public void visit(ExecutionContext context, UnaryMinusExpression expr, boolean strict) {
        expr.getExpr().accept(context, this, strict);
        Number oldValue = Types.toNumber(context, pop());
        if (oldValue instanceof Double) {
            if (Double.isNaN(oldValue.doubleValue())) {
                push(Double.NaN);
            } else {
                push(-1 * oldValue.doubleValue());
            }
        } else {
            push(-1 * oldValue.longValue());
        }
    }

    @Override
    public void visit(ExecutionContext context, UnaryPlusExpression expr, boolean strict) {
        expr.getExpr().accept(context, this, strict);
        push(Types.toNumber(context, pop()));
    }

    @Override
    public void visit(ExecutionContext context, VariableDeclaration expr, boolean strict) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ExecutionContext context, VariableStatement statement, boolean strict) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ExecutionContext context, VoidOperatorExpression expr, boolean strict) {
        expr.getExpr().accept(context, this, strict);
        pop();
    }

    @Override
    public void visit(ExecutionContext context, WhileStatement statement, boolean strict) {
        Expression testExpr = statement.getTest();
        Statement block = statement.getBlock();

        Object v = null;

        while (true) {
            testExpr.accept(context, this, strict);
            Boolean testResult = Types.toBoolean(Types.getValue(context, pop()));
            if (testResult) {
                block.accept(context, this, strict);
                Completion completion = (Completion) pop();
                if (completion.value != null) {
                    v = completion.value;
                }
                if (completion.type == Completion.Type.CONTINUE) {
                    if (completion.target == null) {
                        continue;
                    } else if (!statement.getLabels().contains(completion.target)) {
                        push(completion);
                        return;
                    }
                }
                if (completion.type == Completion.Type.BREAK) {
                    if (completion.target == null) {
                        break;
                    } else if (!statement.getLabels().contains(completion.target)) {
                        push(completion);
                        return;
                    }
                }
                if (completion.type == Completion.Type.RETURN) {
                    push(Completion.createReturn(v));
                    return;
                }
            } else {
                break;
            }
        }

        push(Completion.createNormal(v));
    }

    @Override
    public void visit(ExecutionContext context, WithStatement statement, boolean strict) {
        // TODO Auto-generated method stub

    }

}
