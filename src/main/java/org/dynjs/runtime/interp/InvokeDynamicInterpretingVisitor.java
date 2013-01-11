package org.dynjs.runtime.interp;

import java.util.List;

import org.dynjs.exception.ThrowException;
import org.dynjs.parser.ast.Expression;
import org.dynjs.parser.ast.FunctionCallExpression;
import org.dynjs.parser.ast.NewOperatorExpression;
import org.dynjs.runtime.BlockManager;
import org.dynjs.runtime.EnvironmentRecord;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.linker.DynJSBootstrapper;

public class InvokeDynamicInterpretingVisitor extends BasicInterpretingVisitor {

    public InvokeDynamicInterpretingVisitor(BlockManager blockManager) {
        super( blockManager );
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

        Object thisValue = null;

        if (ref instanceof Reference) {
            if (((Reference) ref).isPropertyReference()) {
                thisValue = ((Reference) ref).getBase();
            } else {
                thisValue = ((EnvironmentRecord) ((Reference) ref).getBase()).implicitThisValue();
            }
        }

        if (thisValue == null) {
            thisValue = Types.UNDEFINED;
        }
        try {
            push(DynJSBootstrapper.getInvokeHandler().call(function, context, thisValue, args));
        } catch (ThrowException e) {
            throw e;
        } catch (Throwable e) {
            throw new ThrowException(context, e);
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

        try {
            push(DynJSBootstrapper.getInvokeHandler().construct(memberExpr, context, args));
        } catch (ThrowException e) {
            throw e;
        } catch (Throwable e) {
            throw new ThrowException(context, e);
        }
        return;
    }

    protected Object getValue(ExecutionContext context, Object obj) {
        if (obj instanceof Reference) {
            String name = ((Reference) obj).getReferencedName();

            try {
                return DynJSBootstrapper.getInvokeHandler().get(obj, context, name);
            } catch (ThrowException e) {
                throw e;
            } catch (Throwable e) {
                throw new ThrowException(context, e);
            }
        } else {
            return obj;
        }

    }

}
