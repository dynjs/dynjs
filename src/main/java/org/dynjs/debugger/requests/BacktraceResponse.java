package org.dynjs.debugger.requests;

/**
 * @author Bob McWhirter
 */
public class BacktraceResponse extends AbstractResponse {

    public BacktraceResponse(boolean success, boolean running) {
        super("backtrace", success, running);
    }
}
