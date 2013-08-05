package org.dynjs.runtime.java;

import org.dynjs.runtime.DynObject;

public class Thing {
    public int someNum = 100;
    
    public boolean oppositeOf(boolean tf) {
        return !tf;
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
}
