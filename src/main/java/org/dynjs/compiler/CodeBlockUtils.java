package org.dynjs.compiler;

import static me.qmx.jitescript.util.CodegenUtils.*;

import javax.annotation.processing.Completion;

import me.qmx.jitescript.CodeBlock;

import org.dynjs.parser.Position;
import org.dynjs.parser.Statement;
import org.dynjs.parser.statement.BlockStatement;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.BlockManager;
import org.dynjs.runtime.BlockManager.Entry;
import org.dynjs.runtime.Completion.Type;
import org.dynjs.runtime.ExecutionContext;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.VarInsnNode;

public class CodeBlockUtils {

    public static CodeBlock relocateLocalVars(CodeBlock block, int offset) {

        InsnList list = block.getInstructionList();
        int len = list.size();

        for (int i = 0; i < len; ++i) {
            AbstractInsnNode each = list.get( i );
            if (each instanceof VarInsnNode) {
                VarInsnNode node = (VarInsnNode) each;
                if (node.var > 3) {
                    node.var = node.var + offset;
                }
            }
        }

        return block;
    }

    public static void injectLineNumber(CodeBlock block, Statement statement) {
        Position position = statement.getPosition();
        if (position != null) {
            LabelNode lineLabel = new LabelNode();
            block.line( position.getLine(), lineLabel );
            block.label( lineLabel );
        }
    }

    public static CodeBlock handleCompletion() {
        return new CodeBlock() {
            {
                LabelNode completeReturn = new LabelNode();
                LabelNode completeNormal = new LabelNode();

                // INCOMING
                // completion

                dup();
                // completion completion
                getfield( p( Completion.class ), "type", sig( Type.class ) );
                // completion type
                getstatic( p( Type.class ), "NORMAL", sig( Type.class ) );
                // completion type NORMAL
                ifeq( completeNormal );
                // completion

                dup();
                // completion completion
                getfield( p( Completion.class ), "type", sig( Type.class ) );
                // completion type
                getstatic( p( Type.class ), "RETURN", sig( Type.class ) );
                // completion type NORMAL
                ifeq( completeReturn );
                // completion

                // ----------------------------------------
                // RETURN
                label( completeReturn );
                // completion
                areturn();

                // ----------------------------------------
                // NORMAL
                label( completeNormal );
                // completion
                getfield( p( Completion.class ), "value", sig( Object.class ) );
                // value

            }
        };
    }

    public static CodeBlock invokeCompiledBasicBlock(final BlockManager blockManager, final String grist, final BlockStatement block, final boolean forceReturn) {
        return new CodeBlock() {
            {
                append( compiledBasicBlock( blockManager, grist, block, forceReturn ) );
                // basic-block
                aload( JSCompiler.Arities.EXECUTION_CONTEXT );
                // basic-block context
                aload( JSCompiler.Arities.SELF );
                // basic-block context self
                invokevirtual( p( BasicBlock.class ), "invoke", sig( Object.class, ExecutionContext.class, Object.class ) );
                // completion
            }
        };
    }

