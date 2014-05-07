package org.dynjs.runtime.linker;

import org.dynjs.runtime.DynArray;
import org.projectodd.rephract.java.reflect.ArrayCoercer;
import org.projectodd.rephract.java.reflect.CoercionMatrix;

public class DynArrayCoercer extends ArrayCoercer {
    @Override
    public Object[] coerceToObject(Object value) {
        DynArray dynArray = (DynArray) value;
        int length = (int) dynArray.length();
        Object[] converted = new Object[length];
        for (int i = 0; i < length; i++) {
            converted[i] = dynArray.get(i);
        }
        return converted;
    }

    @Override
    public boolean[] coerceToBoolean(Object value) {
        DynArray dynArray = (DynArray) value;
        int length = (int) dynArray.length();
        boolean[] converted = new boolean[length];
        for (int i = 0; i < length; i++) {
            converted[i] = (boolean)dynArray.get(i);
        }
        return converted;
    }

    @Override
    public byte[] coerceToByte(Object value) {
        DynArray dynArray = (DynArray) value;
        int length = (int) dynArray.length();
        byte[] converted = new byte[length];
        for (int i = 0; i < length; i++) {
            converted[i] = CoercionMatrix.numberToPrimitiveByte((Number) dynArray.get(i));
        }
        return converted;
    }

    @Override
    public char[] coerceToChar(Object value) {
        DynArray dynArray = (DynArray) value;
        int length = (int) dynArray.length();
        char[] converted = new char[length];
        for (int i = 0; i < length; i++) {
            String string = (String) dynArray.get(i);
            converted[i] = CoercionMatrix.stringToPrimitiveCharacter((String)dynArray.get(i));
        }
        return converted;
    }

    @Override
    public double[] coerceToDouble(Object value) {
        DynArray dynArray = (DynArray) value;
        int length = (int) dynArray.length();
        double[] converted = new double[length];
        for (int i = 0; i < length; i++) {
            converted[i] = CoercionMatrix.numberToPrimitiveDouble((Number)dynArray.get(i));
        }
        return converted;
    }

    @Override
    public float[] coerceToFloat(Object value) {
        DynArray dynArray = (DynArray) value;
        int length = (int) dynArray.length();
        float[] converted = new float[length];
        for (int i = 0; i < length; i++) {
            converted[i] = CoercionMatrix.numberToPrimitiveFloat((Number)dynArray.get(i));
        }
        return converted;
    }

    @Override
    public int[] coerceToInt(Object value) {
        DynArray dynArray = (DynArray) value;
        int length = (int) dynArray.length();
        int[] converted = new int[length];
        for (int i = 0; i < length; i++) {
            converted[i] = CoercionMatrix.numberToPrimitiveInteger((Number)dynArray.get(i));
        }
        return converted;
    }

    @Override
    public long[] coerceToLong(Object value) {
        DynArray dynArray = (DynArray) value;
        int length = (int) dynArray.length();
        long[] converted = new long[length];
        for (int i = 0; i < length; i++) {
            converted[i] = CoercionMatrix.numberToPrimitiveLong((Number)dynArray.get(i));
        }
        return converted;
    }

    @Override
    public short[] coerceToShort(Object value) {
        DynArray dynArray = (DynArray) value;
        int length = (int) dynArray.length();
        short[] converted = new short[length];
        for (int i = 0; i < length; i++) {
            converted[i] = CoercionMatrix.numberToPrimitiveShort((Number)dynArray.get(i));
        }
        return converted;
    }
}
