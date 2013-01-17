package org.dynjs.runtime.java;

import java.util.concurrent.Executor;

public class NettyFactory {
    
    private Executor e1;
    private Executor e2;
    
    public NettyFactory(String s1, String s2) throws Exception {
        throw new Exception( "wrong constructor" );
    }

    public NettyFactory(Executor e1, Executor e2) {
        this.e1 = e1;
        this.e2 = e2;
    }
    
    public Executor getE1() {
        return this.e1;
    }
    
    public Executor getE2() {
        return this.e2;
    }

}
