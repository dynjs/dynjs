package org.dynjs.runtime.java;

import java.util.HashMap;
import java.util.Map;

public class GenericMapFactory {
    public static Map<Integer, String> create() {
        return new HashMap<Integer, String>();
    }
}
