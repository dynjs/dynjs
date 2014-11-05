package org.dynjs.debugger.agent.handlers;

import io.netty.channel.ChannelHandlerContext;
import org.dynjs.debugger.requests.SourceRequest;
import org.dynjs.debugger.requests.SourceResponse;
import org.dynjs.runtime.JSProgram;

import java.io.*;

/**
 * @author Bob McWhirter
 */
public class SourceHandler extends DebuggerChannelHandler<SourceRequest> {

    public SourceHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SourceRequest request) throws Exception {
        JSProgram program = this.debugger.getCurrentContext().getProgram();

        if ( program == null || program.getSource() == null ) {
            SourceResponse response = new SourceResponse(request, "", -1, -1, false, false);
            ctx.writeAndFlush(response);
            return;
        }


        Reader in = program.getSource().openReader();
        BufferedReader reader = new BufferedReader(in);

        int fromLine = request.getFromLine();
        int toLine = request.getToLine();

        StringBuilder builder = new StringBuilder();

        int actualFromLine = -1;
        int actualToLine = -1;

        try {
            int curLine = 0;
            while (curLine < toLine) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                if (curLine >= fromLine) {
                    if (actualFromLine < 0) {
                        actualFromLine = curLine;
                    }
                    builder.append(line).append("\n");
                }
                actualToLine = curLine;
                ++curLine;
            }

            SourceResponse response = new SourceResponse(request, builder.toString(), actualFromLine, actualToLine, true, false);
            ctx.writeAndFlush(response);
        } finally {
            reader.close();
        }
    }

}
