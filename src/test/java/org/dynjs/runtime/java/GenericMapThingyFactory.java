package org.dynjs.runtime.java;

import java.util.Map;

public class GenericMapThingyFactory {
    public static Map<Integer, String> create() {
        return new GenericMapThingy<Integer, String>();
    }
}
