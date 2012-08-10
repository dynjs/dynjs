package org.dynjs.parser.statement;

import static me.qmx.jitescript.util.CodegenUtils.*;
import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Reference;

public class VariableDeclarationExpression extends AbstractExpression {

    private String identifier;
    private Expression initializer;

    public VariableDeclarationExpression(Tree tree, String identifier, Expression initializer) {
        super( tree );
        this.identifier = identifier;
        this.initializer = initializer;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                // 12.2
                if (initializer == null) {
                    ldc( identifier );
                } else {
                    append( jsResolve( identifier ) );
                    // reference
                    aload( JSCompiler.Arities.EXECUTION_CONTEXT );
                    // reference context
                    append( initializer.getCodeBlock() );
                    // reference context val
                    append( jsGetValue() );
                    // reference context val
                    invokevirtual( p( Reference.class ), "putValue", sig( void.class, ExecutionContext.class, Object.class ) );
                    ldc( identifier );
                }
            }
        };
    }

}
