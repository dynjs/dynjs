package org.dynjs.debugger.requests;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.dynjs.debugger.agent.HandleSerializer;

/**
 * @author Bob McWhirter
 */
public class EvaluateResponse extends AbstractResponse<EvaluateRequest> {

    private final Object result;

    public EvaluateResponse(EvaluateRequest request, Object result, boolean success, boolean running) {
        super(request, success, running);
        this.result = result;
    }

    public Object getResult() {
        return this.result;
    }
}
