package org.dynjs.debugger.events;

import org.dynjs.debugger.DebugRunner;
import org.dynjs.debugger.Debugger;

/**
 * @author Bob McWhirter
 */
public interface Event {

    Debugger getDebugger();
    String getEvent();

}
