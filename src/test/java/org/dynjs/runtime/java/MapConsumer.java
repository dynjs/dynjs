package org.dynjs.runtime.java;

import java.util.Map;

public class MapConsumer {
    
    public Object consume(Map<String, Object> map, String key) {
        return map.get( key );
    }

}
