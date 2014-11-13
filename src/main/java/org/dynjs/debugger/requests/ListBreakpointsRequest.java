package org.dynjs.debugger.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * @author Bob McWhirter
 */
public class ListBreakpointsRequest extends AbstractRequest<ListBreakpointsResponse> {


    @JsonIgnoreProperties("maxStringLength")
    public static class Arguments {

    }

    private Arguments arguments;

    public ListBreakpointsRequest() {
        super("listbreakpoints");
    }

    public void setArguments(Arguments arguments) {
        this.arguments = arguments;
    }

    public Arguments getArguments() {
        return this.arguments;
    }

}

