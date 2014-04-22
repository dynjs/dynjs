package org.dynjs.runtime.interp;

import java.util.List;

import org.dynjs.codegen.DereferencedReference;
import org.dynjs.exception.ThrowException;
import org.dynjs.parser.ast.AssignmentExpression;
import org.dynjs.parser.ast.Expression;
import org.dynjs.parser.ast.FunctionCallExpression;
import org.dynjs.parser.ast.NewOperatorExpression;
import org.dynjs.runtime.*;
import org.dynjs.runtime.linker.DynJSBootstrapper;

public class InvokeDynamicInterpretingVisitor extends BasicInterpretingVisitor {

    public InvokeDynamicInterpretingVisitor(BlockManager blockManager) {
        super(blockManager);
    }

    @Override
    public Object visit(Object context, AssignmentExpression expr, boolean strict) {
        
        Object lhs = expr.getLhs().accept(context, this, strict);
        if (!(lhs instanceof Reference)) {
            throw new ThrowException((ExecutionContext) context, ((ExecutionContext) context).createReferenceError(expr.getLhs() + " is not a reference"));
        }

        Reference lhsRef = (Reference) lhs;
        Object rhs = getValue(context, expr.getRhs().accept(context, this, strict));

        if (lhsRef.isUnresolvableReference() && strict) {
            throw new ThrowException((ExecutionContext) context, ((ExecutionContext) context).createReferenceError(lhsRef.getReferencedName() + " is not defined"));
        }

        try {
            DynJSBootstrapper.getInvokeHandler().set(lhsRef, (ExecutionContext) context, lhsRef.getReferencedName(), rhs);
        } catch (ThrowException e) {
            throw e;
        } catch (Throwable e) {
            throw new ThrowException((ExecutionContext) context, e);
        }
        return(rhs);

        // lhsRef.putValue(context, rhs);
        // invokedynamic("dyn:setProperty", sig(void.class, Reference.class, ExecutionContext.class, String.class, Object.class), DynJSBootstrapper.HANDLE,
        // DynJSBootstrapper.ARGS);
    }

    @Override
    public Object visit(Object context, FunctionCallExpression expr, boolean strict) {
        Object ref = expr.getMemberExpression().accept(context, this, strict);
        Object function = getValue(context, ref);

        List<Expression> argExprs = expr.getArgumentExpressions();

        Object[] args = new Object[argExprs.size()];
        int i = 0;

        for (Expression each : argExprs) {

            Object value = getValue(context,each.accept(context, this, strict));
            args[i] = value;
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

        if (ref instanceof Reference) {
            function = new DereferencedReference((Reference) ref, function);
        }

        try {
            return(DynJSBootstrapper.getInvokeHandler().call(function, (ExecutionContext) context, thisValue, args));
        } catch (ThrowException e) {
            throw e;
        } catch (NoSuchMethodError e) {
            throw new ThrowException((ExecutionContext) context, ((ExecutionContext) context).createTypeError("not callable: " + function.toString()));
        } catch (Throwable e) {
            throw new ThrowException((ExecutionContext) context, e);
        }
    }

    @Override
    public Object visit(Object context, NewOperatorExpression expr, boolean strict) {
        Object ref = expr.getExpr().accept(context, this, strict);
        Object memberExpr = getValue(context, ref);
        Object[] args = new Object[expr.getArgumentExpressions().size()];

        int i = 0;

        for (Expression each : expr.getArgumentExpressions()) {
            args[i] = getValue(context, each.accept(context, this, strict));
            ++i;
        }

        Object ctor = memberExpr;

        if ( ref instanceof Reference && ctor instanceof JSFunction) {
            ctor = new DereferencedReference((Reference) ref, ctor);
        }

        try {
            return( DynJSBootstrapper.getInvokeHandler().construct(ctor, (ExecutionContext) context, args) );
        } catch (NoSuchMethodError e) {
            throw new ThrowException((ExecutionContext) context, ((ExecutionContext) context).createTypeError("cannot construct with: " + ref));
        } catch (ThrowException e) {
            throw e;
        } catch (Throwable e) {
            throw new ThrowException((ExecutionContext) context, e);
        }
    }

    protected Object getValue(ExecutionContext context, Object obj) {
        if (obj instanceof Reference) {
            Reference ref = (Reference) obj;
            String name = ref.getReferencedName();
            try {
                Object result = DynJSBootstrapper.getInvokeHandler().get(obj, context, name);
                return result;
            } catch (ThrowException e) {
                throw e;
            } catch (NoSuchMethodError e) {
                if (ref.isPropertyReference() && !ref.isUnresolvableReference()) {
                    return Types.UNDEFINED;
                }
                throw new ThrowException(context, context.createReferenceError("unable to reference: " + name));
            } catch (Throwable e) {
                throw new ThrowException(context, e);
            }
        } else {
            return obj;
        }

    }
}
