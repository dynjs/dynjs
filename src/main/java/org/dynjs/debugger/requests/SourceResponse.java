package org.dynjs.debugger.requests;

/**
 * @author Bob McWhirter
 */
public class SourceResponse extends AbstractResponse<SourceRequest> {

    private final String source;
    private final int fromLine;
    private final int toLine;

    public SourceResponse(SourceRequest request, String source, int fromLine, int toLine, boolean success, boolean running) {
        super(request, success, running);
        this.source = source;
        this.fromLine = fromLine;
        this.toLine = toLine;
    }

    public String getSource() {
        return this.source;
    }

    public int getFromLine() {
        return this.fromLine;
    }

    public int getToLine() {
        return this.toLine;
    }
}
