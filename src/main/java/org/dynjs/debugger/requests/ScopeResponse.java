package org.dynjs.debugger.requests;

/**
 * @author Bob McWhirter
 */
public class ScopeResponse extends AbstractResponse {

    public ScopeResponse(boolean success, boolean running) {
        super("scope", success, running);
    }
}
