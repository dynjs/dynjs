package org.dynjs.debugger.requests;

/**
 * @author Bob McWhirter
 */
public class SuspendResponse extends AbstractResponse<SuspendRequest> {

    public SuspendResponse(SuspendRequest request, boolean success, boolean running) {
        super(request, success, running);
    }

}
