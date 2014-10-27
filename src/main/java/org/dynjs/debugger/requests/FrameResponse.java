package org.dynjs.debugger.requests;

/**
 * @author Bob McWhirter
 */
public class FrameResponse extends AbstractResponse {

    public FrameResponse(boolean success, boolean running) {
        super("frame", success, running);
    }
}
