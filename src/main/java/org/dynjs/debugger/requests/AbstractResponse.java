package org.dynjs.debugger.requests;

/**
 * @author Bob McWhirter
 */
public abstract class AbstractResponse implements Response {

    private final String command;
    private final boolean success;
    private final boolean running;

    public AbstractResponse(String command, boolean success, boolean running) {
        this.command = command;
        this.success = success;
        this.running = running;
    }

    @Override
    public String getCommand() {
        return this.command;
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


