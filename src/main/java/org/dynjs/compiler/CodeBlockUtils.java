package org.dynjs.compiler;

import static me.qmx.jitescript.util.CodegenUtils.*;

import java.util.Collections;
import java.util.List;

import me.qmx.jitescript.CodeBlock;

import org.dynjs.api.Function;
import org.dynjs.parser.Position;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.CodeStorage.Entry;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.DynThreadContext;
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
    
    public static CodeBlock compileFunction(final DynThreadContext context, final Statement statement, final List<String> args) {
        return compileIfNecessary( context, args, null, statement, false );
    }
    
    public static CodeBlock compileBasicBlock(final DynThreadContext context, String name, final Statement statement) {
        return compileIfNecessary( context, null, name, statement, false );
    }
    
    public static CodeBlock compileBasicBlockWithArg(final DynThreadContext context, String name, final Statement statement, String arg) {
        return compileIfNecessary( context, Collections.singletonList( arg ), name, statement, false );
    }
    
    public static CodeBlock compileBasicBlockWithReturn(final DynThreadContext context, String name, final Statement statement) {
        return compileIfNecessary( context, null, name, statement, true );
    }

    public static CodeBlock compileIfNecessary(final DynThreadContext context, final List<String> args, final String name, final Statement toCompile, final boolean forceReturn) {
        if (toCompile == null) {
            return new CodeBlock() {
                {
                    getstatic( p( DynThreadContext.class ), "NOOP", ci( Function.class ) );
                }
            };
        }

        return new CodeBlock() {
            {
                LabelNode skipCompile = new LabelNode();
                LabelNode end = new LabelNode();

                int statementNumber = toCompile.getStatementNumber();
                Entry entry = context.retrieve( statementNumber );

                // Stash codeblock if required
                if (entry.codeBlock == null) {
                    System.err.println( "stashing codeblock for " + statementNumber );
                    entry.codeBlock = toCompile.getCodeBlock();
                    if (forceReturn) {
                        entry.codeBlock.areturn();
                    }
                    System.err.println( "stashed: " + entry.codeBlock + " with " + entry.codeBlock.getInstructionList().size() );
                    for ( int i = 0 ; i < entry.codeBlock.getInstructionList().size() ; ++i ) {
                        System.err.println( "- " + entry.codeBlock.getInstructionList().get( i ));
                    }
                    context.retrieve( statementNumber );
                }

                // ----------------------------------------
                // ----------------------------------------

                aload( DynJSCompiler.Arities.CONTEXT );
                ldc( statementNumber );
                invokevirtual( DynJSCompiler.Types.CONTEXT, "retrieve", sig( Entry.class, int.class ) );
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

                aload( DynJSCompiler.Arities.CONTEXT );
                invokevirtual( DynJSCompiler.Types.CONTEXT, "getRuntime", sig( DynJS.class ) );

                // entry runtime
                swap();

                // runtime entry

                if (name != null) { // basic-block prefix
                    ldc( name );
                    swap();
                    // runtime name entry
                }

                aload( DynJSCompiler.Arities.CONTEXT );
                swap();
                // runtime [ name ] context entry

                getfield( p( Entry.class ), "codeBlock", ci( CodeBlock.class ) );
                // runtime [ name ] context codeblock

                if (name == null) { // not a basic-block
                    int len = args.size();
                    bipush( len );
                    anewarray( p( String.class ) );
                    for (int i = 0; i < len; ++i) {
                        dup();
                        bipush( i );
                        ldc( args.get( i ) );
                        aastore();
                    }
                    // runtime context codeblock args
                    // REQUIRE: runtime context codebock array
                    invokevirtual( DynJSCompiler.Types.RUNTIME, "compile", sig( Object.class, DynThreadContext.class, CodeBlock.class, String[].class ) );
                } else { // basic-block, maybe with and arg, or not.
                    // REQUIRE: runtime name context codebock [ arg ]
                    if (args == null) {
                        invokevirtual( DynJSCompiler.Types.RUNTIME, "compileBasicBlock", 
                                sig( Object.class, String.class, DynThreadContext.class, CodeBlock.class ) );
                    } else {
                        ldc( args.get( 0 ) );
                        invokevirtual( DynJSCompiler.Types.RUNTIME, "compileBasicBlock", 
                                sig( Object.class, String.class, DynThreadContext.class, CodeBlock.class, String.class ) );
                    }
                }
                // object

                dup();

                // object object
                aload( DynJSCompiler.Arities.CONTEXT );
                ldc( statementNumber );

                // object object context statementnum
                invokevirtual( DynJSCompiler.Types.CONTEXT, "retrieve", sig( Entry.class, int.class ) );
                // object object entry
                swap();
                // object entry object
                invokevirtual( p( Entry.class ), "setCompiled", sig( void.class, Object.class ) );

                go_to( end );

                label( skipCompile );
                // entry object
                swap();
                // object entry
                pop();
                // object
                label( end );
            }
        };
    }

}
