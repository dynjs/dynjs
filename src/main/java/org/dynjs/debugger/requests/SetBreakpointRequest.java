package org.dynjs.debugger.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Bob McWhirter
 */
@JsonIgnoreProperties("maxStringLength")
public class SetBreakpointRequest extends AbstractRequest<SetBreakpointResponse> {


    private String type;
    private String target;
    private String condition;
    private int line;
    private int column;

    public SetBreakpointRequest() {
        super("setbreakpoint");
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTarget() {
        return this.target;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getLine() {
        return this.line;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getColumn() {
        return this.column;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getCondition() {
        return this.condition;
    }
}
