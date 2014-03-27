package org.dynjs.runtime.interp;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.dynjs.exception.ThrowException;
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
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.BlockManager;
import org.dynjs.runtime.BlockManager.Entry;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.DynArray;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.EnvironmentRecord;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.BuiltinArray;
import org.dynjs.runtime.builtins.types.BuiltinNumber;
import org.dynjs.runtime.builtins.types.BuiltinObject;
import org.dynjs.runtime.builtins.types.BuiltinRegExp;

public class BasicInterpretingVisitor implements InterpretingVisitor {

    private List<Object> stack = new ArrayList<>();
    private BlockManager blockManager;

    public BasicInterpretingVisitor(BlockManager blockManager) {
        this.blockManager = blockManager;
    }

    public void push(Object value) {
        if (value == null) {
            new Exception().printStackTrace();
        }
        this.stack.add(value);
    }

    public Object pop() {
        return this.stack.remove(this.stack.size() - 1);
    }

    @Override
    public void visit(Object context, AdditiveExpression expr, boolean strict) {
        if (expr.getOp().equals("+")) {
            visitPlus(context, expr, strict);
        } else {
            visitMinus(context, expr, strict);
        }
    }

    public void visitPlus(Object context, AdditiveExpression expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        Object lhs = Types.toPrimitive(context, getValue(context, pop()));

        expr.getRhs().accept(context, this, strict);
        Object rhs = Types.toPrimitive(context, getValue(context, pop()));

        if (lhs instanceof String || rhs instanceof String) {
            push(Types.toString(context, lhs) + Types.toString(context, rhs));
            return;
        }

        Number lhsNum = Types.toNumber(context, lhs);
        Number rhsNum = Types.toNumber(context, rhs);

        if (Double.isNaN(lhsNum.doubleValue()) || Double.isNaN(rhsNum.doubleValue())) {
            push(Double.NaN);
            return;
        }

        if (lhsNum instanceof Double || rhsNum instanceof Double) {
            if (lhsNum.doubleValue() == 0.0 && rhsNum.doubleValue() == 0.0) {
                if (Double.compare(lhsNum.doubleValue(), 0.0) < 0 && Double.compare(rhsNum.doubleValue(), 0.0) < 0) {
                    push(-0.0);
                    return;
                } else {
                    push(0.0);
                    return;
                }
            }
            push(lhsNum.doubleValue() + rhsNum.doubleValue());
            return;
        }

        push(lhsNum.longValue() + rhsNum.longValue());
    }

    public void visitMinus(Object context, AdditiveExpression expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        Number lhs = Types.toNumber(context, getValue(context, pop()));

        expr.getRhs().accept(context, this, strict);
        Number rhs = Types.toNumber(context, getValue(context, pop()));

        if (Double.isNaN(lhs.doubleValue()) || Double.isNaN(rhs.doubleValue())) {
            push(Double.NaN);
            return;
        }

        if (lhs instanceof Double || rhs instanceof Double) {
            if (lhs.doubleValue() == 0.0 && rhs.doubleValue() == 0.0) {
                if (Double.compare(lhs.doubleValue(), 0.0) < 0 && Double.compare(rhs.doubleValue(), 0.0) < 0) {
                    push(+0.0);
                    return;
                }

            }
            push(lhs.doubleValue() - rhs.doubleValue());
            return;
        }

        push(lhs.longValue() - rhs.longValue());
    }

    @Override
    public void visit(Object context, BitwiseExpression expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        Object lhs = getValue(context, pop());

        Long lhsNum = null;

        if (expr.getOp().equals(">>>")) {
            lhsNum = Types.toUint32(context, lhs);
        } else {
            lhsNum = Types.toInt32(context, lhs);
        }

        expr.getRhs().accept(context, this, strict);

        if (expr.getOp().equals("<<")) {
            // 11.7.1
            Long rhsNum = Types.toUint32(context, getValue(context, pop()));
            int shiftCount = rhsNum.intValue() & 0x1F;
            push((int) (lhsNum.longValue() << shiftCount));
        } else if (expr.getOp().equals(">>")) {
            // 11.7.2
            Long rhsNum = Types.toUint32(context, getValue(context, pop()));
            int shiftCount = rhsNum.intValue() & 0x1F;
            push((int) (lhsNum.longValue() >> shiftCount));
        } else if (expr.getOp().equals(">>>")) {
            // 11.7.3
            Long rhsNum = Types.toUint32(context, getValue(context, pop()));
            int shiftCount = rhsNum.intValue() & 0x1F;
            push(lhsNum.longValue() >>> shiftCount);
        } else if (expr.getOp().equals("&")) {
            Long rhsNum = Types.toInt32(context, getValue(context, pop()));
            push(lhsNum.longValue() & rhsNum.longValue());
        } else if (expr.getOp().equals("|")) {
            Long rhsNum = Types.toInt32(context, getValue(context, pop()));
            push(lhsNum.longValue() | rhsNum.longValue());
        } else if (expr.getOp().equals("^")) {
            Long rhsNum = Types.toInt32(context, getValue(context, pop()));
            push(lhsNum.longValue() ^ rhsNum.longValue());
        }
    }

    @Override
    public void visit(Object context, ArrayLiteralExpression expr, boolean strict) {
        DynArray array = BuiltinArray.newArray((ExecutionContext) context);

        int i = 0;
        for (Expression each : expr.getExprs()) {
            Object value = null;
            if (each != null) {
                each.accept(context, this, strict);
                value = getValue(context, pop());
                array.defineOwnProperty((ExecutionContext) context, "" + i, PropertyDescriptor.newPropertyDescriptorForObjectInitializer(value), false);
            }
            ++i;
        }
        array.put((ExecutionContext) context, "length", (long) i, true);

        push(array);
    }

