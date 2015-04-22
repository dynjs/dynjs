package org.dynjs;

import org.dynjs.cli.Options;
import org.dynjs.runtime.Classpath;
import org.dynjs.runtime.DynamicClassLoader;

import java.io.PrintStream;
import java.util.Locale;
import java.util.TimeZone;

public class Config {


    public static enum CompileMode {
        OFF,
        FORCE,
        JIT,
        IR;
    }

    public static enum KernelMode {
        INTERNAL,
        EXTERNAL;
    }

    public static final String DEFAULT_BASE_PACKAGE = "org.dynjs.gen";

    private boolean debug = false;
    private String exposeDebugAs;

    private final DynamicClassLoader classLoader;
    private Clock clock = SystemClock.INSTANCE;
    private TimeZone timeZone = TimeZone.getDefault();
    private Locale locale = Locale.getDefault();
    private PrintStream outputStream = System.out;
    private PrintStream errorStream = System.err;
    private String basePackage = DEFAULT_BASE_PACKAGE;
    private boolean invokeDynamicEnabled = Options.INVOKEDYNAMIC.load();
    private boolean commonJSCompatible = Options.COMPATIBILITY_COMMONJS.load();
    private boolean rhinoCompatible = Options.COMPATIBILITY_RHINO.load();
    private CompileMode compileMode = Options.CLI_COMPILE_MODE.load();
    private KernelMode kernelMode = Options.CLI_KERNEL_MODE.load();
    private Integer jitThreshold = Options.JIT_TRESHOLD.load();
    private boolean jitEnabled = Options.JIT.load();
    private boolean jitAsync = Options.JIT_ASYNC.load();
    private boolean v8Compatible = Options.COMPATIBILITY_V8.load();
    private boolean sandbox = false;

    private final Classpath classpath;

    public Classpath getClasspath() {
        return classpath;
    }

    private Object[] argv;

    public Config() {
        this.classLoader = new DynamicClassLoader();
        this.classpath = new Classpath(this.classLoader);
    }

    public Config(ClassLoader parentClassLoader) {
        this.classLoader = new DynamicClassLoader(parentClassLoader);
        this.classpath = new Classpath(this.classLoader);
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

    public KernelMode getKernelMode() {
        return kernelMode;
    }

    public void setKernelMode(KernelMode kernelMode) {
        this.kernelMode = kernelMode;
    }

    public Integer getJitThreshold() {
        return jitThreshold;
    }

    public boolean isJitEnabled() {
        return jitEnabled;
    }

    public boolean isJitAsync() {
        return jitAsync;
    }

    public void setJitAsync(boolean jitAsync) {
        this.jitAsync = jitAsync;
    }

    public void setJitEnabled(boolean jitEnabled) {
        this.jitEnabled = jitEnabled;
    }

    public void setJitThreshold(Integer jitThreshold) {
        this.jitThreshold = jitThreshold;
    }

    public void setCommonJSCompatible(boolean enabled) {
        this.commonJSCompatible = enabled;
    }

    public boolean isCommonJSCompatible() {
        return this.commonJSCompatible;
    }

    public boolean isV8Compatible() {
        return v8Compatible;
    }

    public void setV8Compatible(boolean v8Compatible) {
        this.v8Compatible = v8Compatible;
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

    public void setExposeDebugAs(String name) {
        this.exposeDebugAs = name;
    }

    public String getExposeDebugAs() {
        return this.exposeDebugAs;
    }

    public void setArgv(Object[] arguments) {
        this.argv = arguments;
    }

    public Object[] getArgv() {
        return this.argv;
    }

    public boolean isSandbox() {
        return sandbox;
    }

    public void setSandbox(boolean sandbox) {
        this.sandbox = sandbox;
    }
}
