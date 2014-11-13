package org.dynjs.debugger.requests;

/**
 * @author Bob McWhirter
 */
public class SuspendRequest extends AbstractRequest<SuspendResponse> {

    public static class Arguments {

        private int maxStringLength;

        public void setMaxStringLength(int len) {
            this.maxStringLength = len;
        }

        public int getMaxStringLength() {
            return this.maxStringLength;
        }
    }

    private Arguments arguments;

    public SuspendRequest() {
        super("suspend");
    }

    public void setArguments(Arguments arguments) {
        this.arguments = arguments;
    }

    public Arguments getArguments() {
        return this.arguments;
    }

}

