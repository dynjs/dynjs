package org.dynjs.debugger.requests;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bob McWhirter
 */
public class LookupRequest extends AbstractRequest<LookupResponse> {

    public static class Arguments {

        private List<Integer> handles;

        public Arguments() {

        }

        public List<Integer> getHandles() {
            return this.handles;
        }

        public void setHandles(List<Integer> handles) {
            this.handles = handles;
        }

    }

    private Arguments arguments;

    public LookupRequest() {
        super("lookup");
    }

    public Arguments getArguments() {
        return this.arguments;
    }
    
    public void setArguments(Arguments arguments) {
        this.arguments = arguments;
    }

}

