package org.dynjs.debugger.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * @author Bob McWhirter
 */
@JsonIgnoreProperties("maxStringLength")
public class ListBreakpointsRequest extends AbstractRequest<ListBreakpointsResponse> {

    public ListBreakpointsRequest() {
        super("listbreakpoints");
    }

}

