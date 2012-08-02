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
        return compileIfNecessary(Collections.singletonList( arg ), name);
    }
    
    public CodeBlock compileBasicBlockIfNecessary(String name) {
        return compileIfNecessary(null, name);
    }
    
    public CodeBlock compileFunctionIfNecessary(final List<String> args) {
        return compileIfNecessary(args, null);
    }
    
    protected CodeBlock compileIfNecessary(final List<String> args, final String name) {
        return new CodeBlock() {
            {
                LabelNode skipCompile = new LabelNode();

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
                astore( 4 ); // Entry
                aload( 4 );

                //getfield( p( Entry.class ), "compiled", ci( Object.class ) );
                invokevirtual( p( Entry.class), "getCompiled", sig( Object.class ) );
                astore( 5 ); // Compiled object
                aload( 5 );

                ifnonnull( skipCompile );

                if (name == null) { // not a basic-block
                    bipush( args.size() );
                    anewarray( p( String.class ) );
                    astore( 6 ); // Args array

                    for (int i = 0; i < args.size(); i++) {
                        aload( 6 );
                        bipush( i );
                        ldc( args.get( i ) );
                        aastore();
                    }
                }

                aload( DynJSCompiler.Arities.CONTEXT );
                invokevirtual( DynJSCompiler.Types.CONTEXT, "getRuntime", sig( DynJS.class ) );
                
                if ( name != null ) { // basic-block prefix
                    ldc( name );
                }

                aload( DynJSCompiler.Arities.CONTEXT );
                aload( 4 ); // Entry
                getfield( p( Entry.class ), "codeBlock", ci( CodeBlock.class ) );
                if (name == null ) { // not a basic-block
                    aload( 6 ); // args array
                    invokevirtual( DynJSCompiler.Types.RUNTIME, "compile", sig( Object.class, DynThreadContext.class, CodeBlock.class, String[].class ) );
                } else { // basic-block, maybe with and arg, or not.
                    if ( args == null ) {
                        invokevirtual( DynJSCompiler.Types.RUNTIME, "compileBasicBlock", sig( Object.class, String.class, DynThreadContext.class, CodeBlock.class ) );
                    } else {
                        ldc( args.get( 0 ) );
                        invokevirtual( DynJSCompiler.Types.RUNTIME, "compileBasicBlock", sig( Object.class, String.class, DynThreadContext.class, CodeBlock.class, String.class ) );
                    }
                }
                dup();
                astore( 5 ); // Compiled object
                aload( 4 ); // Entry
                swap();
                //putfield( p( Entry.class ), "compiled", ci( Object.class ) );
                invokevirtual( p(Entry.class), "setCompiled", sig( void.class, Object.class ) );

                label( skipCompile );
                aload(5);
            }
        };
    }

}
