package org.dynjs.debugger.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Bob McWhirter
 */
public class EvaluateRequest extends AbstractRequest<EvaluateResponse> {

    @JsonIgnoreProperties( "maxStringLength" )
    public static class Arguments {

        private String expr;
        private boolean global;

        public void setExpression(String expr) {
            this.expr = expr;
        }

        public String getExpression() {
            return this.expr;
        }

        public void setGlobal(boolean global) {
            this.global = global;
        }

        public boolean isGlobal() {
            return this.global;
        }
    }

    private Arguments arguments;

    public EvaluateRequest() {
        super("evaluate");
    }

    public void setArguments(Arguments arguments) {
        this.arguments = arguments;
    }

    public Arguments getArguments() {
        return this.arguments;
    }


}

