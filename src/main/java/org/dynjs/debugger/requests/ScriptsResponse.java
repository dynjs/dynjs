package org.dynjs.debugger.requests;

/**
 * @author Bob McWhirter
 */
public class ScriptsResponse extends AbstractResponse {

    public ScriptsResponse(boolean success, boolean running) {
        super("scripts", success, running);
    }
}
