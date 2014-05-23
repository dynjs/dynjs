package org.dynjs.ir;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class JITCompiler {
    private static final Executor compilationQueue = Executors.newFixedThreadPool(8, new ThreadFactory() {
        private final AtomicInteger count = new AtomicInteger(1);

        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setName("IR_JIT_Compiler-" + count.getAndIncrement());
            thread.setDaemon(true);
            return thread;
        }
    });

    public static interface CompilerCallback {
        public void done(JSFunction compiledFunction);
    }

    public void compile(final ExecutionContext context, final IRJSFunction function, final CompilerCallback callback) {
        compilationQueue.execute(new Runnable() {
            @Override
            public void run() {
                // compile
                callback.done(compileFunction(context, function));
            }
        });
    }

    private JSFunction compileFunction(ExecutionContext context, IRJSFunction function) {
        return function.compile(context);
    }
}
