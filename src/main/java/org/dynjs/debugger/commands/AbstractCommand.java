package org.dynjs.debugger.commands;

import io.netty.channel.ChannelHandler;
import org.dynjs.debugger.Debugger;
import org.dynjs.debugger.agent.handlers.DebuggerChannelHandler;
import org.dynjs.debugger.requests.Request;
import org.dynjs.debugger.requests.Response;

/**
 * @author Bob McWhirter
 */
public abstract class AbstractCommand<REQUEST extends Request<RESPONSE>, RESPONSE extends Response> {

    private final Debugger debugger;
    private final Class<REQUEST> requestClass;
    private final Class<RESPONSE> responseClass;
    private final Class<? extends DebuggerChannelHandler> channelHandlerClass;

    public AbstractCommand(Debugger debugger, Class<REQUEST> requestClass, Class<RESPONSE> responseClass, Class<? extends DebuggerChannelHandler> channelHandlerClass) {
        this.debugger = debugger;
        this.requestClass = requestClass;
        this.responseClass = responseClass;
        this.channelHandlerClass = channelHandlerClass;
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

    public Class<? extends DebuggerChannelHandler> channelHandlerClass() {
        return this.channelHandlerClass;
    }

    public DebuggerChannelHandler newChannelHandler(Debugger debugger) throws IllegalAccessException, InstantiationException {
        DebuggerChannelHandler handler = this.channelHandlerClass.newInstance();
        handler.setDebugger( debugger );
        return handler;

    }

    public abstract RESPONSE handle(REQUEST request);
}
