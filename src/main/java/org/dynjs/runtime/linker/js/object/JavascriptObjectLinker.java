package org.dynjs.runtime.linker.js.object;

import com.headius.invokebinder.Binder;
import org.dynjs.codegen.DereferencedReference;
import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Reference;
import org.projectodd.rephract.ContextualLinker;
import org.projectodd.rephract.Invocation;
import org.projectodd.rephract.Link;
import org.projectodd.rephract.LinkLogger;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class JavascriptObjectLinker extends ContextualLinker {

    public JavascriptObjectLinker(LinkLogger logger) {
        super(logger);
    }

    @Override
    public Link linkGetProperty(Invocation invocation, String propertyName) throws Exception {
        return new JSObjectPropertyGetLink( invocation.builder() );
    }

    @Override
    public Link linkSetProperty(Invocation invocation, String propertyName) throws Exception {
        return new JSObjectPropertySetLink( invocation.builder() );
    }

    @Override
    public Link linkCall(Invocation invocation) throws Exception {
        return new JSObjectCallLink( invocation.builder() );
    }

    @Override
    public Link linkConstruct(Invocation invocation) throws Exception {
        return new JSObjectConstructLink( invocation.builder() );
    }

    public static boolean callableJSObjectGuard(Object receiver) {
        return (receiver instanceof JSFunction);
    }

    public static boolean noncallableJSObjectGuard(Object receiver) {
        return ((receiver instanceof JSObject) && (!(receiver instanceof JSFunction)));
    }

    public static MethodHandle getCallableJSObjectGuard(Binder binder) throws NoSuchMethodException, IllegalAccessException {
        MethodHandle mh = MethodHandles.lookup().findStatic(JavascriptObjectLinker.class, "callableJSObjectGuard",
                MethodType.methodType(boolean.class, Object.class));

        return binder
                .drop(1, binder.type().parameterCount() - 1)
                .invoke(mh);
    }

    public static MethodHandle getNoncallableJSObjectGuard(Binder binder) throws NoSuchMethodException, IllegalAccessException {
        MethodHandle mh = MethodHandles.lookup().findStatic(JavascriptObjectLinker.class, "noncallableJSObjectGuard",
                MethodType.methodType(boolean.class, Object.class));

        return binder
                .drop(1, binder.type().parameterCount() - 1)
                .invoke(mh);
    }

    public static MethodHandle dereferencedReferenceFilter() throws NoSuchMethodException, IllegalAccessException {
        return MethodHandles.lookup().findStatic(JavascriptObjectLinker.class, "dereferencedReferenceFilter",
                MethodType.methodType(Reference.class, DereferencedReference.class));
    }

    public static Reference dereferencedReferenceFilter(DereferencedReference deref) {
        return deref.getReference();
    }

    public static MethodHandle dereferencedValueFilter() throws NoSuchMethodException, IllegalAccessException {
        return MethodHandles.lookup().findStatic(JavascriptObjectLinker.class, "dereferencedValueFilter",
                MethodType.methodType(Object.class, DereferencedReference.class));
    }

    public static Object dereferencedValueFilter(DereferencedReference deref) {
        return deref.getValue();
    }

    public static MethodHandle createTypeErrorFilter() throws NoSuchMethodException, IllegalAccessException {
        return MethodHandles.lookup().findStatic(JavascriptObjectLinker.class, "createTypeErrorFilter",
                MethodType.methodType(ThrowException.class, ExecutionContext.class, Object.class));
    }

    public static ThrowException createTypeErrorFilter(ExecutionContext context, Object nonCallable) {
        return new ThrowException(context, context.createTypeError("not callable: " + nonCallable));
    }

    public static MethodHandle createReferenceErrorFilter() throws NoSuchMethodException, IllegalAccessException {
        return MethodHandles.lookup().findStatic(JavascriptObjectLinker.class, "createReferenceErrorFilter",
                MethodType.methodType(ThrowException.class, ExecutionContext.class));
    }

    public static ThrowException createReferenceErrorFilter(ExecutionContext context) {
        return new ThrowException(context, context.createTypeError("not referenceable"));
    }

    /*
     * @Override
     * public StrategicLink linkConstruct(StrategyChain chain, Object receiver, Object[] args, Binder binder, Binder guardBinder) throws NoSuchMethodException,
     * IllegalAccessException {
     * // TODO Auto-generated method stub
     * return super.linkConstruct(chain, receiver, args, binder, guardBinder);
     * }
     */

}
