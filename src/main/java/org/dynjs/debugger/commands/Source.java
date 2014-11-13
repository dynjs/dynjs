package org.dynjs.debugger.commands;

import org.dynjs.debugger.Debugger;
import org.dynjs.debugger.requests.SourceRequest;
import org.dynjs.debugger.requests.SourceResponse;
import org.dynjs.runtime.JSProgram;
import org.dynjs.runtime.SourceProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * @author Bob McWhirter
 */
public class Source extends AbstractCommand<SourceRequest, SourceResponse> {

    public Source(Debugger debugger) {
        super(debugger, SourceRequest.class, SourceResponse.class);
    }

    @Override
    public SourceResponse handle(SourceRequest request) {
        SourceProvider source = this.debugger.getCurrentContext().getSource();

        if (source == null ) {
            return new SourceResponse(request, "", -1, -1, false, false);
        }

        BufferedReader reader = null;
        try {
            Reader in = source.openReader();
            reader = new BufferedReader(in);

            int fromLine = request.getFromLine();
            int toLine = request.getToLine();

            StringBuilder builder = new StringBuilder();

            int actualFromLine = -1;
            int actualToLine = -1;

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
                    actualToLine = curLine;
                }
                ++curLine;
            }

            return new SourceResponse(request, builder.toString(), actualFromLine, actualToLine+1, true, false);
        } catch (IOException e) {
            return new SourceResponse(request, "", -1, -1, false, false);
        } finally {
            if ( reader != null ) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