    @Override
    public void visit(Object context, AssignmentExpression expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        Object lhs = pop();
        if (!(lhs instanceof Reference)) {
            throw new ThrowException((ExecutionContext) context, ((ExecutionContext) context).createReferenceError(expr.getLhs() + " is not a reference"));
        }

        Reference lhsRef = (Reference) lhs;

        expr.getRhs().accept(context, this, strict);
        Object rhs = getValue(context, pop());

        lhsRef.putValue((ExecutionContext) context, rhs);
        push(rhs);
    }

    @Override
    public void visit(Object context, BitwiseInversionOperatorExpression expr, boolean strict) {
        expr.getExpr().accept(context, this, strict);
        push(~Types.toInt32(context, getValue(context, pop())));
    }

    @Override
    public void visit(Object context, BlockStatement statement, boolean strict) {
        List<Statement> content = statement.getBlockContent();

        Object completionValue = Types.UNDEFINED;

        for (Statement each : content) {
            Position position = each.getPosition();
            if (position != null) {
                ((ExecutionContext) context).setLineNumber(position.getLine());
            }

            each.accept(context, this, strict);
            Completion completion = (Completion) pop();
            if (completion.type == Completion.Type.NORMAL) {
                completionValue = completion.value;
                continue;
            }
            if (completion.type == Completion.Type.CONTINUE) {
                push(completion);
                return;
            }
            if (completion.type == Completion.Type.RETURN) {
                push(completion);
                return;
            }
            if (completion.type == Completion.Type.BREAK) {
                completion.value = completionValue;
                if (completion.target != null && statement.getLabels().contains(completion.target)) {
                    push(Completion.createNormal(completionValue));
                } else {
                    push(completion);
                }
                return;
            }
        }

        push(Completion.createNormal(completionValue));
    }

    @Override
    public void visit(Object context, BooleanLiteralExpression expr, boolean strict) {
        push(expr.getValue());
    }

    @Override
    public void visit(Object context, BreakStatement statement, boolean strict) {
        push(Completion.createBreak(statement.getTarget()));
    }

    @Override
    public void visit(Object context, CaseClause clause, boolean strict) {
        // not used, handled by switch-statement
    }

    @Override
    public void visit(Object context, DefaultCaseClause clause, boolean strict) {
        // not used, handled by switch-statement
    }

    @Override
    public void visit(Object context, CatchClause clause, boolean strict) {
        // not used, handled by try-statement
    }

    @Override
    public void visit(Object context, CompoundAssignmentExpression expr, boolean strict) {
        expr.getRootExpr().accept(context, this, strict);
        Object r = pop();

        expr.getRootExpr().getLhs().accept(context, this, strict);
        Object lref = pop();

        if (lref instanceof Reference) {
            if (((Reference) lref).isStrictReference()) {
                if (((Reference) lref).getBase() instanceof EnvironmentRecord) {
                    if (((Reference) lref).getReferencedName().equals("arguments") || ((Reference) lref).getReferencedName().equals("eval")) {
                        throw new ThrowException((ExecutionContext) context, ((ExecutionContext) context).createSyntaxError("invalid assignment: " + ((Reference) lref).getReferencedName()));
                    }
                }
            }

            ((Reference) lref).putValue((ExecutionContext) context, r);
            push(r);
            return;
        }

        throw new ThrowException((ExecutionContext) context, ((ExecutionContext) context).createReferenceError("cannot assign to non-reference"));
    }

    @Override
    public void visit(Object context, ContinueStatement statement, boolean strict) {
        push(Completion.createContinue(statement.getTarget()));
    }

    @Override
    public void visit(Object context, DeleteOpExpression expr, boolean strict) {
        expr.getExpr().accept(context, this, strict);
        Object result = pop();
        if (!(result instanceof Reference)) {
            push(true);
            return;
        }

        Reference ref = (Reference) result;
        if (ref.isUnresolvableReference()) {
            if (strict) {
                throw new ThrowException((ExecutionContext) context, ((ExecutionContext) context).createSyntaxError("cannot delete unresolvable reference"));
            } else {
                push(true);
                return;
            }
        }

        if (ref.isPropertyReference()) {
            push(Types.toObject(context, ref.getBase()).delete((ExecutionContext) context, ref.getReferencedName(), ref.isStrictReference()));
            return;
        }

        if (ref.isStrictReference()) {
            throw new ThrowException((ExecutionContext) context, ((ExecutionContext) context).createSyntaxError("cannot delete from environment record binding"));
        }

        EnvironmentRecord bindings = (EnvironmentRecord) ref.getBase();

        push(bindings.deleteBinding((ExecutionContext) context, ref.getReferencedName()));
    }

    @Override
    public void visit(Object context, DoWhileStatement statement, boolean strict) {
        Expression testExpr = statement.getTest();
        Statement block = statement.getBlock();

        Object v = null;

        while (true) {
            Completion completion = invokeCompiledBlockStatement(context, "DoWhile", block);
            if (completion.value != null) {
                v = completion.value;
            }
            if (completion.type == Completion.Type.CONTINUE) {
                if (completion.target == null) {
                    // nothing
                } else if (!statement.getLabels().contains(completion.target)) {
                    push(completion);
                    return;
                }
            } else if (completion.type == Completion.Type.BREAK) {
                if (completion.target == null) {
                    break;
                } else if (!statement.getLabels().contains(completion.target)) {
                    push(completion);
                    return;
                } else {
                    break;
                }
            } else if (completion.type == Completion.Type.RETURN) {
                push(Completion.createReturn(v));
                return;
            }

            testExpr.accept(context, this, strict);
            Boolean testResult = Types.toBoolean(getValue(context, pop()));
            if (!testResult) {
                break;
            }
        }

        push(Completion.createNormal(v));
    }

    @Override
    public void visit(Object context, EmptyStatement statement, boolean strict) {
        push(Completion.createNormal());
    }

