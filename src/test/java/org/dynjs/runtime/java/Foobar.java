package org.dynjs.runtime.java;

public abstract class Foobar implements Foo {
    public abstract String doItWithParameters(String param, boolean tf);

    public abstract Integer doItWithInt(Integer param);
    public abstract int doItWithPrimitiveInt(int param);
    public abstract Long doItWithLong(Long param);
    public abstract long doItWithPrimitiveLong(long param);
    public abstract Short doItWithShort(Short param);
    public abstract short doItWithPrimitiveShort(short param);
    public abstract Float doItWithFloat(Float param);
    public abstract float doItWithPrimitiveFloat(float param);
    public abstract Double doItWithDouble(Double param);
    public abstract double doItWithPrimitiveDouble(double param);
    public abstract Boolean doItWithBoolean(Boolean param);
    public abstract boolean doItWithPrimitiveBoolean(boolean param);
    public abstract boolean doItWithObjectReturningBoolean(Object param);

    public String callWithParameters(String param, boolean tf) {
        return doItWithParameters(param, tf);
    }

    public Integer callWithInt(Integer param) {
        return doItWithInt(param);
    }
    public int callWithPrimitiveInt(int param) {
        return doItWithPrimitiveInt(param);
    }
    public Long callWithLong(Long param) {
        return doItWithLong(param);
    }
    public long callWithPrimitiveLong(long param) {
        return doItWithPrimitiveLong(param);
    }
    public Short callWithShort(Short param) {
        return doItWithShort(param);
    }
    public short callWithPrimitiveShort(short param) {
        return doItWithPrimitiveShort(param);
    }
    public Float callWithFloat(Float param) {
        return doItWithFloat(param);
    }
    public float callWithPrimitiveFloat(float param) {
        return doItWithPrimitiveFloat(param);
    }
    public Double callWithDouble(Double param) {
        return doItWithDouble(param);
    }
    public double callWithPrimitiveDouble(double param) {
        return doItWithPrimitiveDouble(param);
    }
    public Boolean callWithBoolean(Boolean param) {
        return doItWithBoolean(param);
    }
    public boolean callWithPrimitiveBoolean(boolean param) {
        return doItWithPrimitiveBoolean(param);
    }

}
