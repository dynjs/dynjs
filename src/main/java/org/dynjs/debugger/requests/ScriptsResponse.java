package org.dynjs.debugger.requests;

/**
 * @author Bob McWhirter
 */
public class ScriptsResponse extends AbstractResponse<ScriptsRequest> {

    public ScriptsResponse(ScriptsRequest request, boolean success, boolean running) {
        super(request, success, running);
    }
}
