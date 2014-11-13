package org.dynjs.debugger.events;

/**
 * @author Bob McWhirter
 */
public class EventWrapper {

    private final Event body;

    public EventWrapper(Event body) {
        this.body = body;
    }

    public String getEvent() {
        return this.body.getEvent();
    }

    public Event getBody() {
        return this.body;
    }
}
