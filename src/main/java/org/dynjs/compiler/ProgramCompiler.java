package org.dynjs.compiler;

import static me.qmx.jitescript.util.CodegenUtils.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.JiteClass;

import org.dynjs.Config;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.BaseProgram;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSProgram;

public class ProgramCompiler extends AbstractCompiler {

    public ProgramCompiler(Config config) {
        super(config, "Program");
    }

    public JSProgram compile(final Statement statement) {
        JiteClass jiteClass = new JiteClass( nextClassName(), p( BaseProgram.class ), new String[0] ) {
            {
                defineMethod( "<init>", ACC_PUBLIC | ACC_VARARGS, sig( void.class, Statement.class ),
                        new CodeBlock() {
                            {
                                aload( 0 );
                                aload( 1 );
                                invokespecial( p( BaseProgram.class ), "<init>", sig( void.class, Statement.class ) );
                                voidreturn();
                            }
                        } );

                defineMethod( "execute", ACC_PUBLIC | ACC_VARARGS, sig( Completion.class, ExecutionContext.class ), getCodeBlock() );
            }

            private CodeBlock getCodeBlock() {
                return new CodeBlock() {
                    {
                        append( statement.getCodeBlock() );
                        areturn();
                    }
                };
            }
        };

        Class<BaseProgram> cls = (Class<BaseProgram>) defineClass( jiteClass );
        try {
            Constructor<BaseProgram> ctor = cls.getDeclaredConstructor( Statement.class );
            return (JSProgram) ctor.newInstance( statement );
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalStateException( e );
        }
    }
}
