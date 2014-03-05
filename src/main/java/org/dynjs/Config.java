package org.dynjs;

import java.io.PrintStream;
import java.util.Locale;
import java.util.TimeZone;

import org.dynjs.cli.Options;
import org.dynjs.runtime.DefaultObjectFactory;
import org.dynjs.runtime.DynamicClassLoader;
import org.dynjs.runtime.GlobalObjectFactory;

public class Config {

    public static enum CompileMode {
        OFF,
        FORCE,
        JIT,
    }

    public static final String DEFAULT_BASE_PACKAGE = "org.dynjs.gen";

    private boolean debug = false;
    private final DynamicClassLoader classLoader;
    private Clock clock = SystemClock.INSTANCE;
    private TimeZone timeZone = TimeZone.getDefault();
    private Locale locale = Locale.getDefault();
    private PrintStream outputStream = System.out;
    private PrintStream errorStream = System.err;
    private String basePackage = DEFAULT_BASE_PACKAGE;
    private GlobalObjectFactory globalObjectFactory = new DefaultObjectFactory();
    private boolean invokeDynamicEnabled = Options.INVOKEDYNAMIC.load();
    private boolean nodePackageManagerEnabled = true;
    private boolean rhinoCompatible = true;
    private CompileMode compileMode = Options.CLI_COMPILE_MODE.load();

    private Object[] argv;

    public Config() {
        this.classLoader = new DynamicClassLoader();
        initializeOptions();
    }

    public Config(ClassLoader parentClassLoader) {
        this.classLoader = new DynamicClassLoader(parentClassLoader);
        initializeOptions();
    }

    private void initializeOptions() {
        if (System.getProperty("dynjs.disable.npm") != null) {
            setNodePackageManagerEnabled(false);
        }
        if (System.getProperty("dynjs.rhino.compat") != null) {
            setRhinoCompatible(false);
        }
    }

    private void setRhinoCompatible(boolean rhinoCompatible) {
        this.rhinoCompatible = rhinoCompatible;
    }

    public boolean isRhinoCompatible() {
        return this.rhinoCompatible;
    }

    public void setInvokeDynamicEnabled(boolean enabled) {
        this.invokeDynamicEnabled = enabled;
    }

    public boolean isInvokeDynamicEnabled() {
        return this.invokeDynamicEnabled;
    }

    public void setCompileMode(CompileMode compileMode) {
        this.compileMode = compileMode;
    }

    public CompileMode getCompileMode() {
        return this.compileMode;
    }

    public void setNodePackageManagerEnabled(boolean enabled) {
        this.nodePackageManagerEnabled = enabled;
    }

    public boolean isNodePackageManagerEnabled() {
        return this.nodePackageManagerEnabled;
    }

    public DynamicClassLoader getClassLoader() {
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

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return this.locale;
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

    public void setArgv(Object[] arguments) {
        this.argv = arguments;
    }
    
    public Object[] getArgv() {
        return this.argv;
    }

}
