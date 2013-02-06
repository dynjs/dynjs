package org.dynjs.runtime.java;

import java.util.HashMap;
import java.util.Map;

public class GenericMapFactory {
    public static Map<Integer, String> create() {
        return new HashMap<Integer, String>();
    }
    
    public static NonMap<?> createNonMap() {
        return new NonMap<String>();
    }
    
    public static class NonMap<T> {
        public String sayHello(T thing) {
            return thing.toString();
        }
    }
}
