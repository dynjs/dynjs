package org.dynjs.debugger.agent.handlers;

import io.netty.channel.SimpleChannelInboundHandler;
import org.dynjs.debugger.Debugger;

/**
 * @author Bob McWhirter
 */
public abstract class DebuggerChannelHandler<T> extends SimpleChannelInboundHandler<T> {

    protected Debugger debugger;

    public void setDebugger(Debugger debugger) {
        this.debugger = debugger;
    }

    protected Debugger getDebugger() {
        return this.debugger;
    }
}