    @Override
    public void visit(Object context, EqualityOperatorExpression expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        Object lhs = getValue(context, pop());

        expr.getRhs().accept(context, this, strict);
        Object rhs = getValue(context, pop());

        if (expr.getOp().equals("==")) {
            push(Types.compareEquality(context, lhs, rhs));
        } else {
            push(!Types.compareEquality(context, lhs, rhs));
        }
    }

    @Override
    public void visit(Object context, CommaOperator expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        getValue(context, pop());
        expr.getRhs().accept(context, this, strict);
        push(getValue(context, pop()));
        // leave RHS on the stack
    }

    @Override
    public void visit(Object context, ExpressionStatement statement, boolean strict) {
        Expression expr = statement.getExpr();
        if (expr instanceof FunctionDeclaration) {
            push(Completion.createNormal());
        } else {
            expr.accept(context, this, strict);
            push(Completion.createNormal(getValue(context, pop())));
        }
    }

    @Override
    public void visit(Object context, FloatingNumberExpression expr, boolean strict) {
        push(expr.getValue());
    }

    @Override
    public void visit(Object context, ForExprInStatement statement, boolean strict) {
        statement.getRhs().accept(context, this, strict);

        Object exprRef = pop();
        Object exprValue = getValue(context, exprRef);

        if (exprValue == Types.NULL || exprValue == Types.UNDEFINED) {
            push(Completion.createNormal());
            return;
        }

        JSObject obj = Types.toObject(context, exprValue);

        Object v = null;

        List<String> names = obj.getAllEnumerablePropertyNames().toList();

        for (String each : names) {
            statement.getExpr().accept(context, this, strict);
            Object lhsRef = pop();

            if (lhsRef instanceof Reference) {
                ((Reference) lhsRef).putValue((ExecutionContext) context, each);
            }

            statement.getBlock().accept(context, this, strict);
            Completion completion = (Completion) pop();
            //Completion completion = invokeCompiledBlockStatement(context, "ForIn", statement.getBlock());

            if (completion.value != null) {
                v = completion.value;
            }

            if (completion.type == Completion.Type.BREAK) {
                if (completion.target == null || statement.getLabels().contains(completion.target)) {
                    push(Completion.createNormal(v));
                } else {
                    push(completion);
                }
                return;
            }

            if (completion.type == Completion.Type.RETURN || completion.type == Completion.Type.BREAK) {
                push(completion);
                return;
            }
        }

        push(Completion.createNormal(v));
    }

    @Override
    public void visit(Object context, ForExprOfStatement statement, boolean strict) {
        statement.getRhs().accept(context, this, strict);

        Object exprRef = pop();
        Object exprValue = getValue(context, exprRef);

        if (exprValue == Types.NULL || exprValue == Types.UNDEFINED) {
            push(Completion.createNormal());
            return;
        }

        JSObject obj = Types.toObject(context, exprValue);

        Object v = null;

        List<String> names = obj.getAllEnumerablePropertyNames().toList();

        for (String each : names) {
            statement.getExpr().accept(context, this, strict);
            Object lhsRef = pop();

            if (lhsRef instanceof Reference) {
                Reference propertyRef = ((ExecutionContext) context).createPropertyReference(obj, each);
                ((Reference) lhsRef).putValue((ExecutionContext) context, propertyRef.getValue((ExecutionContext) context));
            }

            statement.getBlock().accept(context, this, strict);
            Completion completion = (Completion) pop();
            //Completion completion = invokeCompiledBlockStatement(context, "ForOf", statement.getBlock());

            if (completion.value != null) {
                v = completion.value;
            }

            if (completion.type == Completion.Type.BREAK) {
                if (completion.target == null || statement.getLabels().contains(completion.target)) {
                    push(Completion.createNormal(v));
                } else {
                    push(completion);
                }
                return;
            }

            if (completion.type == Completion.Type.RETURN || completion.type == Completion.Type.BREAK) {
                push(completion);
                return;
            }
        }

        push(Completion.createNormal(v));
    }

    @Override
    public void visit(Object context, ForExprStatement statement, boolean strict) {
        if (statement.getExpr() != null) {
            statement.getExpr().accept(context, this, strict);
            pop();
        }

        Expression test = statement.getTest();
        Expression incr = statement.getIncrement();
        Statement body = statement.getBlock();

        Object v = null;

        while (true) {
            if (test != null) {
                test.accept(context, this, strict);
                if (!Types.toBoolean(getValue(context, pop()))) {
                    break;
                }
            }
            body.accept(context, this, strict);
            Completion completion = (Completion) pop();
            //Completion completion = invokeCompiledBlockStatement(context, "ForExpr", body);

            if (completion.value != null && completion.value != Types.UNDEFINED) {
                v = completion.value;
            }

            if (completion.type == Completion.Type.BREAK) {
                if (completion.target == null || statement.getLabels().contains(completion.target)) {
                    push(Completion.createNormal(v));
                } else {
                    completion.value = v;
                    push(completion);
                }
                return;
            }
            if (completion.type == Completion.Type.RETURN) {
                push(completion);
                return;
            }
            if (completion.type == Completion.Type.CONTINUE) {
                if (completion.target != null && !statement.getLabels().contains(completion.target)) {
                    push(completion);
                    return;
                }
            }

            if (incr != null) {
                incr.accept(context, this, strict);
                getValue(context, pop());
            }
        }

        push(Completion.createNormal(v));
    }

