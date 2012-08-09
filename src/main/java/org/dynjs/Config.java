package org.dynjs;

import java.io.PrintStream;

import org.dynjs.runtime.DynamicClassLoader;

public class Config {
    
    public static final String DEFAULT_BASE_PACKAGE = "org.dynjs.gen";
    
    private boolean debug = false;
    private final ClassLoader classLoader;
    private PrintStream outputStream = System.out;
    private PrintStream errorStream = System.err;
    private String basePackage = DEFAULT_BASE_PACKAGE;
    
    public Config() {
        this.classLoader = new DynamicClassLoader();
    }
    
    public Config(ClassLoader parentClassLoader) {
        this.classLoader = new DynamicClassLoader( parentClassLoader );
    }
    
    public ClassLoader getClassLoader() {
        return this.classLoader;
    }
    
    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }
    
    public String getBasePackage() {
        return this.basePackage;
    }
    
    public void setOutputStream(PrintStream outputStream) {
        this.outputStream = outputStream;
    }
    
    public PrintStream getOutputStream() {
        return this.outputStream;
    }
    
    public void setErrorStream(PrintStream errorStream) {
        this.errorStream = errorStream;
    }
    
    public PrintStream getErrorStream() {
        return this.errorStream;
    }
    
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
    
    public boolean isDebug() {
        return this.debug;
    }

}
