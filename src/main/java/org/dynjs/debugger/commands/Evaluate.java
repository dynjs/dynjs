package org.dynjs.debugger.commands;

import org.dynjs.debugger.Debugger;
import org.dynjs.debugger.requests.EvaluateRequest;
import org.dynjs.debugger.requests.EvaluateResponse;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Runner;

/**
 * @author Bob McWhirter
 */
public class Evaluate extends AbstractCommand<EvaluateRequest, EvaluateResponse> {

    public Evaluate(Debugger debugger) {
        super(debugger, EvaluateRequest.class, EvaluateResponse.class);
    }

    @Override
    public EvaluateResponse handle(EvaluateRequest request) {
        ExecutionContext context = debugger.getCurrentContext();
        Runner runner = context.getRuntime().newRunner();
        Object result = runner.withContext(context).withSource(request.getArguments().getExpression()).evaluate();
        return new EvaluateResponse(request, result, true, false);
    }
}
