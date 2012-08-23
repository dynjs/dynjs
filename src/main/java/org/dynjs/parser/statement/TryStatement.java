package org.dynjs.parser.statement;

import static me.qmx.jitescript.util.CodegenUtils.*;
import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.compiler.CodeBlockUtils;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.exception.ThrowException;
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
                LabelNode end = new LabelNode();

                LabelNode tryStart = new LabelNode();
                LabelNode tryEnd = new LabelNode();

                LabelNode outerCatchHandler = new LabelNode();

                label(tryStart);
                append(CodeBlockUtils.invokeCompiledStatementBlock(getBlockManager(), "Try", tryBlock));
                label(tryEnd);
                // completion(try)

                if (finallyBlock != null) {
                    // completion(try)
                    append(CodeBlockUtils.invokeCompiledStatementBlock(getBlockManager(), "Finally", finallyBlock));
                    // completion(try) completion(finally)
                    dup();
                    // completion(try) completion(finally) completion(finally)
                    getfield(p(Completion.class), "type", ci(Completion.Type.class));
                    // completion(try) completion(finally) type(finally)
                    getstatic(p(Completion.Type.class), "NORMAL", ci(Completion.Type.class));
                    // completion(try) completion(finally) type(finally) NORMAL

                    LabelNode normalFinally = new LabelNode();
                    if_acmpeq(normalFinally);

                    // ----------------------------------------
                    // Abnormal

                    // completion(try) completion(finally)
                    swap();
                    // completion(finally) completion(try)
                    pop();
                    // completion(finally)
                    go_to(end);

                    // ----------------------------------------
                    // Normal

                    label(normalFinally);
                    // completion(try) completion(finally)
                    pop();
                    // completion(try)
                }

                go_to(end);

                trycatch(tryStart, tryEnd, outerCatchHandler, p(ThrowException.class));

                if (catchClause != null) {

                    LabelNode catchCatchHandler = new LabelNode();
                    LabelNode catchStart = new LabelNode();
                    LabelNode catchEnd = new LabelNode();

                    label(outerCatchHandler);
                    // ex
                    invokevirtual(p(ThrowException.class), "getValue", sig(Object.class));
                    // thrown
                    aload(JSCompiler.Arities.EXECUTION_CONTEXT);
                    // thrown context
                    swap();
                    // context thrown
                    append(CodeBlockUtils.compiledStatementBlock(getBlockManager(), "Catch", catchClause));
                    // context thrown catchblock
                    swap();
                    // context catchblock thrown
                    ldc(catchClause.getIdentifier());
                    // context catchblock thrown ident
                    swap();
                    // context catchblock ident thrown
                    label(catchStart);
                    invokevirtual(p(ExecutionContext.class), "executeCatch", sig(Completion.class, BasicBlock.class, String.class, Object.class));
                    label(catchEnd);
                    // completion(catch)

                    if (finallyBlock != null) {
                        // completion(catch)
                        append(CodeBlockUtils.invokeCompiledStatementBlock(getBlockManager(), "Finally", finallyBlock));
                        // completion(catch) completion(finally)
                        dup();
                        // completion(catch) completion(finally) completion(finally)
                        getfield(p(Completion.class), "type", ci(Completion.Type.class));
                        // completion(catch) completion(finally) type(finally)
                        getstatic(p(Completion.Type.class), "NORMAL", ci(Completion.Type.class));
                        // completion(catch) completion(finally) type(finally) NORMAL

                        LabelNode normalFinally = new LabelNode();
                        if_acmpeq(normalFinally);

                        // ----------------------------------------
                        // Abnormal

                        // completion(catch) completion(finally)
                        swap();
                        // completion(finally) completion(catch)
                        pop();
                        // completion(finally)
                        go_to(end);

                        // ----------------------------------------
                        // Normal

                        label(normalFinally);
                        // completion(catch) completion(finally)
                        pop();
                        // completion(catch)
                        go_to(end);

                        // ----------------------------------------
                        // IN CASE CATCH ITSELF THROWS
                        // ----------------------------------------
                        
                        LabelNode normalFinallyAfterThrow = new LabelNode();

                        trycatch(catchStart, catchEnd, catchCatchHandler, null );
                        label(catchCatchHandler);
                        // ex
                        
                        append(CodeBlockUtils.invokeCompiledStatementBlock(getBlockManager(), "Finally", finallyBlock));
                        // ex completion(finally)
                        dup();
                        // ex completion(finally) completion(finally)
                        getfield(p(Completion.class), "type", ci(Completion.Type.class));
                        // ex completion(finally) type(finally)
                        getstatic(p(Completion.Type.class), "NORMAL", ci(Completion.Type.class));
                        // ex completion(finally) type(finally) NORMAL
                        if_acmpeq(normalFinallyAfterThrow);
                        // ex completion(finally)
                        swap();
                        // completion(finally) ex
                        pop();
                        // completion(finally)
                        go_to( end );
                        
                        label(normalFinallyAfterThrow);
                        // ex completion(finally)
                        pop();
                        // ex
                        athrow();
                    }
                } else {
                    // No catch, but thrown
                    label(outerCatchHandler);
                    // ex
                    if (finallyBlock != null) {
                        // ex
                        append(CodeBlockUtils.invokeCompiledStatementBlock(getBlockManager(), "Finally", finallyBlock));
                        // ex completion(finally)
                        dup();
                        // ex completion(finally) completion(finally)
                        getfield(p(Completion.class), "type", ci(Completion.Type.class));
                        // ex completion(finally) type(finally)
                        getstatic(p(Completion.Type.class), "NORMAL", ci(Completion.Type.class));
                        // ex completion(finally) type(finally) NORMAL

                        LabelNode normalFinally = new LabelNode();
                        if_acmpeq(normalFinally);

                        // ----------------------------------------
                        // Abnormal

                        // ex completion(finally)
                        swap();
                        // completion(finally) ex
                        pop();
                        // completion(finally)
                        go_to(end);

                        // ----------------------------------------
                        // Normal

                        label(normalFinally);
                        // ex completion(finally)
                        pop();
                        // ex
                        athrow();
                        // <THROWN>
                    }

                }

                label(end);
                // completion
                nop();
            }

        };
    }

    protected CodeBlock getFinallyBlock() {
        if (finallyBlock == null) {
            // IN: x
            // OUT: x
            return new CodeBlock();
        }

        return CodeBlockUtils.invokeCompiledStatementBlock(getBlockManager(), "Finally", finallyBlock);
    }

}