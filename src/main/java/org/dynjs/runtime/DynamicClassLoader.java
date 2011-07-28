package org.dynjs.runtime;


public class DynamicClassLoader extends ClassLoader {

    public Class<?> define(String className, byte[] bytecode) {
        return super.defineClass(className, bytecode, 0, bytecode.length);
    }
}
