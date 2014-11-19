package org.dynjs.debugger.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Bob McWhirter
 */
public class BacktraceRequest extends AbstractRequest<BacktraceResponse> {

    @JsonIgnoreProperties("maxStringLength")
    public static class Arguments {

        private int fromFrame = -1;
        private int toFrame = -1;
        private boolean inlineRefs;

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

        public void setInlineRefs(boolean inlineRefs) {
            this.inlineRefs = inlineRefs;
        }

        public boolean isInlineRefs() {
            return this.inlineRefs;
        }

    }

    private Arguments arguments;

    public BacktraceRequest() {
        super("backtrace");
    }

    public void setArguments(Arguments arguments) {
        this.arguments = arguments;
    }

    public Arguments getArguments() {
        return this.arguments;
    }
}
