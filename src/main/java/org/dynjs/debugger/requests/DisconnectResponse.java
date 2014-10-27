package org.dynjs.debugger.requests;

/**
 * @author Bob McWhirter
 */
public class DisconnectResponse extends AbstractResponse {

    public DisconnectResponse(boolean success, boolean running) {
        super("version", success, running);
    }
}
