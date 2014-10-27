package org.dynjs.debugger.requests;

/**
 * @author Bob McWhirter
 */
public class SourceResponse extends AbstractResponse {

    public SourceResponse(boolean success, boolean running) {
        super("source", success, running);
    }
}
