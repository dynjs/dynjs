package org.dynjs.debugger.requests;

/**
 * @author Bob McWhirter
 */
public class SetVariableValueResponse extends AbstractResponse {

    public SetVariableValueResponse(boolean success, boolean running) {
        super("listbreakpoints", success, running);
    }
}
