package org.dynjs.debugger.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Bob McWhirter
 */
public class ContinueRequest  extends AbstractRequest<ContinueResponse> {

    public static class Arguments {

        private String action;
        private int count;

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

        public String toString() {
            return "[Arguments: action=" + this.action + "; count=" + this.count + "]";
        }
    }

    private Arguments arguments;

    public ContinueRequest() {
        super("continue");
    }

    public void setArguments(Arguments arguments) {
        this.arguments = arguments;
    }

    public Arguments getArguments() {
        return this.arguments;
    }

    public String toString() {
        return "[Continue: arguments=" + this.arguments + "]";
    }



}
