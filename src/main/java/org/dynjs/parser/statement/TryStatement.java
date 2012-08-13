package org.dynjs.parser.statement;

import static me.qmx.jitescript.util.CodegenUtils.*;
import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.compiler.CodeBlockUtils;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.BlockManager;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.ExecutionContext;
import org.objectweb.asm.tree.LabelNode;

public class TryStatement extends AbstractCompilingStatement implements Statement {
    private final BlockStatement tryBlock;
    private final CatchClause catchClause;
    private final BlockStatement finallyBlock;

    public TryStatement(Tree tree, BlockManager blockManager, BlockStatement tryBlock, CatchClause catchClause, BlockStatement finallyBlock) {
        super( tree, blockManager );
        this.tryBlock = tryBlock;
        this.catchClause = catchClause;
        this.finallyBlock = finallyBlock;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                LabelNode normalTarget = new LabelNode();
                LabelNode throwTarget = new LabelNode();
                LabelNode end = new LabelNode();

                append( CodeBlockUtils.invokeCompiledStatementBlock( getBlockManager(), "Try", tryBlock ) );
                // completion(try)

                dup();
                // completion(try) completion(try)

                Completion.handle( normalTarget, normalTarget, normalTarget, normalTarget, throwTarget );

                // ----------------------------------------
                // THROW
                label( throwTarget );
                // completion(try)

                if (catchClause != null) {
                    aload( JSCompiler.Arities.EXECUTION_CONTEXT );
                    // completion(try) context
                    swap();
                    // context completion(try)
                    append( CodeBlockUtils.compiledStatementBlock( getBlockManager(), "Catch", catchClause.getBlock() ) );
                    // context completion(try) block(catch)
                    swap();
                    // context block(catch) completion(try)
                    ldc( catchClause.getIdentifier() );
                    // context block(catch) completion(try) identifier
                    swap();
                    // context block(catch) identifier completion(try)
                    getfield( p( Completion.class ), "value", ci( Object.class ) );
                    // context block(catch) identifier thrown
                    invokevirtual( p( ExecutionContext.class ), "invokeCatch", sig( Completion.class, BasicBlock.class, String.class, Object.class ) );
                    // completion(catch)
                } else {
                    areturn();
                }

                // ----------------------------------------
                // NORMAL, BREAK, CONTINUE, RETURN

                label( normalTarget );
                // completion(try)

                if (finallyBlock != null) {
                    append( CodeBlockUtils.invokeCompiledStatementBlock( getBlockManager(), "Finally", finallyBlock ) );
                    // completion(try) completion(finally)
                    append( CodeBlockUtils.ifCompletionIsNormal( end ) );
                    // completion(try)
                }

                label( end );
                // completion(try) completion(finally)
            }

        };
    }
}