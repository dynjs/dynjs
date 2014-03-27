package org.dynjs.parser.ast;

import java.util.ArrayList;
import java.util.List;
import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.ExecutionContext;

public class VariableDeclaration {
    public static final List<VariableDeclaration> EMPTY_LIST = new ArrayList<>();

    private Position position;
    private String identifier;
    private Expression expr;

    public VariableDeclaration(Position position, String identifier, Expression initializerExpr) {
        this.position = position;
        this.identifier = identifier;
        this.expr = initializerExpr;
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

        buf.append(indent + "var " + this.identifier + "\n");
        if (this.expr != null) {
            buf.append(this.expr.dump(indent + "  "));
        }

        return buf.toString();
    }

    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit(context, this, strict);
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

}
