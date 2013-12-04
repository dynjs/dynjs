package org.dynjs.runtime.java;

public abstract class Foobar implements Foo {
    public abstract String doItWithParameters(String param);

    public String callWithParameters(String param) {
        return doItWithParameters(param);
    }
}
