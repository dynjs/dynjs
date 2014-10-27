package org.dynjs.debugger.requests;

/**
 * @author Bob McWhirter
 */
public class EvaluateResponse extends AbstractResponse {

    public EvaluateResponse(boolean success, boolean running) {
        super("evaluate", success, running);
    }
}
