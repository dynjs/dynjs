package org.dynjs.debugger.commands;

import io.netty.channel.ChannelHandler;
import org.dynjs.debugger.Debugger;
import org.dynjs.debugger.agent.handlers.CommandHandler;
import org.dynjs.debugger.agent.handlers.DebuggerChannelHandler;
import org.dynjs.debugger.requests.Request;
import org.dynjs.debugger.requests.Response;

/**
 * @author Bob McWhirter
 */
public abstract class AbstractCommand<REQUEST extends Request<RESPONSE>, RESPONSE extends Response> {

    protected final Debugger debugger;
    private final Class<REQUEST> requestClass;
    private final Class<RESPONSE> responseClass;

    public AbstractCommand(Debugger debugger, Class<REQUEST> requestClass, Class<RESPONSE> responseClass) {
        this.debugger = debugger;
        this.requestClass = requestClass;
        this.responseClass = responseClass;
    }

    protected Debugger getDebugger() {
        return this.debugger;
    }

    public Class<REQUEST> requestClass() {
        return this.requestClass;
    }

    public REQUEST newRequest() throws IllegalAccessException, InstantiationException {
        return this.requestClass.newInstance();
    }

    public Class<RESPONSE> responseClass() {
        return this.responseClass;
    }

    public RESPONSE newResponse() throws IllegalAccessException, InstantiationException {
        return this.responseClass.newInstance();
    }

    public ChannelHandler newChannelHandler(Debugger debugger) throws IllegalAccessException, InstantiationException {
        return new CommandHandler(this);
    }

    public abstract RESPONSE handle(REQUEST request);
}
