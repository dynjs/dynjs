package org.dynjs.debugger.requests;

import java.util.List;

/**
 * @author Bob McWhirter
 */
public class ListBreakpointsRequest extends AbstractRequest<ListBreakpointsResponse> {

    public ListBreakpointsRequest() {
        super("listbreakpoints");
    }

}

