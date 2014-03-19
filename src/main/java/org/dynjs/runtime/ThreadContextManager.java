package org.dynjs.runtime;

public class ThreadContextManager {

    private static ThreadLocal<ThreadContext> threadContext = new ThreadLocal<>();

    public static ExecutionContext currentContext() {
        final ThreadContext ctx = threadContext.get();
        if (ctx == null) {
            return null;
        }
        return ctx.getCurrentContext();
    }

    public static void pushContext(ExecutionContext context) {
        final ThreadContext ctx = threadContext.get();
        if (ctx == null) {
            threadContext.set(new ThreadContext());
        }
        threadContext.get().pushContext(context);
    }

    public static void popContext() {
        final ThreadContext ctx = threadContext.get();
        if (ctx == null) {
            throw new IllegalStateException("Cannot pop ExecutionContext from empty stack");
        }
        ctx.popContext();
    }

}
