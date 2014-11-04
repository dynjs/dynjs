package org.dynjs.debugger.events;

import org.dynjs.debugger.Debugger;
import org.dynjs.debugger.Message;

/**
 * @author Bob McWhirter
 */
public interface Event extends Message {

    Debugger getDebugger();
    String getEvent();

}
