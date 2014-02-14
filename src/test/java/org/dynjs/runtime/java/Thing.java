package org.dynjs.runtime.java;

import java.lang.reflect.Array;
import java.util.List;

import org.dynjs.runtime.DynObject;

public class Thing {
    public int someNum = 100;
    public Object someThing = "Not null";
    public Object someNull = null;
    
    public boolean oppositeOf(boolean tf) {
        return !tf;
    }

    public Thing() {
        super();
    }

    public Thing(List[] lists) {
        super();
    }

    public Thing(Object obj) {
      super();
      someThing = obj;
    }

    public boolean gotNull() {
      return someThing == null;
    }

    public Object getSomeNull() {
        return someNull;
    }
    
    public String intMethod(int arg) {
        return String.valueOf(arg);
    }
    
    public long longMethod(long arg) {
        return arg;
    }

    public Object dynObjectMethod(DynObject obj) {
        return obj.get("foo");
    }

    public String stringJoiner(String[] strings) {
        return joinArray(strings);
    }

    public String booleanJoiner(boolean[] bools) {
        return joinArray(bools);
    }

    public String byteJoiner(byte[] bytes) {
        return joinArray(bytes);
    }

    public String charJoiner(char[] chars) {
        return joinArray(chars);
    }

    public String doubleJoiner(double[] doubles) {
        return joinArray(doubles);
    }

    public String floatJoiner(float[] floats) {
        return joinArray(floats);
    }

    public String intJoiner(int[] ints) {
        return joinArray(ints);
    }

    public String longJoiner(long[] longs) {
        return joinArray(longs);
    }

    public String shortJoiner(short[] shorts) {
        return joinArray(shorts);
    }

    protected String joinArray(Object array) {
        int length = Array.getLength(array);
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            buffer.append("" + Array.get(array, i));
        }
        return buffer.toString();
    }
}
