package org.dynjs.debugger.events;

import org.dynjs.debugger.agent.handlers.WrappingHandler;

/**
 * @author Bob McWhirter
 */
public class EventWrapper {

    private final Event body;
    private final int seq;

    public EventWrapper(Event body) {
        this.body = body;
        this.seq = WrappingHandler.seqCounter.incrementAndGet();
    }

    public String getType() {
        return "event";
    }

    public String getEvent() {
        return this.body.getEvent();
    }

    public Event getBody() {
        return this.body;
    }

    public int getSeq() {
        return this.seq;
    }
}
