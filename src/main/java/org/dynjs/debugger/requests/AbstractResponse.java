package org.dynjs.debugger.requests;

/**
 * @author Bob McWhirter
 */
public abstract class AbstractResponse implements Response {

    private final Request request;
    private final boolean success;
    private final boolean running;

    public AbstractResponse(Request request, boolean success, boolean running) {
        this.request = request;
        this.success = success;
        this.running = running;
    }

    @Override
    public Request getRequest() {
        return this.request;
    }

    @Override
    public String getCommand() {
        return this.request.getCommand();
    }

    @Override
    public boolean isSuccess() {
        return this.success;
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }
}


