package org.dynjs.debugger.agent;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.dynjs.debugger.Debugger;
import org.dynjs.debugger.agent.handlers.ConnectHandler;
import org.dynjs.debugger.agent.handlers.DebugHandler;
import org.dynjs.debugger.agent.handlers.ErrorHandler;
import org.dynjs.debugger.commands.AbstractCommand;

/**
 * @author Bob McWhirter
 */
public class DebuggerAgent {

    private final ChannelFuture channelFuture;

    public DebuggerAgent(final Debugger debugger, final EventLoopGroup group, final int port) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(group);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.config().setAutoRead(true);
                ch.pipeline().addLast("debug", new DebugHandler("debugger"));
                ch.pipeline().addLast("json.encoder", new JSONEncoder(debugger));
                ch.pipeline().addLast("json.decoder", new JSONDecoder(debugger));
                ch.pipeline().addLast("decoded", new DebugHandler("decoded"));

                ch.pipeline().addLast("connect", new ConnectHandler());

                for ( AbstractCommand each : debugger.getCommands() ) {
                    ch.pipeline().addLast( each.newChannelHandler( debugger ) );
                }

                ch.pipeline().addLast("error", new ErrorHandler() );

                debugger.setListener( new AgentListener( ch ) );
            }
        });
        this.channelFuture = bootstrap.bind(port);
        this.channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                System.err.println("debugger listening on port " + port);
            }
        });
    }
}