    @Override
    public void visit(Object context, ForVarDeclInStatement statement, boolean strict) {
        statement.getDeclaration().accept(context, this, strict);
        String varName = (String) pop();

        statement.getRhs().accept(context, this, strict);

        Object exprRef = pop();
        Object exprValue = getValue(context, exprRef);

        if (exprValue == Types.NULL || exprValue == Types.UNDEFINED) {
            push(Completion.createNormal());
            return;
        }

        JSObject obj = Types.toObject(context, exprValue);

        Object v = null;

        List<String> names = obj.getAllEnumerablePropertyNames().toList();

        for (String each : names) {
            Reference varRef = ((ExecutionContext) context).resolve(varName);

            varRef.putValue((ExecutionContext) context, each);

            statement.getBlock().accept(context, this, strict);
            Completion completion = (Completion) pop();
            //Completion completion = invokeCompiledBlockStatement(context, "ForVarDeclsIn", statement.getBlock());

            if (completion.value != null) {
                v = completion.value;
            }

            if (completion.type == Completion.Type.BREAK) {
                if (completion.target == null || statement.getLabels().contains(completion.target)) {
                    push(Completion.createNormal(v));
                } else {
                    push(completion);
                }
                return;
            }

            if (completion.type == Completion.Type.RETURN || completion.type == Completion.Type.BREAK) {
                push(completion);
                return;
            }
        }

        push(Completion.createNormal(v));

    }

    @Override
    public void visit(Object context, ForVarDeclOfStatement statement, boolean strict) {
        statement.getDeclaration().accept(context, this, strict);
        String varName = (String) pop();

        statement.getRhs().accept(context, this, strict);

        Object exprRef = pop();
        Object exprValue = getValue(context, exprRef);

        if (exprValue == Types.NULL || exprValue == Types.UNDEFINED) {
            push(Completion.createNormal());
            return;
        }

        JSObject obj = Types.toObject(context, exprValue);

        Object v = null;

        List<String> names = obj.getAllEnumerablePropertyNames().toList();

        for (String each : names) {
            Reference varRef = ((ExecutionContext) context).resolve(varName);
            Reference propertyRef = ((ExecutionContext) context).createPropertyReference(obj, each);

            varRef.putValue((ExecutionContext) context, propertyRef.getValue((ExecutionContext) context));

            statement.getBlock().accept(context, this, strict);
            Completion completion = (Completion) pop();
            //Completion completion = invokeCompiledBlockStatement(context, "ForVarDeclsOf", statement.getBlock());

            if (completion.value != null) {
                v = completion.value;
            }

            if (completion.type == Completion.Type.BREAK) {
                if (completion.target == null || statement.getLabels().contains(completion.target)) {
                    push(Completion.createNormal(v));
                } else {
                    push(completion);
                }
                return;
            }

            if (completion.type == Completion.Type.RETURN || completion.type == Completion.Type.BREAK) {
                push(completion);
                return;
            }
        }

        push(Completion.createNormal(v));

    }

    @Override
    public void visit(Object context, ForVarDeclStatement statement, boolean strict) {

        List<VariableDeclaration> decls = statement.getDeclarationList();
        for (VariableDeclaration each : decls) {
            each.accept(context, this, strict);
            pop();
        }

        Expression test = statement.getTest();
        Expression incr = statement.getIncrement();
        Statement body = statement.getBlock();

        Object v = null;

        while (true) {
            if (test != null) {
                test.accept(context, this, strict);
                if (!Types.toBoolean(getValue(context, pop()))) {
                    break;
                }
            }

            body.accept(context, this, strict);
            Completion completion = (Completion) pop();
            //Completion completion = invokeCompiledBlockStatement(context, "ForVarDecl", body);

            if (completion.value != null && completion.value != Types.UNDEFINED) {
                v = completion.value;
            }

            if (completion.type == Completion.Type.BREAK) {
                if (completion.target == null || statement.getLabels().contains(completion.target)) {
                    push(Completion.createNormal(v));
                } else {
                    completion.value = v;
                    push(completion);
                }
                return;
            }
            if (completion.type == Completion.Type.RETURN) {
                push(completion);
                return;
            }
            if (completion.type == Completion.Type.CONTINUE) {
                if (completion.target != null && !statement.getLabels().contains(completion.target)) {
                    push(completion);
                    return;
                }
            }

            if (incr != null) {
                incr.accept(context, this, strict);
                getValue(context, pop());
            }
        }

        push(Completion.createNormal(v));
    }

    @Override
    public void visit(Object context, FunctionCallExpression expr, boolean strict) {
        expr.getMemberExpression().accept(context, this, strict);
        Object ref = pop();
        Object function = getValue(context, ref);

        List<Expression> argExprs = expr.getArgumentExpressions();

        Object[] args = new Object[argExprs.size()];
        int i = 0;

        for (Expression each : argExprs) {
            each.accept(context, this, strict);
            args[i] = getValue(context, pop());
            ++i;
        }

        if (!(function instanceof JSFunction)) {
            throw new ThrowException((ExecutionContext) context, ((ExecutionContext) context).createTypeError(expr.getMemberExpression() + " is not calllable"));
        }

        Object thisValue = null;

        if (ref instanceof Reference) {
            if (((Reference) ref).isPropertyReference()) {
                thisValue = ((Reference) ref).getBase();
            } else {
                thisValue = ((EnvironmentRecord) ((Reference) ref).getBase()).implicitThisValue();
            }
        }

        push(((ExecutionContext) context).call(ref, (JSFunction) function, thisValue, args));
    }

    @Override
    public void visit(Object context, FunctionDeclaration statement, boolean strict) {
        push(Completion.createNormal());
    }

    @Override
    public void visit(Object context, FunctionExpression expr, boolean strict) {
        JSFunction compiledFn = ((ExecutionContext) context).getCompiler().compileFunction((ExecutionContext) context,
                expr.getDescriptor().getIdentifier(),
                expr.getDescriptor().getFormalParameterNames(),
                expr.getDescriptor().getBlock(),
                expr.getDescriptor().isStrict() || strict);
        push(compiledFn);
    }

    @Override
    public void visit(Object context, IdentifierReferenceExpression expr, boolean strict) {
        push(((ExecutionContext) context).resolve(expr.getIdentifier()));
    }

