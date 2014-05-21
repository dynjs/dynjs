package org.dynjs.runtime.util;

/**
 * Simple utility functions to help execution of calls
 */
public class CallHelpers {
    public static final Object[] EMPTY_ARRAY = new Object[0];

    public static Object[] allButFirstArgument(Object[] args) {
        int newLength = args.length - 1;

        if (newLength == 0) { // no leftover args
            return EMPTY_ARRAY;
        }

        Object[] argList = new Object[newLength];
        System.arraycopy(args, 1, argList, 0, newLength);
        return argList;
    }
}
