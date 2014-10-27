package org.dynjs.debugger.events;

import org.dynjs.debugger.DebugRunner;
import org.dynjs.debugger.Debugger;

/**
 * @author Bob McWhirter
 */
public class AbstractEvent implements Event {

    private final Debugger debugger;
    private final String event;

    public AbstractEvent(Debugger debugger, String event) {
        this.debugger = debugger;
        this.event = event;
    }

    @Override
    public Debugger getDebugger() {
        return this.debugger;
    }

    @Override
    public String getEvent() {
        return this.event;
    }
}
