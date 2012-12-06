package org.dynjs.parser.ast;

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
        if ( this.lhs == null ) {
            System.err.println( "NULL: " + this.getClass() );
        }
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
    
    public String dump(String indent) {
        return super.dump( indent ) + this.lhs.dump( indent + "  " ) + "\n"+ this.rhs.dump( indent + "  " );
    }

    public String toString() {
        return this.lhs + " " + this.op + " " + this.rhs;
    }

}
