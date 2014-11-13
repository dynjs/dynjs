package org.dynjs.debugger.requests;

/**
 * @author Bob McWhirter
 */
public class EvaluateRequest extends AbstractRequest<EvaluateResponse> {

    public static class Arguments {

        private String expr;

        public void setExpression(String expr) {
            this.expr = expr;
        }

        public String getExpression() {
            return this.expr;
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

