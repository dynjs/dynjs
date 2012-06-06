package org.dynjs.runtime;

import org.dynjs.api.Function;
import org.dynjs.api.Resolver;

public class Frame implements Resolver {
    private final Function function;
    private final Object[] params;

    public Frame(Function function, Object... params) {
        this.function = function;
        this.params = params;
    }

    public Object[] getParams() {
        return params;
    }

    @Override
    public Object resolve(String name) {
        final String[] arguments = function.getArguments();
        for (int i = 0; i < arguments.length; i++) {
            String argument = arguments[i];
            if (argument.equals(name)) {
                if (getParams().length > i) {
                    return getParams()[i];
                }
            }
        }
        return null;
    }
}
