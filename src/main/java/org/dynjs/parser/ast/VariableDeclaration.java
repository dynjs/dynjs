package org.dynjs.parser.ast;

import org.dynjs.exception.ThrowException;
import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.linker.DynJSBootstrapper;

import java.lang.invoke.CallSite;
import java.util.ArrayList;
import java.util.List;

public class VariableDeclaration {
    public static final List<VariableDeclaration> EMPTY_LIST = new ArrayList<>();

    private Position position;
    private String identifier;
    private Expression expr;
    private CallSite get;

    public VariableDeclaration(Position position, String identifier, Expression initializerExpr) {
        this.position = position;
        this.identifier = identifier;
        this.expr = initializerExpr;
        if (this.expr != null) {
            this.get = DynJSBootstrapper.factory().createGet(initializerExpr.getPosition());
        }
    }

    public Position getPosition() {
        return this.position;
    }

    public Expression getExpr() {
        return this.expr;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String dump(String indent) {
        StringBuilder buf = new StringBuilder();

        buf.append(indent + getClass().getSimpleName() + "(" + this.identifier + ")\n");
        if (this.expr != null) {
            buf.append(this.expr.dump(indent + "  "));
        }

        return buf.toString();
    }

    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit(context, this, strict);
    }

    public String interpret(ExecutionContext context, boolean debug) {
        if (this.expr != null) {
            Object value = getValue(this.get, context, this.expr.interpret(context, debug));
            Reference var = context.resolve(getIdentifier());
            var.putValue(context, value);
        }
        return (getIdentifier());

    }

    public int getSizeMetric() {
        if (this.expr != null) {
            return this.expr.getSizeMetric() + 3;
        }
        return 3;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(this.identifier);
        if (this.expr != null) {
            buf.append(" = ");
            buf.append(this.expr);
        }
        return buf.toString();

    }

    protected Object getValue(CallSite callSite, ExecutionContext context, Object obj) {
        if (obj instanceof Reference) {
            Reference ref = (Reference) obj;
            String name = ref.getReferencedName();
            try {
                Object result = callSite.getTarget().invoke(obj, context, name);
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
