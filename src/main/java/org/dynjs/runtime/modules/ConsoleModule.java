package org.dynjs.runtime.modules;

@Module(name = "console")
public class ConsoleModule {

    @Export
    public void log(String message) {
        System.err.println( message );
    }

}
