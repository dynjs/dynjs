package org.dynjs.debugger;

import org.dynjs.debugger.events.Event;

/**
 * @author Bob McWhirter
 */
public interface DebugListener {
    void on(Event event);
}
