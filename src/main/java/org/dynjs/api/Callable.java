package org.dynjs.api;

import org.dynjs.runtime.DynThreadContext;

public interface Callable {
    Object call(DynThreadContext context, Object[] arguments);
}
