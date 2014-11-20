package org.dynjs.debugger.agent;

import io.netty.channel.Channel;
import org.dynjs.debugger.DebugListener;
import org.dynjs.debugger.events.Event;

/**
 * @author Bob McWhirter
 */
public class AgentListener implements DebugListener {

    private final Channel channel;

    public AgentListener(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void on(Event event) {
        this.channel.writeAndFlush(event);
    }
}
