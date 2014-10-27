package org.dynjs.debugger.requests;

/**
 * @author Bob McWhirter
 */
public class SetExceptionBreakResponse extends AbstractResponse {

    public SetExceptionBreakResponse(boolean success, boolean running) {
        super("setexceptionbreak", success, running);
    }
}
