package org.dynjs.debugger.requests;

/**
 * @author Bob McWhirter
 */
public class SourceResponse extends AbstractResponse<SourceRequest> {

    private final String source;

    public SourceResponse(SourceRequest request, String source, boolean success, boolean running) {
        super(request, success, running);
        this.source = source;
    }

    public String getSource() {
        return this.source;
    }

    public int getFromLine() {
        return getRequest().getFromLine();
    }

    public int getToLine() {
        return getRequest().getToLine();
    }
}
