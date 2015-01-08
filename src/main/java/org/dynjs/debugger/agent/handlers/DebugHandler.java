/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dynjs.debugger.agent.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.net.SocketAddress;
import java.nio.charset.Charset;

/**
 * @author Bob McWhirter
 */
public class DebugHandler extends ChannelDuplexHandler {

    private final String name;

    public DebugHandler(String name) {
        this.name = name;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.err.println(ctx.channel() + " | " + this.name + " >> READ >> " + msg.getClass() );
        System.err.println("-->>" );
        if (msg instanceof ByteBuf) {
            System.err.println(msg);
            System.err.println(((ByteBuf) msg).toString(Charset.defaultCharset()));
        } else {
            System.err.println(msg.toString());
        }
        System.err.println("-->>" );
        super.channelRead(ctx, msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.err.println(ctx.channel() + " | " + this.name + " << WRITE << " + msg.getClass() );
        System.err.println("<<--" );
        if (msg instanceof ByteBuf) {
            System.err.println(((ByteBuf) msg).toString(Charset.defaultCharset()));
        } else {
            System.err.println(msg.toString());
        }
        System.err.println("<<--" );
        super.write(ctx, msg, promise);
    }

    @Override
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise future) throws Exception {
        System.err.println(ctx.channel() + " | " + this.name + " << BIND << " + localAddress );
        super.bind(ctx, localAddress, future);
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise future) throws Exception {
        System.err.println(ctx.channel() + " | " + this.name + " << CLOSE");
        super.close(ctx, future);
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise future) throws Exception {
        System.err.println(ctx.channel() + " | " + this.name + " << DISCONNECT");
        super.disconnect(ctx, future);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.err.println(ctx.channel() + " | " + this.name + " >> CHANNEL ACTIVE");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.err.println(ctx.channel() + " | " + this.name + " >> CHANNEL INACTIVE");
        super.channelInactive(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.err.println(ctx.channel() + " | " + this.name + " >> CHANNEL REGISTERED");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.err.println(ctx.channel() + " | " + this.name + " >> CHANNEL UNREGISTERED");
        super.channelUnregistered(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println(ctx.channel() + " | " + this.name + " >> ERROR" );
        cause.printStackTrace();
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.err.println(ctx.channel() + " | " + this.name + " >> USER EVENT: " + evt  );
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void deregister(ChannelHandlerContext ctx, ChannelPromise future) throws Exception {
        System.err.println(ctx.channel() + " | " + this.name + " >> DEREGISTER" );
        super.deregister(ctx, future);
    }

    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        System.err.println(ctx.channel() + " | " + this.name + " << READ() autoRead=" + ctx.channel().config().isAutoRead() );
        super.read(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.err.println(ctx.channel() + " | " + this.name + " >> READ COMPLETE" );
        super.channelReadComplete(ctx);
    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise future) throws Exception {
        System.err.println(ctx.channel() + " | " + this.name + " >> CONNECT : " + remoteAddress );
        super.connect(ctx, remoteAddress, localAddress, future);
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
        System.err.println(ctx.channel() + " | " + this.name + " << FLUSH()" );
        super.flush(ctx);
    }

}
