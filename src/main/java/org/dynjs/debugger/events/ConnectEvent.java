package org.dynjs.debugger.events;

import org.dynjs.debugger.Debugger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bob McWhirter
 */
public class ConnectEvent {

    public ConnectEvent() {
    }

    public Map<String, String> getHeaders() {
        return new HashMap<String, String>() {
            {
                put("Type", "connect");
            } };
    }

}
