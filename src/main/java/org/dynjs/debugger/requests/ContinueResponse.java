package org.dynjs.debugger.requests;

/**
 * @author Bob McWhirter
 */
public class ContinueResponse extends AbstractResponse {

    public ContinueResponse(boolean success, boolean running) {
        super("continue", success, running);
    }
}
