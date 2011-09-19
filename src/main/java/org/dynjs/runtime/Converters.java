package org.dynjs.runtime;

import org.dynalang.dynalink.linker.GuardedInvocation;
import org.dynalang.dynalink.support.Guards;
import org.dynalang.dynalink.support.Lookup;
import org.dynjs.runtime.primitives.DynPrimitiveNumber;

import java.lang.invoke.MethodHandle;

import static java.lang.invoke.MethodType.methodType;

public class Converters {

    public static final MethodHandle DynString2String;
    public static final MethodHandle DynPrimitiveNumber2DynNumber;
    public static final MethodHandle DynNumber2DynPrimitiveNumber;

    static {
        DynString2String = Lookup.PUBLIC.findStatic(Converters.class, "convertDynString2String", methodType(String.class, Object.class));
        DynPrimitiveNumber2DynNumber = Lookup.PUBLIC.findStatic(Converters.class, "convertDynPrimitiveNumber2DynNumber", methodType(DynNumber.class, DynPrimitiveNumber.class));
        DynNumber2DynPrimitiveNumber = Lookup.PUBLIC.findStatic(Converters.class, "convertDynNumber2DynPrimitiveNumber", methodType(DynPrimitiveNumber.class, DynNumber.class));
    }

    public static final GuardedInvocation Guarded_DynString2String = new GuardedInvocation(DynString2String, null);
    public static final GuardedInvocation Guarded_DynPrimitiveNumber2DynNumber = new GuardedInvocation(DynPrimitiveNumber2DynNumber,
            Guards.isInstance(DynPrimitiveNumber.class, methodType(DynNumber.class, DynPrimitiveNumber.class)));
    public static final GuardedInvocation Guarded_DynNumber2DynPrimitiveNumber = new GuardedInvocation(DynNumber2DynPrimitiveNumber,
            Guards.isInstance(DynPrimitiveNumber.class, methodType(DynPrimitiveNumber.class, DynNumber.class)));

    public static String convertDynString2String(Object dynString) {
        return dynString.toString();
    }

    public static DynNumber convertDynPrimitiveNumber2DynNumber(DynPrimitiveNumber number) {
        return new DynNumber(number);
    }

    public static DynPrimitiveNumber convertDynNumber2DynPrimitiveNumber(DynNumber number) {
        return new DynPrimitiveNumber(Double.valueOf(number.getValue()).toString(), 10);
    }

}
