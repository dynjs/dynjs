package org.dynjs.parser.ast;

import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.CodeVisitor;
import org.dynjs.runtime.ExecutionContext;

public class VariableDeclaration extends AbstractExpression {

    private String identifier;
    private Expression expr;

    public VariableDeclaration(Tree tree, String identifier, Expression initializerExpr) {
        super(tree);
        this.identifier = identifier;
        this.expr = initializerExpr;
    }
    
    public Expression getExpr() {
        return this.expr;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String dump(String indent) {
        StringBuffer buf = new StringBuffer();

        buf.append(indent + "var " + this.identifier + "\n");
        if (this.expr != null) {
            buf.append(this.expr.dump(indent + "  "));
        }

        return buf.toString();
    }

    @Override
    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit( context, this, strict );
    }

}
