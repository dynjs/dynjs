package org.dynjs.runtime.java;

public abstract class Foobar implements Foo {
    public abstract String doItWithParameters(String param, boolean tf);
    public abstract int doItWithInt(int param);
    public abstract boolean doItWithBoolean(boolean param);

    public String callWithParameters(String param, boolean tf) {
        return doItWithParameters(param, tf);
    }

    public int callWithInt(int param) {
        return doItWithInt(param);
    }

    public boolean callWithBoolean(boolean param) {
        return doItWithBoolean(param);
    }
}