    @Override
    public void visit(Object context, IfStatement statement, boolean strict) {
        statement.getTest().accept(context, this, strict);

        Boolean result = Types.toBoolean(getValue(context, pop()));
        if (result) {
            push(invokeCompiledBlockStatement(context, "Then", statement.getThenBlock()));
        } else if (statement.getElseBlock() != null) {
            push(invokeCompiledBlockStatement(context, "Else", statement.getElseBlock()));
        } else {
            push(Completion.createNormal());
        }
    }

    @Override
    public void visit(Object context, InOperatorExpression expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        Object lhs = getValue(context, pop());

        expr.getRhs().accept(context, this, strict);
        Object rhs = getValue(context, pop());

        if (!(rhs instanceof JSObject)) {
            throw new ThrowException((ExecutionContext) context, ((ExecutionContext) context).createTypeError(expr.getRhs() + " is not an object"));
        }

        push(((JSObject) rhs).hasProperty((ExecutionContext) context, Types.toString(context, lhs)));
    }

    @Override
    public void visit(Object context, OfOperatorExpression expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        Object lhs = getValue(context, pop());

        expr.getRhs().accept(context, this, strict);
        Object rhs = getValue(context, pop());

        if (!(rhs instanceof JSObject)) {
            throw new ThrowException((ExecutionContext) context, ((ExecutionContext) context).createTypeError(expr.getRhs() + " is not an object"));
        }

        push(((JSObject) rhs).hasProperty((ExecutionContext) context, Types.toString(context, lhs)));
    }

    @Override
    public void visit(Object context, InstanceofExpression expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        Object lhs = getValue(context, pop());

        expr.getRhs().accept(context, this, strict);
        Object rhs = getValue(context, pop());

        if (rhs instanceof JSObject) {
            if (!(rhs instanceof JSFunction)) {
                throw new ThrowException((ExecutionContext) context, ((ExecutionContext) context).createTypeError(expr.getRhs() + " is not a function"));
            }
            push(((JSFunction) rhs).hasInstance((ExecutionContext) context, lhs));
        } else if (rhs instanceof Class) {
            Class clazz = (Class) rhs;
            push(lhs.getClass().getName().equals(clazz.getName()));
        }
    }

    @Override
    public void visit(Object context, IntegerNumberExpression expr, boolean strict) {
        push(expr.getValue());
    }

    @Override
    public void visit(Object context, LogicalExpression expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        Object lhs = getValue(context, pop());

        if ((expr.getOp().equals("||") && Types.toBoolean(lhs)) || (expr.getOp().equals("&&") && !Types.toBoolean(lhs))) {
            push(lhs);
        } else {
            expr.getRhs().accept(context, this, strict);
        }
    }

    @Override
    public void visit(Object context, LogicalNotOperatorExpression expr, boolean strict) {
        expr.getExpr().accept(context, this, strict);
        push(!Types.toBoolean(getValue(context, pop())));
    }

    @Override
    public void visit(Object context, DotExpression expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        Object baseRef = pop();
        Object baseValue = getValue(context, baseRef);

        String propertyName = expr.getIdentifier();

        Types.checkObjectCoercible(context, baseValue, propertyName);

        push(((ExecutionContext) context).createPropertyReference(baseValue, propertyName));
    }

    @Override
    public void visit(Object context, BracketExpression expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        Object baseRef = pop();
        Object baseValue = getValue(context, baseRef);

        expr.getRhs().accept(context, this, strict);
        Object identifier = getValue(context, pop());

        Types.checkObjectCoercible(context, baseValue);

        String propertyName = Types.toString(context, identifier);

        push(((ExecutionContext) context).createPropertyReference(baseValue, propertyName));
    }

    @Override
    public void visit(Object context, MultiplicativeExpression expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        Number lval = Types.toNumber(context, getValue(context, pop()));

        expr.getRhs().accept(context, this, strict);
        Number rval = Types.toNumber(context, getValue(context, pop()));

        if (Double.isNaN(lval.doubleValue()) || Double.isNaN(rval.doubleValue())) {
            push(Double.NaN);
            return;
        }

        if (lval instanceof Double || rval instanceof Double) {
            switch (expr.getOp()) {
            case "*":
                push(lval.doubleValue() * rval.doubleValue());
                return;
            case "/":
                // Divide-by-zero
                if (isZero(rval)) {
                    if (isZero(lval)) {
                        push(Double.NaN);
                        return;
                    } else if (isSameSign(lval, rval)) {
                        push(Double.POSITIVE_INFINITY);
                        return;
                    } else {
                        push(Double.NEGATIVE_INFINITY);
                        return;
                    }
                }

                // Zero-divided-by-something
                else if (isZero(lval)) {
                    if (isSameSign(lval, rval)) {
                        push(0L);
                    } else {
                        push(-0.0);
                    }
                    return;
                }

                // Regular math
                double primaryValue = lval.doubleValue() / rval.doubleValue();
                if (isRepresentableByLong(primaryValue)) {
                    push((long) primaryValue);
                } else {
                    push(primaryValue);
                }
                return;
            case "%":
                if (rval.doubleValue() == 0.0) {
                    push(Double.NaN);
                    return;
                }
                push(BuiltinNumber.modulo(lval, rval));
                return;
            }
        } else {
            switch (expr.getOp()) {
            case "*":
                push(lval.longValue() * rval.longValue());
                return;
            case "/":
                if (rval.longValue() == 0L) {
                    if (lval.longValue() == 0L) {
                        push(Double.NaN);
                        return;
                    } else if (isSameSign(lval, rval)) {
                        push(Double.POSITIVE_INFINITY);
                        return;
                    } else {
                        push(Double.NEGATIVE_INFINITY);
                        return;
                    }
                }

                if (lval.longValue() == 0) {
                    if (Double.compare(rval.doubleValue(), 0.0) > 0) {
                        push(0L);
                        return;
                    } else {
                        push(-0.0);
                        return;
                    }
                }
                double primaryResult = lval.doubleValue() / rval.longValue();
                if (primaryResult == (long) primaryResult) {
                    push((long) primaryResult);
                } else {
                    push(primaryResult);
                }
                return;
            case "%":
                if (rval.longValue() == 0L) {
                    push(Double.NaN);
                    return;
                }
                
                push(BuiltinNumber.modulo(lval, rval));
                return;
            }
        }
    }

