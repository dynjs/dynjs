package org.dynjs.runtime;

import java.util.ArrayList;
import java.util.List;

public class ThreadContextManager {

    private static ThreadLocal<List<ExecutionContext>> threadContext = new ThreadLocal<>();

    public static ExecutionContext currentContext() {
        List<ExecutionContext> currentList = threadContext.get();
        if (currentList == null || currentList.isEmpty()) {
            return null;
        }

        return currentList.get(0);
    }

    public static void pushContext(ExecutionContext context) {
        List<ExecutionContext> currentList = threadContext.get();
        if (currentList == null) {
            currentList = new ArrayList<>();
            threadContext.set(currentList);
        }
        currentList.add(context);
    }

    public static void popContext() {
        List<ExecutionContext> currentList = threadContext.get();
        if (currentList == null) {
            throw new IllegalStateException("Cannot pop ExecutionContext from empty stack");
        }
        ExecutionContext head = currentList.get(currentList.size() - 1);
        currentList.remove(currentList.size() - 1);

        if (!currentList.isEmpty()) {
            if (head.isThrowInProgress()) {
                ExecutionContext nextHead = currentList.get(currentList.size() - 1);
                nextHead.addThrowStack(head.getThrowStack());
            }
        }

        //if ( currentList.isEmpty() ) {
        //threadContext.remove();
        //}
    }

}
