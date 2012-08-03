package org.dynjs.parser.statement;

import static me.qmx.jitescript.util.CodegenUtils.*;

import java.util.Collections;
import java.util.List;

import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.CodeStorage.Entry;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.DynThreadContext;
import org.objectweb.asm.tree.LabelNode;

public class BaseCompilableBlockStatement extends BaseStatement {

    protected DynThreadContext context;
    private Statement compilable;

    BaseCompilableBlockStatement(Tree tree, final DynThreadContext context, Statement compilable) {
        super( tree );
        this.context = context;
        this.compilable = compilable != null ? compilable : new EmptyStatement();
    }

    public CodeBlock compileBasicBlockIfNecessary(String name, String arg) {
        return compileIfNecessary( Collections.singletonList( arg ), name );
    }

    public CodeBlock compileBasicBlockIfNecessary(String name) {
        return compileIfNecessary( null, name );
    }

    public CodeBlock compileFunctionIfNecessary(final List<String> args) {
        return compileIfNecessary( args, null );
    }

    protected CodeBlock compileIfNecessary(final List<String> args, final String name) {
        return new CodeBlock() {
            {
                LabelNode skipCompile = new LabelNode();
                LabelNode end = new LabelNode();

                int statementNumber = compilable.getStatementNumber();
                Entry entry = context.retrieve( statementNumber );

                // Stash codeblock if required
                if (entry.codeBlock == null) {
                    System.err.println( "stashing codeblock for " + statementNumber );
                    entry.codeBlock = compilable.getCodeBlock();
                    System.err.println( "stashed: " + entry.codeBlock );
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
                        invokevirtual( DynJSCompiler.Types.RUNTIME, "compileBasicBlock", sig( Object.class, String.class, DynThreadContext.class, CodeBlock.class ) );
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
