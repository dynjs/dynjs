package org.dynjs;

import java.io.File;
import java.io.PrintStream;
import java.util.TimeZone;

import org.dynjs.runtime.DefaultObjectFactory;
import org.dynjs.runtime.DynamicClassLoader;
import org.dynjs.runtime.GlobalObjectFactory;

public class Config {

    public static final String DEFAULT_BASE_PACKAGE = "org.dynjs.gen";

    private boolean debug = false;
    private final ClassLoader classLoader;
    private Clock clock = SystemClock.INSTANCE;
    private TimeZone timeZone = TimeZone.getDefault();
    private PrintStream outputStream = System.out;
    private PrintStream errorStream = System.err;
    private String basePackage = DEFAULT_BASE_PACKAGE;
    private GlobalObjectFactory globalObjectFactory = new DefaultObjectFactory();
    private File bytecodeOutputDir = null;

    public Config() {
        this.classLoader = new DynamicClassLoader();
    }

    public Config(ClassLoader parentClassLoader) {
        this.classLoader = new DynamicClassLoader(parentClassLoader);
    }

    public ClassLoader getClassLoader() {
        return this.classLoader;
    }
    
    public void setClock(Clock clock) {
        this.clock = clock;
    }
    
    public Clock getClock() {
        return this.clock;
    }
    
    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }
    
    public TimeZone getTimeZone() {
        return this.timeZone;
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

    public GlobalObjectFactory getGlobalObjectFactory() {
        return globalObjectFactory;
    }

    public void setGlobalObjectFactory(GlobalObjectFactory globalObjectFactory) {
        this.globalObjectFactory = globalObjectFactory;
    }

    public File getBytecodeOutputDir() {
        return bytecodeOutputDir;
    }

    public void setBytecodeOutputDir(File bytecodeOutputDir) {
        this.bytecodeOutputDir = bytecodeOutputDir;
    }
}
