package org.dynjs.runtime;

public class ThreadContextManager {
    private static ThreadLocal<ThreadContext> threadContextLocal = new ThreadLocal<>();

    public static ThreadContext getThreadContext() {
        ThreadContext threadContext = threadContextLocal.get();

        if (threadContext == null) {
            threadContext = new ThreadContext();
            threadContextLocal.set(threadContext);
        }

        return threadContext;
    }
}
