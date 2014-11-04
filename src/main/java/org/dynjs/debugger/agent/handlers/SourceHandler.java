package org.dynjs.debugger.agent.handlers;

import io.netty.channel.ChannelHandlerContext;
import org.dynjs.debugger.requests.SourceRequest;
import org.dynjs.debugger.requests.SourceResponse;

import java.io.*;

/**
 * @author Bob McWhirter
 */
public class SourceHandler extends DebuggerChannelHandler<SourceRequest> {

    public SourceHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SourceRequest request) throws Exception {
        InputStream in = tryClasspath();

        if (in == null) {
            in = tryFilesystem();
        }

        if (in == null) {
            SourceResponse response = new SourceResponse(request, "", false, false);
            ctx.writeAndFlush(response);
            return;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        int fromLine = request.getFromLine();
        int toLine = request.getToLine();

        StringBuilder builder = new StringBuilder();

        try {
            int curLine = 0;
            while (curLine < toLine) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                if (curLine >= fromLine) {
                    builder.append(line).append("\n");
                }
                ++curLine;
            }

            SourceResponse response = new SourceResponse(request, builder.toString(), true, false);
            ctx.writeAndFlush(response);
        } finally {
            reader.close();
        }
    }

    private InputStream tryClasspath() {
        String fileName = this.debugger.getFileName();

        return this.debugger.getClass().getClassLoader().getResourceAsStream(fileName);
    }

    private InputStream tryFilesystem() throws FileNotFoundException {
        File file = new File(this.debugger.getFileName());

        if (file.exists()) {
            return new FileInputStream(file);
        }

        return null;
    }

}
