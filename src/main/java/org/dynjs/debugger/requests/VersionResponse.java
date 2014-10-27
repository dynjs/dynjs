package org.dynjs.debugger.requests;

/**
 * @author Bob McWhirter
 */
public class VersionResponse extends AbstractResponse {

    public VersionResponse(boolean success, boolean running) {
        super("version", success, running);
    }
}
