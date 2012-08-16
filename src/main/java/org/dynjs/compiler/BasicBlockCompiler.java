package org.dynjs.compiler;

import static me.qmx.jitescript.util.CodegenUtils.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.JiteClass;

import org.dynjs.Config;
import org.dynjs.parser.Statement;
import org.dynjs.parser.statement.BlockStatement;
import org.dynjs.runtime.AbstractBasicBlock;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.ExecutionContext;

public class BasicBlockCompiler extends AbstractCompiler {

    private final String INVOKE = sig( Completion.class, ExecutionContext.class );

    public BasicBlockCompiler(Config config) {
        super( config, "Block" );
    }

    public BasicBlock compile(final String grist, final Statement body) {
        JiteClass jiteClass = new JiteClass( nextClassName( grist ), p( AbstractBasicBlock.class ), new String[0] ) {
            {
                defineMethod( "<init>", ACC_PUBLIC, sig( void.class, BlockStatement.class ),
                        new CodeBlock() {
                            {
                                aload( 0 );
                                // this
                                aload( 1 );
                                // this statements
                                invokespecial( p( AbstractBasicBlock.class ), "<init>", sig( void.class, BlockStatement.class ) );
                                voidreturn();
                            }
                        } );
                defineMethod( "call", ACC_PUBLIC, INVOKE,
                        new CodeBlock() {
                            {
                                append( body.getCodeBlock() );
                                areturn();
                            }
                        } );

            }
        };

        Class<AbstractBasicBlock> blockClass = (Class<AbstractBasicBlock>) defineClass( jiteClass );
        try {
            Constructor<AbstractBasicBlock> ctor = blockClass.getDeclaredConstructor( BlockStatement.class );
            AbstractBasicBlock block = ctor.newInstance( body );
            return block;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalStateException( e );
        }
    }

}
