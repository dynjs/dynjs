package org.dynjs.debugger.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Bob McWhirter
 */
@JsonIgnoreProperties("maxStringLength")
public class ContinueRequest extends AbstractRequest<ContinueResponse> {

    private String action;
    private int count;

    public ContinueRequest() {
        super("continue");
    }

    @JsonProperty("stepaction")
    public void setStepAction(String action) {
        this.action = action;
    }

    @JsonProperty("stepaction")
    public String getStepAction() {
        return this.action;
    }

    @JsonProperty("stepcount")
    public void setStepCount(int count) {
        this.count = count;
    }

    @JsonProperty("stepcount")
    public int getStepCount() {
        return this.count;
    }

}
