package org.dynjs.parser.statement;

public class NamedValue {
    
    private String name;
    private Expression expr;

    public NamedValue(String name, Expression expr) {
        this.name = name;
        this.expr = expr;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Expression getExpr() {
        return this.expr;
    }

}
