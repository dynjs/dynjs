package org.dynjs.debugger.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Bob McWhirter
 */
public abstract class AbstractResponse<T extends Request> implements Response {

    private final T request;
    private final boolean success;
    private final boolean running;

    public AbstractResponse(T request, boolean success, boolean running) {
        this.request = request;
        this.success = success;
        this.running = running;
    }

    @Override
    @JsonIgnore
    public T getRequest() {
        return this.request;
    }

    @Override
    @JsonIgnore
    public String getCommand() {
        return this.request.getCommand();
    }

    @Override
    @JsonIgnore
    public boolean isSuccess() {
        return this.success;
    }

    @Override
    @JsonIgnore
    public boolean isRunning() {
        return this.running;
    }

    @Override
    @JsonIgnore
    public Collection<? extends Object> getRefs() {
        return Collections.emptyList();
    }
}


