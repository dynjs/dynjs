package org.dynjs.runtime.java;

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
}
