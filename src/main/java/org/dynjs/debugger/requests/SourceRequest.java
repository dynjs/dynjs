package org.dynjs.debugger.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Bob McWhirter
 */
@JsonIgnoreProperties("maxStringLength")
public class SourceRequest extends AbstractRequest<SourceResponse> implements NoArgumentsRequest {

    private int fromLine;
    private int toLine;

    public SourceRequest() {
        super("source");
    }

    public void setFromLine(int fromLine) {
        this.fromLine = fromLine;
    }

    public int getFromLine() {
        return this.fromLine;
    }

    public void setToLine(int toLine) {
        this.toLine = toLine;
    }

    public int getToLine() {
        return this.toLine;
    }

    public String toString() {
        return "[Source: fromLine="+ fromLine + "; toLine=" + toLine + "]";
    }

}

