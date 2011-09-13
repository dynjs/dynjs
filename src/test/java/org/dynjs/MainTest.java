package org.dynjs;

import org.junit.Test;

public class MainTest {
    @Test
    public void callMainWithNoArguments(){
        new Main(new String[]{}).run();
    }

    @Test
    public void callMainWithInvalidFile(){
        new Main(new String[]{"meh.js"}).run();
    }

    @Test
    public void callMainWithInvalidArguments(){
        new Main(new String[]{"--invalid"}).run();
    }
}
