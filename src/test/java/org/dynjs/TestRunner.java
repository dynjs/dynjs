package org.dynjs;

import org.dynjs.runtime.*;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lanceball
 */
public class TestRunner {

    private static final String SCRIPT = "" +
            "var executor = require('./target/test-classes/specRunner.js');" +
            "executor.run('" + testPattern() + "');";

    public static String testPattern() {
        String pattern = System.getProperty("test.pattern");
        if (pattern == null) {
            pattern = "**/*Spec.js";
        }

        return pattern;
    }

    public static void main(String... args) throws InterruptedException {
        TestRunner runner = new TestRunner();
        runner.run();
    }

    private final ScheduledExecutorService executor;
    private final AtomicInteger counter = new AtomicInteger(0);
    private final DynJS dynjs;

    public TestRunner() {
        Config config = new Config(TestRunner.class.getClassLoader());
        config.setCompileMode(Config.CompileMode.OFF);
        this.executor = Executors.newSingleThreadScheduledExecutor( new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setDaemon(false);
                return t;
            }
        });
        //config.setArgv(new String[]{ SCRIPT});
        this.dynjs = new DynJS(config);
        setupTimers();
        setupConsole();
    }

    public void increment() {
        counter.incrementAndGet();
    }

    public void decrement() {
        int val = counter.decrementAndGet();
        if ( val == 0 ) {
            this.executor.shutdown();
        }
    }

    public void run() {
        counter.incrementAndGet();
        executor.submit( new Runnable() {
            @Override
            public void run() {
                dynjs.newRunner().withSource( SCRIPT ).execute();
                decrement();
            }
        });
    }

    private void setupConsole() {
        GlobalContext context = dynjs.getGlobalContext();
        JSObject object = context.getObject();

        JSObject console = new DynObject( context );
        console.put( null, "log", new Log( context), false );

        object.put( null, "console", console, false );

    }

    private void setupTimers() {
        GlobalContext context = dynjs.getGlobalContext();
        JSObject object = context.getObject();
        object.put(null, "setTimeout", new SetTimeout(context, executor), false);
        object.put(null, "clearTimeout", new ClearTimeout(context, executor), false);
        object.put(null, "setInterval", new SetInterval(context, executor), false);
        object.put(null, "clearInterval", new ClearInterval(context, executor), false);
    }

    public class SetTimeout extends AbstractNativeFunction {

        public SetTimeout(GlobalContext globalContext, ScheduledExecutorService executor) {
            super(globalContext);
        }

        @Override
        public Object call(final ExecutionContext context, Object self, Object... args) {
            final JSFunction fn = (JSFunction) args[0];
            long timeout = Types.toInteger( context, args[1] );
            increment();
            executor.schedule( new Runnable() {
                @Override
                public void run() {
                    try {
                        context.call(fn, Types.UNDEFINED);
                    } finally {
                        decrement();
                    }
                }
            }, timeout, TimeUnit.MILLISECONDS );
            return Types.UNDEFINED;
        }
    }

    public class ClearTimeout extends AbstractNativeFunction {

        public ClearTimeout(GlobalContext globalContext, ScheduledExecutorService executor) {
            super(globalContext);
        }

        @Override
        public Object call(ExecutionContext context, Object self, Object... args) {
            System.err.println( "clearTimeout: " + self + ", " + Arrays.asList( args ) );
            return null;
        }
    }

    public class SetInterval extends AbstractNativeFunction {


        public SetInterval(GlobalContext globalContext, ScheduledExecutorService executor) {
            super(globalContext);
        }

        @Override
        public Object call(ExecutionContext context, Object self, Object... args) {
            System.err.println( "setInterval: " + self + ", " + Arrays.asList( args ) );
            return null;
        }
    }

    public class ClearInterval extends AbstractNativeFunction {

        public ClearInterval(GlobalContext globalContext, ScheduledExecutorService executor) {
            super(globalContext);
        }

        @Override
        public Object call(ExecutionContext context, Object self, Object... args) {
            System.err.println( "clearInterval: " + self + ", " + Arrays.asList( args ) );
            return null;
        }
    }

    public class Log extends AbstractNativeFunction {

        public Log(GlobalContext globalContext) {
            super(globalContext);
        }

        @Override
        public Object call(ExecutionContext context, Object self, Object... args) {
            System.err.println( args[0] );
            return Types.UNDEFINED;
        }
    }


}
