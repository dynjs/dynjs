package org.dynjs.compiler;

import static me.qmx.jitescript.util.CodegenUtils.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.JiteClass;

import org.dynjs.Config;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.AbstractFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.LexicalEnvironment;

public class FunctionCompiler extends AbstractCompiler {

    private final String CALL = sig( Object.class, ExecutionContext.class, Object.class, Object[].class );


    public FunctionCompiler(Config config) {
        super( config, "Function" );
    }

    public JSFunction compile(final ExecutionContext context, final boolean strict, final List<String> formalParameters, final Statement[] statements) {
        JiteClass jiteClass = new JiteClass( nextClassName(), p( AbstractFunction.class ), new String[0] ) {
            {
                defineMethod( "<init>", ACC_PUBLIC, sig( void.class, Statement[].class, LexicalEnvironment.class, String[].class ),
                        new CodeBlock() {
                            {
                                aload( 0 );
                                // this
                                aload( 1 );
                                // this statements 
                                aload( 2 );
                                // this statements scope
                                pushBoolean( strict );
                                // this statements scope strict
                                aload( 3 );
                                // this statements scope strict formal-parameters
                                invokespecial( p( AbstractFunction.class ), "<init>", sig( void.class, Statement[].class, LexicalEnvironment.class, boolean.class, String[].class ) );
                                voidreturn();
                            }
                        } );
                defineMethod( "call", ACC_PUBLIC, CALL, new CodeBlock() {
                    {
                        for (Statement statement : statements) {
                            CodeBlockUtils.injectLineNumber( this, statement );
                            append( statement.getCodeBlock() );
                        }
                    }
                } );

            }
        };
        
        Class<AbstractFunction> functionClass = (Class<AbstractFunction>) defineClass( jiteClass );
        try {
            Constructor<AbstractFunction> ctor = functionClass.getDeclaredConstructor( Statement[].class, LexicalEnvironment.class, String[].class );
            AbstractFunction function = ctor.newInstance( statements, context.getLexicalEnvironment(), formalParameters.toArray( new String[ formalParameters.size() ] ) );
            return function;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalStateException( e );
        }
    }

}
