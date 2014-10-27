package org.dynjs.debugger.events;

import org.dynjs.debugger.DebugRunner;
import org.dynjs.debugger.Debugger;

/**
 * @author Bob McWhirter
 */
public class BreakEvent extends AbstractEvent {

    public BreakEvent(Debugger debugger) {
        super(debugger, "break");
    }

}
