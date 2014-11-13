package org.dynjs.debugger.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * @author Bob McWhirter
 */
public class SourceRequest extends AbstractRequest<SourceResponse> {

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

