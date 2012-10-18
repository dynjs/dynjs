package org.dynjs.parser.ast;

import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

import java.util.HashSet;
import java.util.Set;

import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.exception.ThrowException;
import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.VerifierUtils;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Reference;

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

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                // 12.2
                if (expr == null) {
                    ldc(identifier);
                    // str
                } else {
                    append(jsResolve(identifier));
                    // reference
                    aload(JSCompiler.Arities.EXECUTION_CONTEXT);
                    // reference context
                    append(expr.getCodeBlock());
                    // reference context val
                    append(jsGetValue());
                    // reference context val
                    invokevirtual(p(Reference.class), "putValue", sig(void.class, ExecutionContext.class, Object.class));
                    // reference
                    ldc(identifier);
                    // str
                }
            }
        };
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