    public static CodeBlock compiledBasicBlock(final BlockManager blockManager, final String grist, final BlockStatement block, final boolean forceReturn) {
        return new CodeBlock() {
            {
                LabelNode skipCompile = new LabelNode();
                LabelNode end = new LabelNode();

                int statementNumber = block.getStatementNumber();
                Entry entry = blockManager.retrieve( statementNumber );

                // Stash statement if required
                if (entry.statement == null) {
                    entry.statement = block;
                }

                // ----------------------------------------
                // ----------------------------------------

                aload( JSCompiler.Arities.EXECUTION_CONTEXT );
                ldc( statementNumber );
                invokevirtual( p( ExecutionContext.class ), "retrieveBasicBlock", sig( Entry.class, int.class ) );
                dup();
                // entry entry
                invokevirtual( p( Entry.class ), "getCompiled", sig( Object.class ) );
                // entry object
                dup();
                // entry object object

                ifnonnull( skipCompile );
                // entry object
                pop();
                // entry

                aload( JSCompiler.Arities.EXECUTION_CONTEXT );
                // entry context

                invokevirtual( p( ExecutionContext.class ), "getCompiler", sig( JSCompiler.class ) );
                // entry compiler

                swap();
                // compiler entry

                ldc( grist );
                // compiler entry grist

                swap();
                // compiler grist entry

                getfield( p( Entry.class ), "statement", ci( Statement.class ) );
                // compiler grist statement

                invokevirtual( p( JSCompiler.class ), "compileBasicBlock", sig( BasicBlock.class, String.class, Statement.class ) );
                // basic-block

                dup();
                // basic-block basic-block

                aload( JSCompiler.Arities.EXECUTION_CONTEXT );
                // basic-block basic-block context

                ldc( statementNumber );
                // basic-block basic-block context statement-number

                invokevirtual( p( ExecutionContext.class ), "retrieveBasicBlock", sig( Entry.class, int.class ) );
                // basic-block basic-block entry

                swap();
                // basic-block entry basic-block

                invokevirtual( p( Entry.class ), "setCompiled", sig( void.class, Object.class ) );
                // basic-block

                go_to( end );

                label( skipCompile );
                // entry basic-block
                swap();
                // basic-block entry
                pop();
                // basic-block

                label( end );
                // basic-block

            }
        };

    }

    public static CodeBlock normalCompletion() {
        return new CodeBlock() {
            {
                invokestatic( p( Completion.class ), "createNormal", sig( Completion.class ) );
                areturn();
            }
        };
    }

    public static CodeBlock normalCompletionWithValue() {
        return new CodeBlock() {
            {
                invokestatic( p( Completion.class ), "createNormal", sig( Completion.class, Object.class ) );
                areturn();
            }
        };
    }

    public static CodeBlock ifCompletionIsNormal(final LabelNode jumpTarget) {
        return new CodeBlock() {
            {
                // IN
                // completion
                getfield( p( Completion.class ), "type", sig( Type.class ) );
                // type
                getstatic( p( Type.class ), "NORMAL", sig( Type.class ) );
                if_acmpeq( jumpTarget );

            }
        };
    }

    public static CodeBlock ifCompletionIsBreak(final LabelNode jumpTarget) {
        return new CodeBlock() {
            {
                // IN
                // completion
                getfield( p( Completion.class ), "type", sig( Type.class ) );
                // type
                getstatic( p( Type.class ), "BREAK", sig( Type.class ) );
                if_acmpeq( jumpTarget );

            }
        };
    }

    public static CodeBlock ifCompletionIsContinue(final LabelNode jumpTarget) {
        return new CodeBlock() {
            {
                // IN
                // completion
                getfield( p( Completion.class ), "type", sig( Type.class ) );
                // type
                getstatic( p( Type.class ), "CONTINUE", sig( Type.class ) );
                if_acmpeq( jumpTarget );
            }
        };
    }

    public static CodeBlock ifCompletionIsTrue(final LabelNode jumpTarget) {
        return new CodeBlock() {
            {
                LabelNode abrupt = new LabelNode();
                // IN
                // completion
                dup();
                // completion completion
                getfield( p( Completion.class ), "type", sig( Type.class ) );
                // completion type
                getstatic( p( Type.class ), "NORMAL", sig( Type.class ) );
                // completion type NORMAL
                if_acmpne( abrupt );
                // completion
                getfield( p( Completion.class ), "value", sig( Type.class ) );
                // value
                iftrue( jumpTarget );
                label( abrupt );
            }
        };
    }

    public static CodeBlock ifCompletionIsFalse(final LabelNode jumpTarget) {
        return new CodeBlock() {
            {
                LabelNode abrupt = new LabelNode();
                // IN
                // completion
                dup();
                // completion completion
                getfield( p( Completion.class ), "type", sig( Type.class ) );
                // completion type
                getstatic( p( Type.class ), "NORMAL", sig( Type.class ) );
                // completion type NORMAL
                if_acmpne( abrupt );
                // completion
                getfield( p( Completion.class ), "value", sig( Type.class ) );
                // value
                iffalse( jumpTarget );
                label( abrupt );
            }
        };
    }

}
