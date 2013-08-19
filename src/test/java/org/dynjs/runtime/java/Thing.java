package org.dynjs.runtime.java;

import org.dynjs.runtime.DynObject;

public class Thing {
    public int someNum = 100;
    
    public boolean oppositeOf(boolean tf) {
        return !tf;
    }

    public Thing() {
        super();
    }

    public Thing(int[] numbers) {
        super();
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

    public String joiner(String []strings) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < strings.length; i++) {
            buffer.append(strings[i]);
        }
        return buffer.toString();
    }
}