    @Override
    public void visit(Object context, NewOperatorExpression expr, boolean strict) {
        expr.getExpr().accept(context, this, strict);
        Object memberExpr = getValue(context, pop());

        Object[] args = new Object[expr.getArgumentExpressions().size()];

        int i = 0;

        for (Expression each : expr.getArgumentExpressions()) {
            each.accept(context, this, strict);
            args[i] = getValue(context, pop());
            ++i;
        }

        if (memberExpr instanceof JSFunction) {
            push(((ExecutionContext) context).construct((JSFunction) memberExpr, args));
            return;
        }

        throw new ThrowException((ExecutionContext) context, ((ExecutionContext) context).createTypeError("can only construct using functions"));
    }

    @Override
    public void visit(Object context, NullLiteralExpression expr, boolean strict) {
        push(Types.NULL);
    }

    @Override
    public void visit(Object context, ObjectLiteralExpression expr, boolean strict) {
        DynObject obj = BuiltinObject.newObject((ExecutionContext) context);

        List<PropertyAssignment> assignments = expr.getPropertyAssignments();

        for (PropertyAssignment each : assignments) {
            each.accept(context, this, strict);
            String debugName = each.getName();
            Object ref = pop();
            if (ref instanceof Reference) {
                debugName = ((Reference) ref).getReferencedName();
            }
            Object value = getValue(context, ref);
            Object original = obj.getOwnProperty((ExecutionContext) context, each.getName());
            PropertyDescriptor desc = null;
            if (each instanceof PropertyGet) {
                desc = PropertyDescriptor.newPropertyDescriptorForObjectInitializerGet(original, debugName, (JSFunction) value);
            } else if (each instanceof PropertySet) {
                desc = PropertyDescriptor.newPropertyDescriptorForObjectInitializerSet(original, debugName, (JSFunction) value);
            } else {
                desc = PropertyDescriptor.newPropertyDescriptorForObjectInitializer(debugName, value);
            }
            obj.defineOwnProperty((ExecutionContext) context, each.getName(), desc, false);
        }

        push(obj);
    }

    @Override
    public void visit(Object context, PostOpExpression expr, boolean strict) {
        expr.getExpr().accept(context, this, strict);
        Object lhs = pop();

        if (lhs instanceof Reference) {
            if (((Reference) lhs).isStrictReference()) {
                if (((Reference) lhs).getBase() instanceof EnvironmentRecord) {
                    if (((Reference) lhs).getReferencedName().equals("arguments") || ((Reference) lhs).getReferencedName().equals("eval")) {
                        throw new ThrowException((ExecutionContext) context, ((ExecutionContext) context).createSyntaxError("invalid assignment: " + ((Reference) lhs).getReferencedName()));
                    }
                }
            }

            Number newValue = null;
            Number oldValue = Types.toNumber(context, getValue(context, lhs));

            if (oldValue instanceof Double) {
                switch (expr.getOp()) {
                case "++":
                    newValue = oldValue.doubleValue() + 1;
                    break;
                case "--":
                    newValue = oldValue.doubleValue() - 1;
                    break;
                }
            } else {
                switch (expr.getOp()) {
                case "++":
                    newValue = oldValue.longValue() + 1;
                    break;
                case "--":
                    newValue = oldValue.longValue() - 1;
                    break;
                }
            }

            ((Reference) lhs).putValue((ExecutionContext) context, newValue);
            push(oldValue);
        }
    }

    @Override
    public void visit(Object context, PreOpExpression expr, boolean strict) {
        expr.getExpr().accept(context, this, strict);
        Object lhs = pop();

        if (lhs instanceof Reference) {
            if (((Reference) lhs).isStrictReference()) {
                if (((Reference) lhs).getBase() instanceof EnvironmentRecord) {
                    if (((Reference) lhs).getReferencedName().equals("arguments") || ((Reference) lhs).getReferencedName().equals("eval")) {
                        throw new ThrowException((ExecutionContext) context, ((ExecutionContext) context).createSyntaxError("invalid assignment: " + ((Reference) lhs).getReferencedName()));
                    }
                }
            }

            Number newValue = null;
            Number oldValue = Types.toNumber(context, getValue(context, lhs));

            if (oldValue instanceof Double) {
                switch (expr.getOp()) {
                case "++":
                    newValue = oldValue.doubleValue() + 1;
                    break;
                case "--":
                    newValue = oldValue.doubleValue() - 1;
                    break;
                }
            } else {
                switch (expr.getOp()) {
                case "++":
                    newValue = oldValue.longValue() + 1;
                    break;
                case "--":
                    newValue = oldValue.longValue() - 1;
                    break;
                }
            }

            ((Reference) lhs).putValue((ExecutionContext) context, newValue);
            push(newValue);
        }
    }

    @Override
    public void visit(Object context, PropertyGet propertyGet, boolean strict) {
        JSFunction compiledFn = ((ExecutionContext) context).getCompiler().compileFunction((ExecutionContext) context,
                null,
                new String[] {},
                propertyGet.getBlock(),
                strict);
        push(compiledFn);
    }

    @Override
    public void visit(Object context, PropertySet propertySet, boolean strict) {
        JSFunction compiledFn = ((ExecutionContext) context).getCompiler().compileFunction((ExecutionContext) context,
                null,
                new String[] { propertySet.getIdentifier() },
                propertySet.getBlock(),
                strict);
        push(compiledFn);
    }

