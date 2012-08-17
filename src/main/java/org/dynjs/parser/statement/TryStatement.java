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
    private final Statement tryBlock;
    private final CatchClause catchClause;
    private final Statement finallyBlock;

    public TryStatement(Tree tree, BlockManager blockManager, Statement tryBlock, CatchClause catchClause, Statement finallyBlock) {
        super(tree, blockManager);
        this.tryBlock = tryBlock;
        this.catchClause = catchClause;
        this.finallyBlock = finallyBlock;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                LabelNode doFinally = new LabelNode();
                LabelNode doCatch = new LabelNode();
                LabelNode end = new LabelNode();

                append(CodeBlockUtils.invokeCompiledStatementBlock(getBlockManager(), "Try", tryBlock));
                // completion(try)

                dup();
                // completion(try) completion(try)
                append(handleCompletion(doFinally, doFinally, doFinally, doFinally, doCatch));

                
                // ----------------------------------------
                // Catch
                label(doCatch);
                // completion(try)
                
                if( catchClause != null ) {
                    append( jsCompletionValue() );
                    // thrown
                    aload( JSCompiler.Arities.EXECUTION_CONTEXT );
                    // thrown context
                    swap();
                    // context thrown
                    append( CodeBlockUtils.compiledStatementBlock(getBlockManager(), "Catch", catchClause ));
                    // context thrown catchblock
                    swap();
                    // context catchblock thrown
                    ldc( catchClause.getIdentifier() );
                    // context catchblock thrown ident
                    swap();
                    // context catchblock ident thrown
                    invokevirtual(p(ExecutionContext.class), "executeCatch", sig(Completion.class, BasicBlock.class, String.class, Object.class));
                    // completion(catch)
                } else {
                    nop();
                }

                // ----------------------------------------
                // Finally

                label(doFinally);
                // completion(try)

                if (finallyBlock != null) {
                    append(CodeBlockUtils.invokeCompiledStatementBlock(getBlockManager(), "Finally", finallyBlock));
                    // completion(try) completion(finally)
                    dup();
                    // completion(try) completion(finally) completion(finally)
                    getfield(p(Completion.class), "type", ci(Completion.Type.class));
                    // completion(try) completion(finally) type(finally)
                    getstatic(p(Completion.Type.class), "NORMAL", ci(Completion.Type.class));
                    // completion(try) completion(finally) type(finally) NORMAL
                    
                    LabelNode returnTryCompletion = new LabelNode();
                    
                    if_acmpeq( returnTryCompletion );
                    // completion(try) completion(finally) 
                    swap();
                    // completion(finally) completion(try) 
                    pop();
                    // completion(finally)
                    go_to(end);
                    
                    label( returnTryCompletion );
                    // completion(try) completion(finally) 
                    pop();
                    // completion(try)
                }

                label(end);
                // completion(try)

                nop();
            }

        };
    }
}