package org.dynjs.runtime.interp;

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
    public void visit(ExecutionContext context, AdditiveExpression expr, boolean strict) {
        if (expr.getOp().equals("+")) {
            visitPlus(context, expr, strict);
        } else {
            visitMinus(context, expr, strict);
        }
    }

    public void visitPlus(ExecutionContext context, AdditiveExpression expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        expr.getRhs().accept(context, this, strict);

        Object rhs = Types.toPrimitive(context, getValue(context, pop()));
        Object lhs = Types.toPrimitive(context, getValue(context, pop()));

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

    public void visitMinus(ExecutionContext context, AdditiveExpression expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        expr.getRhs().accept(context, this, strict);

        Number rhs = Types.toNumber(context, getValue(context, pop()));
        Number lhs = Types.toNumber(context, getValue(context, pop()));

        if (lhs instanceof Double || rhs instanceof Double) {
            push(lhs.doubleValue() - rhs.doubleValue());
            return;
        }

        push(lhs.longValue() - rhs.longValue());
    }

    @Override
    public void visit(ExecutionContext context, BitwiseExpression expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        Object lhs = getValue(context, pop());

        expr.getRhs().accept(context, this, strict);
        Long rhsNum = Types.toInt32(context, getValue(context, pop()));

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
            Object value = null;
            if (each != null) {
                each.accept(context, this, strict);
                value = getValue(context, pop());
            }
            array.defineOwnProperty(context, "" + i, PropertyDescriptor.newPropertyDescriptorForObjectInitializer(value), false);
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
        Object rhs = getValue(context, pop());

        lhsRef.putValue(context, rhs);
        push(rhs);
    }

    @Override
    public void visit(ExecutionContext context, BitwiseInversionOperatorExpression expr, boolean strict) {
        expr.getExpr().accept(context, this, strict);
        push(~Types.toInt32(context, getValue(context, pop())));
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
                push(completion);
                return;
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
        // not used, handled by switch-statement
    }

    @Override
    public void visit(ExecutionContext context, DefaultCaseClause clause, boolean strict) {
        // not used, handled by switch-statement
    }

    @Override
    public void visit(ExecutionContext context, CatchClause clause, boolean strict) {
        // not used, handled by try-statement
    }

    @Override
    public void visit(ExecutionContext context, CompoundAssignmentExpression expr, boolean strict) {
        expr.getRootExpr().accept(context, this, strict);
        Object r = pop();

        expr.getRootExpr().getLhs().accept(context, this, strict);
        Object lref = pop();

        if (lref instanceof Reference) {
            if (((Reference) lref).isStrictReference()) {
                if (((Reference) lref).getBase() instanceof EnvironmentRecord) {
                    if (((Reference) lref).getReferencedName().equals("arguments") || ((Reference) lref).getReferencedName().equals("eval")) {
                        throw new ThrowException(context, context.createSyntaxError("invalid assignment: " + ((Reference) lref).getReferencedName()));
                    }
                }
            }

            ((Reference) lref).putValue(context, r);
            push(r);
            return;
        }

        throw new ThrowException(context, context.createReferenceError("cannot assign to non-reference"));
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
                System.err.println("do/while break: " + completion.target);
                if (completion.target == null) {
                    System.err.println("break self");
                    break;
                } else if (!statement.getLabels().contains(completion.target)) {
                    System.err.println("break other");
                    push(completion);
                    return;
                } else {
                    System.err.println("break self");
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

        System.err.println("do/while return normal");

        push(Completion.createNormal(v));
    }

    @Override
    public void visit(ExecutionContext context, EmptyStatement statement, boolean strict) {
        push(Completion.createNormal());
    }

    @Override
    public void visit(ExecutionContext context, EqualityOperatorExpression expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        expr.getRhs().accept(context, this, strict);

        Object rhs = getValue(context, pop());
        Object lhs = getValue(context, pop());

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
        Expression expr = statement.getExpr();
        if (expr instanceof FunctionDeclaration) {
            push(Completion.createNormal());
        } else {
            expr.accept(context, this, strict);
            push(Completion.createNormal(getValue(context, pop())));
        }
    }

    @Override
    public void visit(ExecutionContext context, ForExprInStatement statement, boolean strict) {
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
                ((Reference) lhsRef).putValue(context, each);
            }

            // statement.getBlock().accept(context, this, strict);
            // Completion completion = (Completion) pop();
            Completion completion = invokeCompiledBlockStatement(context, "ForIn", statement.getBlock());

            if (completion.value != null) {
                v = completion.value;
            }

            if (completion.type == Completion.Type.BREAK) {
                if (completion.target == null || statement.getLabels().contains(completion.target)) {
                    push(Completion.createNormal(v));
                    return;
                }
            }

            if (completion.type == Completion.Type.RETURN || completion.type == Completion.Type.BREAK) {
                push(completion);
                return;
            }
        }

        push(Completion.createNormal(v));
    }

    @Override
    public void visit(ExecutionContext context, ForExprStatement statement, boolean strict) {
        if (statement.getExpr() != null) {
            statement.getExpr().accept(context, this, strict);
            pop();
        }

        Expression test = statement.getTest();
        Expression incr = statement.getIncrement();
        Statement body = statement.getBlock();

        Object v = null;

        while (true) {
            test.accept(context, this, strict);
            if (!Types.toBoolean(getValue(context, pop()))) {
                break;
            }
            // body.accept(context, this, strict);
            // Completion completion = (Completion) pop();
            Completion completion = invokeCompiledBlockStatement(context, "ForExpr", body);

            if (completion.value != null) {
                v = completion.value;
            }

            if (completion.type == Completion.Type.BREAK) {
                if (completion.target == null || statement.getLabels().contains(completion.target)) {
                    push(Completion.createNormal(v));
                    return;
                }
            }
            if (completion.type == Completion.Type.RETURN) {
                push(completion);
                return;
            }
            if (completion.type == Completion.Type.CONTINUE) {
                if (completion.target != null && statement.getLabels().contains(completion.target)) {
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
    public void visit(ExecutionContext context, ForVarDeclInStatement statement, boolean strict) {
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
            Reference varRef = context.resolve(varName);

            varRef.putValue(context, each);

            // statement.getBlock().accept(context, this, strict);
            // Completion completion = (Completion) pop();
            Completion completion = invokeCompiledBlockStatement(context, "ForVarDeclsIn", statement.getBlock());

            if (completion.value != null) {
                v = completion.value;
            }

            if (completion.type == Completion.Type.BREAK) {
                if (completion.target == null || statement.getLabels().contains(completion.target)) {
                    push(Completion.createNormal(v));
                    return;
                }
            }

            if (completion.type == Completion.Type.RETURN || completion.type == Completion.Type.BREAK) {
                push(completion);
                return;
            }
        }

        push(Completion.createNormal(v));

    }

    @Override
    public void visit(ExecutionContext context, ForVarDeclStatement statement, boolean strict) {

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
            test.accept(context, this, strict);
            if (!Types.toBoolean(getValue(context, pop()))) {
                break;
            }

            // body.accept(context, this, strict);
            // Completion completion = (Completion) pop();
            Completion completion = invokeCompiledBlockStatement(context, "ForVarDecl", body);

            if (completion.value != null) {
                v = completion.value;
            }

            if (completion.type == Completion.Type.BREAK) {
                if (completion.target == null || statement.getLabels().contains(completion.target)) {
                    push(Completion.createNormal(v));
                    return;
                }
            }
            if (completion.type == Completion.Type.RETURN) {
                push(completion);
                return;
            }
            if (completion.type == Completion.Type.CONTINUE) {
                if (completion.target != null && statement.getLabels().contains(completion.target)) {
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
    public void visit(ExecutionContext context, FunctionCallExpression expr, boolean strict) {
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
            throw new ThrowException(context, context.createTypeError(expr.getMemberExpression() + " is not calllable"));
        }

        Object thisValue = null;

        if (ref instanceof Reference) {
            if (((Reference) ref).isPropertyReference()) {
                thisValue = ((Reference) ref).getBase();
            } else {
                thisValue = ((EnvironmentRecord) ((Reference) ref).getBase()).implicitThisValue();
            }
        }

        push(context.call(ref, (JSFunction) function, thisValue, args));
    }

    @Override
    public void visit(ExecutionContext context, FunctionDeclaration statement, boolean strict) {
        push(Completion.createNormal());
    }

    @Override
    public void visit(ExecutionContext context, FunctionExpression expr, boolean strict) {
        JSFunction compiledFn = context.getCompiler().compileFunction(context,
                expr.getDescriptor().getIdentifier(),
                expr.getDescriptor().getFormalParameterNames(),
                expr.getDescriptor().getBlock(),
                strict);
        push(compiledFn);
    }

    @Override
    public void visit(ExecutionContext context, IdentifierReferenceExpression expr, boolean strict) {
        push(context.resolve(expr.getIdentifier()));
    }

    @Override
    public void visit(ExecutionContext context, IfStatement statement, boolean strict) {
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
    public void visit(ExecutionContext context, InOperatorExpression expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        expr.getRhs().accept(context, this, strict);

        Object rhs = getValue(context, pop());
        Object lhs = getValue(context, pop());

        if (!(rhs instanceof JSObject)) {
            throw new ThrowException(context, context.createTypeError(expr.getRhs() + " is not an object"));
        }

        push(((JSObject) rhs).hasProperty(context, Types.toString(context, lhs)));
    }

    @Override
    public void visit(ExecutionContext context, InstanceofExpression expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        expr.getRhs().accept(context, this, strict);

        Object rhs = getValue(context, pop());
        Object lhs = getValue(context, pop());

        if (!(rhs instanceof JSFunction)) {
            throw new ThrowException(context, context.createTypeError(expr.getRhs() + " is not a function"));
        }

        push(((JSFunction) rhs).hasInstance(context, lhs));
    }

    @Override
    public void visit(ExecutionContext context, LogicalExpression expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        Object lhs = getValue(context, pop());

        if ((expr.getOp().equals("||") && Types.toBoolean(lhs)) || (expr.getOp().equals("&&") && !Types.toBoolean(lhs))) {
            push(lhs);
        } else {
            expr.getRhs().accept(context, this, strict);
        }
    }

    @Override
    public void visit(ExecutionContext context, LogicalNotOperatorExpression expr, boolean strict) {
        expr.getExpr().accept(context, this, strict);
        push(!Types.toBoolean(getValue(context, pop())));
    }

    @Override
    public void visit(ExecutionContext context, DotExpression expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        Object baseRef = pop();
        Object baseValue = getValue(context, baseRef);

        String propertyName = expr.getIdentifier();

        Types.checkObjectCoercible(context, baseValue);

        push(context.createPropertyReference(baseValue, propertyName));
    }

    @Override
    public void visit(ExecutionContext context, BracketExpression expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        Object baseRef = pop();
        Object baseValue = getValue(context, baseRef);

        expr.getRhs().accept(context, this, strict);
        Object identifier = getValue(context, pop());

        Types.checkObjectCoercible(context, baseValue);

        String propertyName = Types.toString(context, identifier);

        push(context.createPropertyReference(baseValue, propertyName));
    }

    @Override
    public void visit(ExecutionContext context, MultiplicativeExpression expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        expr.getRhs().accept(context, this, strict);

        Number rval = Types.toNumber(context, getValue(context, pop()));
        Number lval = Types.toNumber(context, getValue(context, pop()));

        if (Double.isNaN(lval.doubleValue()) || Double.isNaN(rval.doubleValue())) {
            push(Double.NaN);
        }

        if (lval instanceof Double || rval instanceof Double) {
            switch (expr.getOp()) {
            case "*":
                push(lval.doubleValue() * rval.doubleValue());
                return;
            case "/":
                if (rval.doubleValue() == 0.0) {
                    if (lval.doubleValue() >= 0 && !(rval.doubleValue() == -0.0)) {
                        push(Double.POSITIVE_INFINITY);
                        return;
                    } else {
                        push(Double.NEGATIVE_INFINITY);
                        return;
                    }
                }
                push(lval.doubleValue() / rval.doubleValue());
                return;
            case "%":
                if (rval.doubleValue() == 0.0) {
                    push(Double.NaN);
                    return;
                }
                push(lval.doubleValue() % rval.doubleValue());
                return;
            }
        } else {
            switch (expr.getOp()) {
            case "*":
                push(lval.longValue() * rval.longValue());
                return;
            case "/":
                if (rval.longValue() == 0L) {
                    if (lval.longValue() >= 0L) {
                        push(Double.POSITIVE_INFINITY);
                        return;
                    } else {
                        push(Double.NEGATIVE_INFINITY);
                        return;
                    }
                }
                push(lval.doubleValue() / rval.longValue());
                return;
            case "%":
                if (rval.longValue() == 0L) {
                    push(Double.NaN);
                    return;
                }

                push(lval.longValue() % rval.doubleValue());
                return;
            }
        }
    }

    @Override
    public void visit(ExecutionContext context, NewOperatorExpression expr, boolean strict) {
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
            push(context.construct((JSFunction) memberExpr, args));
            return;
        }

        throw new ThrowException(context, context.createTypeError("can only construct using functions"));
    }

    @Override
    public void visit(ExecutionContext context, NullLiteralExpression expr, boolean strict) {
        push(Types.NULL);
    }

    @Override
    public void visit(ExecutionContext context, NumberLiteralExpression expr, boolean strict) {
        String text = expr.getText();

        if (text.indexOf('.') == 0) {
            text = "0" + text;
            push(Double.valueOf(text));
            return;
        }

        if (text.indexOf('.') > 0) {
            push(Double.valueOf(text));
            return;
        }

        if (text.startsWith("0x") || text.startsWith("0X")) {
            text = text.substring(2);
            push(Long.valueOf(text, 16));
            return;
        }

        int eLoc = text.toLowerCase().indexOf('e');
        if (eLoc > 0) {

            String base = text.substring(0, eLoc);
            String exponent = text.substring(eLoc);

            String javafied = base + ".0" + exponent;

            push(Double.valueOf(javafied));
        } else {
            push(Long.valueOf(text, expr.getRadix()));
        }
    }

    @Override
    public void visit(ExecutionContext context, ObjectLiteralExpression expr, boolean strict) {
        DynObject obj = BuiltinObject.newObject(context);

        List<PropertyAssignment> assignments = expr.getPropertyAssignments();

        for (PropertyAssignment each : assignments) {
            each.accept(context, this, strict);
            String debugName = each.getName();
            Object ref = pop();
            if (ref instanceof Reference) {
                debugName = ((Reference) ref).getReferencedName();
            }
            Object value = getValue(context, ref);
            Object original = obj.getOwnProperty(context, each.getName());
            PropertyDescriptor desc = null;
            if (each instanceof PropertyGet) {
                desc = PropertyDescriptor.newPropertyDescriptorForObjectInitializerGet(original, debugName, (JSFunction) value);
            } else if (each instanceof PropertySet) {
                desc = PropertyDescriptor.newPropertyDescriptorForObjectInitializerSet(original, debugName, (JSFunction) value);
            } else {
                desc = PropertyDescriptor.newPropertyDescriptorForObjectInitializer(debugName, value);
            }
            obj.defineOwnProperty(context, each.getName(), desc, false);
        }

        push(obj);
    }

    @Override
    public void visit(ExecutionContext context, PostOpExpression expr, boolean strict) {
        expr.getExpr().accept(context, this, strict);
        Object lhs = pop();

        if (lhs instanceof Reference) {
            if (((Reference) lhs).isStrictReference()) {
                if (((Reference) lhs).getBase() instanceof EnvironmentRecord) {
                    if (((Reference) lhs).getReferencedName().equals("arguments") || ((Reference) lhs).getReferencedName().equals("eval")) {
                        throw new ThrowException(context, context.createSyntaxError("invalid assignment: " + ((Reference) lhs).getReferencedName()));
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

            ((Reference) lhs).putValue(context, newValue);
            push(oldValue);
        }
    }

    @Override
    public void visit(ExecutionContext context, PreOpExpression expr, boolean strict) {
        expr.getExpr().accept(context, this, strict);
        Object lhs = pop();

        if (lhs instanceof Reference) {
            if (((Reference) lhs).isStrictReference()) {
                if (((Reference) lhs).getBase() instanceof EnvironmentRecord) {
                    if (((Reference) lhs).getReferencedName().equals("arguments") || ((Reference) lhs).getReferencedName().equals("eval")) {
                        throw new ThrowException(context, context.createSyntaxError("invalid assignment: " + ((Reference) lhs).getReferencedName()));
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

            ((Reference) lhs).putValue(context, newValue);
            push(newValue);
        }
    }

    @Override
    public void visit(ExecutionContext context, PropertyGet propertyGet, boolean strict) {
        JSFunction compiledFn = context.getCompiler().compileFunction(context,
                null,
                new String[] {},
                propertyGet.getBlock(),
                strict);
        push(compiledFn);
    }

    @Override
    public void visit(ExecutionContext context, PropertySet propertySet, boolean strict) {
        JSFunction compiledFn = context.getCompiler().compileFunction(context,
                null,
                new String[] { propertySet.getIdentifier() },
                propertySet.getBlock(),
                strict);
        push(compiledFn);
    }

    @Override
    public void visit(ExecutionContext context, NamedValue namedValue, boolean strict) {
        namedValue.getExpr().accept(context, this, strict);
    }

    @Override
    public void visit(ExecutionContext context, RegexpLiteralExpression expr, boolean strict) {
        push(BuiltinRegExp.newRegExp(context, expr.getPattern(), expr.getFlags()));
    }

    @Override
    public void visit(ExecutionContext context, RelationalExpression expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        expr.getRhs().accept(context, this, strict);

        Object rval = getValue(context, pop());
        Object lval = getValue(context, pop());

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
    public void visit(ExecutionContext context, ReturnStatement statement, boolean strict) {
        if (statement.getExpr() != null) {
            statement.getExpr().accept(context, this, strict);
            Object value = pop();
            push(Completion.createReturn(getValue(context, value)));
        } else {
            push(Completion.createReturn(Types.UNDEFINED));
        }
    }

    @Override
    public void visit(ExecutionContext context, StrictEqualityOperatorExpression expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        expr.getRhs().accept(context, this, strict);

        Object rhs = getValue(context, pop());
        Object lhs = getValue(context, pop());

        if (expr.getOp().equals("===")) {
            push(Types.compareStrictEquality(context, lhs, rhs));
        } else {
            push(!Types.compareStrictEquality(context, lhs, rhs));
        }
    }

    @Override
    public void visit(ExecutionContext context, StringLiteralExpression expr, boolean strict) {
        push(expr.getLiteral());
    }

    @Override
    public void visit(ExecutionContext context, SwitchStatement statement, boolean strict) {
        statement.getExpr().accept(context, this, strict);
        Object value = getValue(context, pop());

        List<CaseClause> clauses = new ArrayList<CaseClause>(statement.getCaseClauses());
        if (statement.getDefaultCaseClause() != null) {
            clauses.add(statement.getDefaultCaseClause());
        }

        Object v = null;

        boolean matched = false;
        for (CaseClause each : clauses) {
            if (each instanceof DefaultCaseClause) {
                matched = true;
            } else {
                each.getExpression().accept(context, this, strict);
                Object caseTest = pop();
                if (Types.compareStrictEquality(context, value, getValue(context, caseTest))) {
                    matched = true;
                }
            }

            if (matched) {
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
    public void visit(ExecutionContext context, TernaryExpression expr, boolean strict) {
        expr.getTest().accept(context, this, strict);
        if (Types.toBoolean(getValue(context, pop()))) {
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
        throw new ThrowException(context, getValue(context, pop()));
    }

    @Override
    public void visit(ExecutionContext context, TryStatement statement, boolean strict) {
        Completion b = null;
        try {
            // statement.getTryBlock().accept(context, this, strict);
            // b = (Completion) pop();
            b = invokeCompiledBlockStatement(context, "Try", statement.getTryBlock());
            push(b);
        } catch (ThrowException e) {
            if (statement.getCatchClause() != null) {
                // BasicBlock catchBlock = new InterpretedStatement(statement.getCatchClause().getBlock(), strict);
                BasicBlock catchBlock = compiledBlockStatement(context, "Catch", statement.getCatchClause().getBlock());
                try {
                    b = context.executeCatch(catchBlock, statement.getCatchClause().getIdentifier(), e.getValue());
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

        if (statement.getFinallyBlock() != null) {
            Completion f = invokeCompiledBlockStatement(context, "Finally", statement.getFinallyBlock());
            if (f.type == Completion.Type.NORMAL) {
                push(b);
            } else {
                push(f);
            }
        }

    }

    @Override
    public void visit(ExecutionContext context, TypeOfOpExpression expr, boolean strict) {
        expr.getExpr().accept(context, this, strict);
        push(Types.typeof(context, pop()));
    }

    @Override
    public void visit(ExecutionContext context, UnaryMinusExpression expr, boolean strict) {
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
    public void visit(ExecutionContext context, UnaryPlusExpression expr, boolean strict) {
        expr.getExpr().accept(context, this, strict);
        push(Types.toNumber(context, getValue(context, pop())));
    }

    @Override
    public void visit(ExecutionContext context, VariableDeclaration expr, boolean strict) {
        if (expr.getExpr() != null) {
            expr.getExpr().accept(context, this, strict);
            Object value = getValue(context, pop());
            Reference var = context.resolve(expr.getIdentifier());
            var.putValue(context, value);
        }
        push(expr.getIdentifier());
    }

    @Override
    public void visit(ExecutionContext context, VariableStatement statement, boolean strict) {
        for (VariableDeclaration each : statement.getVariableDeclarations()) {
            each.accept(context, this, strict);
            pop();
        }

        push(Completion.createNormal(Types.UNDEFINED));
    }

    @Override
    public void visit(ExecutionContext context, VoidOperatorExpression expr, boolean strict) {
        expr.getExpr().accept(context, this, strict);
        pop();
        push(Types.UNDEFINED);
    }

    @Override
    public void visit(ExecutionContext context, WhileStatement statement, boolean strict) {
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
    public void visit(ExecutionContext context, WithStatement statement, boolean strict) {
        statement.getExpr().accept(context, this, strict);
        JSObject obj = Types.toObject(context, getValue(context, pop()));
        BasicBlock block = compiledBlockStatement(context, "With", statement.getBlock());
        push(context.executeWith(obj, block));
    }

    protected BasicBlock compiledBlockStatement(ExecutionContext context, String grist, Statement statement) {
        Entry entry = this.blockManager.retrieve(statement.getStatementNumber());
        if (entry.getCompiled() == null) {
            BasicBlock compiledBlock = context.getCompiler().compileBasicBlock(context, grist, statement, context.isStrict());
            entry.setCompiled(compiledBlock);
        }
        return (BasicBlock) entry.getCompiled();
    }

    protected Completion invokeCompiledBlockStatement(ExecutionContext context, String grist, Statement statement) {
        BasicBlock block = compiledBlockStatement(context, grist, statement);
        return block.call(context);
    }
    
    protected Object getValue(ExecutionContext context, Object obj) {
        return Types.getValue( context, obj );
    }

}