    @Override
    public void visit(Object context, NamedValue namedValue, boolean strict) {
        namedValue.getExpr().accept(context, this, strict);
    }

    @Override
    public void visit(Object context, RegexpLiteralExpression expr, boolean strict) {
        push(BuiltinRegExp.newRegExp((ExecutionContext) context, expr.getPattern(), expr.getFlags()));
    }

    @Override
    public void visit(Object context, RelationalExpression expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        Object lval = getValue(context, pop());

        expr.getRhs().accept(context, this, strict);
        Object rval = getValue(context, pop());

        Object r = null;

        switch (expr.getOp()) {
        case "<":
            r = Types.compareRelational(context, lval, rval, true);
            if (r == Types.UNDEFINED) {
                push(false);
            } else {
                push(r);
            }
            return;
        case ">":
            r = Types.compareRelational(context, rval, lval, false);
            if (r == Types.UNDEFINED) {
                push(false);
            } else {
                push(r);
            }
            return;
        case "<=":
            r = Types.compareRelational(context, rval, lval, false);
            if (r == Boolean.TRUE || r == Types.UNDEFINED) {
                push(false);
            } else {
                push(true);
            }
            return;
        case ">=":
            r = Types.compareRelational(context, lval, rval, true);
            if (r == Boolean.TRUE || r == Types.UNDEFINED) {
                push(false);
            } else {
                push(true);
            }
            return;
        }

    }

    @Override
    public void visit(Object context, ReturnStatement statement, boolean strict) {
        if (statement.getExpr() != null) {
            statement.getExpr().accept(context, this, strict);
            Object value = pop();
            push(Completion.createReturn(getValue(context, value)));
        } else {
            push(Completion.createReturn(Types.UNDEFINED));
        }
    }

    @Override
    public void visit(Object context, StrictEqualityOperatorExpression expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        Object lhs = getValue(context, pop());

        expr.getRhs().accept(context, this, strict);
        Object rhs = getValue(context, pop());

        Object result = null;
        if (expr.getOp().equals("===")) {
            result = Types.compareStrictEquality(context, lhs, rhs);
        } else {
            result = !Types.compareStrictEquality(context, lhs, rhs);
        }
        push(result);
    }

    @Override
    public void visit(Object context, StringLiteralExpression expr, boolean strict) {
        push(expr.getLiteral());
    }

    @Override
    public void visit(Object context, SwitchStatement statement, boolean strict) {
        statement.getExpr().accept(context, this, strict);
        Object value = getValue(context, pop());

        Object v = null;

        int numClauses = statement.getCaseClauses().size();

        int startIndex = -1;
        int defaultIndex = -1;

        for (int i = 0; i < numClauses; ++i) {
            CaseClause each = statement.getCaseClauses().get(i);
            if (each instanceof DefaultCaseClause) {
                defaultIndex = i;
                continue;
            }
            each.getExpression().accept(context, this, strict);
            Object caseTest = pop();
            if (Types.compareStrictEquality(context, value, getValue(context, caseTest))) {
                startIndex = i;
                break;
            }
        }

        if (startIndex < 0 && defaultIndex >= 0) {
            startIndex = defaultIndex;
        }

        if (startIndex >= 0) {
            for (int i = startIndex; i < numClauses; ++i) {
                CaseClause each = statement.getCaseClauses().get(i);
                if (each.getBlock() != null) {
                    each.getBlock().accept(context, this, strict);
                    Completion completion = (Completion) pop();
                    v = completion.value;

                    if (completion.type == Completion.Type.BREAK) {
                        break;
                    } else if (completion.type == Completion.Type.RETURN) {
                        push(completion);
                        return;
                    }
                }
            }
        }

        push(Completion.createNormal(v));
    }

    @Override
    public void visit(Object context, TernaryExpression expr, boolean strict) {
        expr.getTest().accept(context, this, strict);
        if (Types.toBoolean(getValue(context, pop()))) {
            expr.getThenExpr().accept(context, this, strict);
        } else {
            expr.getElseExpr().accept(context, this, strict);
        }
    }

    @Override
    public void visit(Object context, ThisExpression expr, boolean strict) {
        push(((ExecutionContext) context).getThisBinding());
    }

    @Override
    public void visit(Object context, ThrowStatement statement, boolean strict) {
        statement.getExpr().accept(context, this, strict);
        Object throwable = getValue(context, pop());
        // if ( throwable instanceof Throwable ) {
        // ((Throwable) throwable).printStackTrace();
        // }
        throw new ThrowException((ExecutionContext) context, throwable);
    }

    @Override
    public void visit(Object context, TryStatement statement, boolean strict) {
        Completion b = null;
        boolean finallyExecuted = false;
        try {
            b = invokeCompiledBlockStatement(context, "Try", statement.getTryBlock());
            push(b);
        } catch (ThrowException e) {
            if (statement.getCatchClause() != null) {
                // BasicBlock catchBlock = new InterpretedStatement(statement.getCatchClause().getBlock(), strict);
                BasicBlock catchBlock = compiledBlockStatement(context, "Catch", statement.getCatchClause().getBlock());
                try {
                    b = ((ExecutionContext) context).executeCatch(catchBlock, statement.getCatchClause().getIdentifier(), e.getValue());
                } catch (ThrowException e2) {
                    if (statement.getFinallyBlock() != null) {
                        Completion f = invokeCompiledBlockStatement(context, "Finally", statement.getFinallyBlock());
                        if (f.type == Completion.Type.NORMAL) {
                            if (b != null) {
                                push(b);
                            } else {
                                throw e2;
                            }
                        } else {
                            push(f);
                            return;
                        }
                    } else {
                        throw e2;
                    }
                }
            }

            if (statement.getFinallyBlock() != null) {
                finallyExecuted = true;
                Completion f = invokeCompiledBlockStatement(context, "Finally", statement.getFinallyBlock());
                if (f.type == Completion.Type.NORMAL) {
                    if (b != null) {
                        push(b);
                    } else {
                        throw e;
                    }
                } else {
                    push(f);
                    return;
                }
            } else {
                if (b != null) {
                    push(b);
                } else {
                    throw e;
                }
            }
        }

        if (!finallyExecuted && statement.getFinallyBlock() != null) {
            Completion f = invokeCompiledBlockStatement(context, "Finally", statement.getFinallyBlock());
            if (f.type == Completion.Type.NORMAL) {
                push(b);
            } else {
                push(f);
            }
        }

    }

