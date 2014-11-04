package org.dynjs.debugger.requests;

/**
 * @author Bob McWhirter
 */
public class AbstractRequest<T extends Response> implements Request<T> {

    private final String command;

    public AbstractRequest(String command) {
        this.command = command;
    }

    @Override
    public String getCommand() {
        return this.command;
    }
}
