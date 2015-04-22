package org.dynjs.debugger.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Bob McWhirter
 */
@JsonIgnoreProperties("maxStringLength")
public class BacktraceRequest extends AbstractRequest<BacktraceResponse> {

    private int fromFrame = -1;
    private int toFrame = -1;
    private boolean bottom = false;
    private boolean inlineRefs;

    public BacktraceRequest() {
        super("backtrace");
    }

    public void setFromFrame(int fromFrame) {
        this.fromFrame = fromFrame;
    }

    public int getFromFrame() {
        return this.fromFrame;
    }

    public void setToFrame(int toFrame) {
        this.toFrame = toFrame;
    }

    public int getToFrame() {
        return this.toFrame;
    }

    public void setBottom(boolean bottom) {
        this.bottom = bottom;
    }

    public boolean getBottom() {
        return this.bottom;
    }

    public void setInlineRefs(boolean inlineRefs) {
        this.inlineRefs = inlineRefs;
    }

    public boolean isInlineRefs() {
        return this.inlineRefs;
    }



}
