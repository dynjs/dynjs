package org.dynjs.debugger.requests;

/**
 * @author Bob McWhirter
 */
public class ContinueResponse extends AbstractResponse {

    public ContinueResponse(ContinueRequest request, boolean success, boolean running) {
        super(request, success, running);
    }
}
