package org.dynjs.runtime.java;

import java.lang.reflect.Method;

import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.exception.DynJSException;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.DynThreadContext;
import org.dynjs.runtime.modules.JavaFunction;

/** Factory to create DynObject instances based upon Java classes annotated with <code>@JavaPrototype</code>
 *  and <code>@JavaPrototypeFunction</code>.
 *  
 * @see JavaPrototype
 * @see JavaPrototypeFunction
 *  
 * @author Bob McWhirter
 *
 */
public class JavaPrototypeFactory {
    
    /** Create a new Javascript object backed by Java methods.
     * 
     * @param context The context.
     * @param prototype The Java prototype class.
     * @return The new Javascript object, or <code>null</code> if the prototype class
     *         is not marked with <code>@JavaPrototype</code>.
     */
    public static DynObject newObject(DynThreadContext context, Class<?> prototype) {
        JavaPrototype prototypeAnno = prototype.getAnnotation( JavaPrototype.class );
        
        if ( prototypeAnno == null ) {
            return null;
        }
        
        Object javaObj = null;
        
        try {
            javaObj = prototype.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new DynJSException( e );
        }
        
        DynObject dynObj = new DynObject();
        
        dynObj.setProperty( "__java_object", javaObj );
        
        Method[] methods = prototype.getMethods();
        
        for ( Method method : methods ) {
            JavaPrototypeFunction methodAnno = method.getAnnotation( JavaPrototypeFunction.class );
            
            if (methodAnno == null ) {
                continue;
            }
            
            String name = methodAnno.name();
            
            if ( name.equals( "" ) ) {
                name = method.getName();
            }
            
            try {
                DynObject function = DynJSCompiler.wrapFunction( context, new JavaFunction( javaObj, method )  );
                dynObj.setProperty( name, function );
            } catch (IllegalAccessException e) {
                throw new DynJSException( e );
            }
        }
        
        return dynObj;
    }

}
