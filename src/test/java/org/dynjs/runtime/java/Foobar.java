package org.dynjs.runtime.java;

public abstract class Foobar implements Foo {
    public abstract String doItWithParameters(String param, boolean tf);

    public String callWithParameters(String param, boolean tf) {
        return doItWithParameters(param, tf);
    }
}
