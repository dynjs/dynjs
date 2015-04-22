package org.dynjs.debugger.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Bob McWhirter
 */

@JsonIgnoreProperties( { "type", "command" } )
public class AbstractRequest<T extends Response> implements Request<T> {

    private final String command;
    private int seq;

    public AbstractRequest(String command) {
        this.command = command;
    }

    @Override
    public String getCommand() {
        return this.command;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getSeq() {
        return this.seq;
    }
}
