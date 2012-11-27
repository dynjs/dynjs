package org.dynjs.runtime.linker.java;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.List;

public class UnboundJavaMethod {

    private String name;
    private List<MethodHandle> methods = new ArrayList<>();

    public UnboundJavaMethod(String name) {
        if ("value".equals(name)) {
            new Exception().printStackTrace();
        }
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    void add(MethodHandle method) {
        this.methods.add(method);
    }

    public MethodHandle findMethodHandle(Class<?>[] types) {
        if ("<init>".equals(this.name)) {
            for (MethodHandle each : methods) {
                if (matches(each.type(), types)) {
                    return each;
                }
            }
        } else {
            for (MethodHandle each : methods) {
                if (matches(each.type().dropParameterTypes(0, 1), types)) {
                    return each;
                }
            }
        }
        return null;
    }

    protected boolean matches(MethodType candidate, Class<?>[] target) {
        if (candidate.parameterCount() == target.length) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "[UnboundJavaMethod: name=" + this.name + "]";
    }

}
