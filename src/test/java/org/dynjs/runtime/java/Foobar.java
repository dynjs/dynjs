package org.dynjs.runtime.java;

public abstract class Foobar implements Foo {
    public abstract String doItWithParameters(String param, boolean tf);
    public abstract Integer doItWithInt(Integer param);
    public abstract int doItWithPrimitiveInt(int param);
    public abstract boolean doItWithBoolean(boolean param);

    public String callWithParameters(String param, boolean tf) {
        return doItWithParameters(param, tf);
    }

    public Integer callWithInt(Integer param) {
        return doItWithInt(param);
    }

    public int callWithPrimitiveInt(int param) {
        return doItWithPrimitiveInt(param);
    }

    public boolean callWithBoolean(boolean param) {
        return doItWithBoolean(param);
    }
}
