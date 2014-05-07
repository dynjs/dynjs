package org.dynjs.parser.ast;

import java.util.ArrayList;
import java.util.List;

import org.dynjs.parser.js.Position;

public abstract class AbstractBinaryExpression extends AbstractExpression {

    private Expression lhs;
    private Expression rhs;
    private String op;

    AbstractBinaryExpression(final Expression lhs, final Expression rhs, String op) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.op = op;
    }

    public Position getPosition() {
        return this.lhs.getPosition();
    }

    public Expression getLhs() {
        return this.lhs;
    }

    public Expression getRhs() {
        return this.rhs;
    }

    public String getOp() {
        return this.op;
    }

    public List<FunctionDeclaration> getFunctionDeclarations() {
        List<FunctionDeclaration> decls = new ArrayList<>();
        decls.addAll(this.lhs.getFunctionDeclarations());
        decls.addAll(this.rhs.getFunctionDeclarations());
        return decls;
    }

    public String dump(String indent) {
        return super.dump(indent) + this.lhs.dump(indent + "  ") + this.rhs.dump(indent + "  ");
    }

    public String dumpData() {
        return this.op;
    }

    public String toString() {
        return this.lhs + " " + this.op + " " + this.rhs;
    }

    public int getSizeMetric() {
        return this.lhs.getSizeMetric() + this.rhs.getSizeMetric() + 1;
    }

}
