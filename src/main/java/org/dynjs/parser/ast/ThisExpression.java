package org.dynjs.parser.ast;

import static me.qmx.jitescript.util.CodegenUtils.*;
import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSObject;

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

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                aload(JSCompiler.Arities.EXECUTION_CONTEXT);
                invokevirtual(p(ExecutionContext.class), "getThisBinding", sig(Object.class));
                // obj
            }
        };
    }
    
    public String toString() {
        return "this";
    }
}
