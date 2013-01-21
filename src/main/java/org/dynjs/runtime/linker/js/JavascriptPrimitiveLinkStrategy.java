package org.dynjs.runtime.linker.js;

import static org.dynjs.runtime.linker.LinkerUtils.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Reference;
import org.projectodd.linkfusion.LinkLogger;
import org.projectodd.linkfusion.StrategicLink;
import org.projectodd.linkfusion.StrategyChain;
import org.projectodd.linkfusion.mop.ContextualLinkStrategy;

import com.headius.invokebinder.Binder;

public class JavascriptPrimitiveLinkStrategy extends ContextualLinkStrategy<ExecutionContext> {

    public JavascriptPrimitiveLinkStrategy(LinkLogger logger){ 
        super(ExecutionContext.class, logger);
    }

    
    @Override
    public StrategicLink linkGetProperty(StrategyChain chain, Object receiver, String propName, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {

        //System.err.println("jsprimitive: link getprop: " + receiver + " // " + propName);
        // [ object(receiver), T(context), string(name) ]


        if (isJavascriptPrimitiveReference(receiver)) {
            MethodHandle handle = binder
                    .convert( Object.class, Reference.class, ExecutionContext.class, String.class )
                    .permute( 1, 0, 1, 2 )
                    .fold( primitiveReferenceBaseFolder() )
                    .drop( 1, 2 )
                    .convert(Object.class, JSObject.class, ExecutionContext.class, String.class)
                    .invokeVirtual(lookup(), "get");

            MethodHandle guard = javascriptPrimitiveReferenceGuard(guardBinder);
            return new StrategicLink(handle, guard);
        }


        return chain.nextStrategy();
    }
    
    @Override
    public StrategicLink linkCall(StrategyChain chain, Object receiver, Object self, Object[] args, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {
        
        //System.err.println("jsprimitive: link call: " + receiver.getClass() + " // " + receiver );
        
        
        // * [ object(receiver) ?(context) object(self) object[](args)
 
        if (isPrimitiveDereferencedReference(receiver) ) {
            /*
            MethodHandle handle = binder
                    .permute(1, 0, 2, 3)
                    .convert(Object.class, ExecutionContext.class, Reference.class, Object.class, Object[].class)
                    .invokeVirtual(lookup(), "call");

            MethodHandle guard = javascriptPrimitiveReferenceGuard(guardBinder);
            return new StrategicLink(handle, guard);
            */
            MethodHandle handle = binder
                    .drop(0)
                    .drop(1, binder.type().parameterCount() - 2)
                    .convert(Object.class, ExecutionContext.class)
                    .filter(0, createTypeErrorFilter())
                    .throwException();

            MethodHandle guard = getReceiverClassGuard(JSObject.class, guardBinder);

            return new StrategicLink(handle, guard);
        }

        return chain.nextStrategy();
    }
    
    public static MethodHandle createTypeErrorFilter() throws NoSuchMethodException, IllegalAccessException {
        return MethodHandles.lookup().findStatic(JavascriptObjectLinkStrategy.class, "createTypeErrorFilter",
                MethodType.methodType(ThrowException.class, ExecutionContext.class));
    }

}
