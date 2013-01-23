package org.dynjs.runtime.java;

public interface GenericHandler<T> {
    Object handle(T thing);
}