    @Override
    public void visit(Object context, TypeOfOpExpression expr, boolean strict) {
        expr.getExpr().accept(context, this, strict);
        push(Types.typeof(context, pop()));
    }

    @Override
    public void visit(Object context, UnaryMinusExpression expr, boolean strict) {
        expr.getExpr().accept(context, this, strict);
        Object value = getValue(context, pop());
        Number oldValue = Types.toNumber(context, value);
        if (oldValue instanceof Double) {
            if (Double.isNaN(oldValue.doubleValue())) {
                push(Double.NaN);
            } else {
                push(-1 * oldValue.doubleValue());
            }
        } else if (oldValue.longValue() == 0L) {
            push(-0.0);
        } else {
            push(-1 * oldValue.longValue());
        }
    }

    @Override
    public void visit(Object context, UnaryPlusExpression expr, boolean strict) {
        expr.getExpr().accept(context, this, strict);
        push(Types.toNumber(context, getValue(context, pop())));
    }

    @Override
    public void visit(Object context, VariableDeclaration expr, boolean strict) {
        if (expr.getExpr() != null) {
            expr.getExpr().accept(context, this, strict);
            Object value = getValue(context, pop());
            Reference var = ((ExecutionContext) context).resolve(expr.getIdentifier());
            var.putValue((ExecutionContext) context, value);
        }
        push(expr.getIdentifier());
    }

    @Override
    public void visit(Object context, VariableStatement statement, boolean strict) {
        for (VariableDeclaration each : statement.getVariableDeclarations()) {
            each.accept(context, this, strict);
            pop();
        }

        push(Completion.createNormal(Types.UNDEFINED));
    }

    @Override
    public void visit(Object context, VoidOperatorExpression expr, boolean strict) {
        expr.getExpr().accept(context, this, strict);
        Object value = getValue(context, pop());
        push(Types.UNDEFINED);
    }

    @Override
    public void visit(Object context, WhileStatement statement, boolean strict) {
        Expression testExpr = statement.getTest();
        Statement block = statement.getBlock();

        Object v = null;

        while (true) {
            testExpr.accept(context, this, strict);
            Boolean testResult = Types.toBoolean(getValue(context, pop()));
            if (testResult) {
                // block.accept(context, this, strict);
                // Completion completion = (Completion) pop();
                Completion completion = invokeCompiledBlockStatement(context, "While", block);
                if (completion.value != null) {
                    v = completion.value;
                }
                if (completion.type == Completion.Type.CONTINUE) {
                    if (completion.target == null) {
                        continue;
                    } else if (!statement.getLabels().contains(completion.target)) {
                        push(completion);
                        return;
                    } else {
                        continue;
                    }
                }
                if (completion.type == Completion.Type.BREAK) {
                    if (completion.target == null) {
                        break;
                    } else if (!statement.getLabels().contains(completion.target)) {
                        push(completion);
                        return;
                    } else {
                        break;
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
    public void visit(Object context, WithStatement statement, boolean strict) {
        statement.getExpr().accept(context, this, strict);
        JSObject obj = Types.toObject(context, getValue(context, pop()));
        BasicBlock block = compiledBlockStatement(context, "With", statement.getBlock());
        push(((ExecutionContext) context).executeWith(obj, block));
    }

    protected BasicBlock compiledBlockStatement(Object context, String grist, Statement statement) {
        Entry entry = this.blockManager.retrieve(statement.getStatementNumber());
        if (entry.getCompiled() == null) {
            BasicBlock compiledBlock = ((ExecutionContext) context).getCompiler().compileBasicBlock((ExecutionContext) context, grist, statement, ((ExecutionContext) context).isStrict());
            entry.setCompiled(compiledBlock);
        }
        return (BasicBlock) entry.getCompiled();
    }

    protected Completion invokeCompiledBlockStatement(Object context, String grist, Statement statement) {
        BasicBlock block = compiledBlockStatement(context, grist, statement);
        return block.call((ExecutionContext) context);
    }

    protected Object getValue(Object context, Object obj) {
        return Types.getValue(context, obj);
    }

    private boolean isZero(Number n) {
        return n.doubleValue() == 0.0;
    }

    private boolean isNegativeZero(Number n) {
        return isZero(n) && isNegative(n);
    }

    private boolean isPositiveZero(Number n) {
        return isZero(n) && isPositive(n);
    }

    private boolean isNegative(Number n) {
        return (Double.compare(n.doubleValue(), 0.0) < 0);
    }

    private boolean isPositive(Number n) {
        return (Double.compare(n.doubleValue(), 0.0) >= 0);
    }

    private boolean isSameSign(Number n1, Number n2) {
        return (isPositive(n1) && isPositive(n2)) || (isNegative(n1) && isNegative(n2));
    }

    private boolean isDifferentSign(Number n1, Number n2) {
        return (isPositive(n1) && isNegative(n2)) || (isNegative(n1) && isPositive(n2));
    }

    private boolean isRepresentableByLong(double n) {
        if (isNegativeZero(n)) {
            return false;
        }
        return (n == (long) n);
    }
}
