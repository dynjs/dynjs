package org.dynjs.parser.ast;

import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.CodeVisitor;
import org.dynjs.runtime.ExecutionContext;

/**
 * The <code>this</code> keyword evaluates to the value of the ThisBinding of
 * the current execution context.
 * 
 * @see 11.1.1
 * @author Douglas Campos
 * @author Bob McWhirter
 * 
 */
public class ThisExpression extends AbstractExpression {
    public ThisExpression(Tree tree) {
        super(tree);
    }

    public String toString() {
        return "this";
    }

    @Override
    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit( context, this, strict );
    }
}
