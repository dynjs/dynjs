package org.dynjs.runtime;

import org.dynalang.dynalink.linker.GuardedInvocation;
import org.dynalang.dynalink.support.Guards;
import org.dynalang.dynalink.support.Lookup;
import org.dynjs.runtime.primitives.DynPrimitiveBoolean;
import org.dynjs.runtime.primitives.DynPrimitiveNumber;

import java.lang.invoke.MethodHandle;

import static java.lang.invoke.MethodType.methodType;

public class Converters {

    public static final MethodHandle DynString2String;
    public static final MethodHandle DynPrimitiveNumber2DynNumber;
    public static final MethodHandle DynNumber2DynPrimitiveNumber;
    public static final MethodHandle DynAtom2boolean;

    static {
        DynString2String = Lookup.PUBLIC.findStatic(Converters.class, "convertDynString2String", methodType(String.class, Object.class));
        DynPrimitiveNumber2DynNumber = Lookup.PUBLIC.findStatic(Converters.class, "convertDynPrimitiveNumber2DynNumber", methodType(DynNumber.class, DynAtom.class));
        DynNumber2DynPrimitiveNumber = Lookup.PUBLIC.findStatic(Converters.class, "convertDynNumber2DynPrimitiveNumber", methodType(DynPrimitiveNumber.class, DynAtom.class));
        DynAtom2boolean = Lookup.PUBLIC.findStatic(Converters.class, "convertDynAtom2boolean", methodType(DynPrimitiveBoolean.class, DynAtom.class));
    }

    public static final GuardedInvocation Guarded_DynString2String = new GuardedInvocation(DynString2String, null);
    public static final GuardedInvocation Guarded_DynPrimitiveNumber2DynNumber = new GuardedInvocation(DynPrimitiveNumber2DynNumber,
            Guards.isInstance(DynPrimitiveNumber.class, methodType(DynNumber.class, DynAtom.class)));
    public static final GuardedInvocation Guarded_DynNumber2DynPrimitiveNumber = new GuardedInvocation(DynNumber2DynPrimitiveNumber,
            Guards.isInstance(DynNumber.class, methodType(DynPrimitiveNumber.class, DynAtom.class)));
    public static final GuardedInvocation Guarded_DynAtom2boolean = new GuardedInvocation(DynAtom2boolean,
            Guards.isInstance(DynAtom.class, methodType(DynPrimitiveBoolean.class, DynAtom.class)));

    public static String convertDynString2String(Object dynString) {
        return dynString.toString();
    }

    public static DynNumber convertDynPrimitiveNumber2DynNumber(DynAtom number) {
        return new DynNumber((DynPrimitiveNumber) number);
    }

    public static DynPrimitiveNumber convertDynNumber2DynPrimitiveNumber(DynAtom number) {
        return new DynPrimitiveNumber(Double.valueOf(((DynNumber) number).getValue()).toString(), 10);
    }

    public static DynPrimitiveBoolean convertDynAtom2boolean(DynAtom value) {
        return DynObject.toBoolean(value);
    }

}
