package org.dynjs.debugger.requests;

/**
 * @author Bob McWhirter
 */
public class SetBreakpointRequest extends AbstractRequest<SetBreakpointResponse> {

    public static class Arguments {
        private String type;
        private String target;
        private int line;

        public void setType(String type) {
            this.type = type;
        }

        public String getType() {
            return this.type;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public String getTarget() {
            return this.target;
        }

        public void setLine(int line) {
            this.line = line;
        }

        public int getLine() {
            return this.line;
        }
    }

    private Arguments arguments;

    public SetBreakpointRequest() {
        super("setbreakpoint");
    }

    public void setArguments(Arguments arguments) {
        this.arguments = arguments;
    }

    public Arguments getArguments() {
        return this.arguments;
    }
}
