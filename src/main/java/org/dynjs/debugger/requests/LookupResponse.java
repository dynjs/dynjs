package org.dynjs.debugger.requests;

/**
 * @author Bob McWhirter
 */
public class LookupResponse extends AbstractResponse {

    public LookupResponse(boolean success, boolean running) {
        super("lookup", success, running);
    }
}
