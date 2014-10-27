package org.dynjs.debugger.requests;

/**
 * @author Bob McWhirter
 */
public class ScopesResponse extends AbstractResponse {

    public ScopesResponse(boolean success, boolean running) {
        super("scopes", success, running);
    }
}
