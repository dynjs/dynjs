package org.dynjs.debugger.commands;

import org.dynjs.debugger.Debugger;
import org.dynjs.debugger.requests.EvaluateRequest;
import org.dynjs.debugger.requests.EvaluateResponse;
import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Runner;
import org.dynjs.runtime.Types;

/**
 * @author Bob McWhirter
 */
public class Evaluate extends AbstractCommand<EvaluateRequest, EvaluateResponse> {

    public Evaluate(Debugger debugger) {
        super(debugger, EvaluateRequest.class, EvaluateResponse.class);
    }

    @Override
    public EvaluateResponse handle(EvaluateRequest request) {
        ExecutionContext context = null;

        if (request.isGlobal()) {
            context = this.debugger.getGlobalContext();
        } else {
            context = this.debugger.getContext( request.getFrame() );
        }

        Runner runner = context.getRuntime().newRunner();


        try {
            Object result = runner.withContext(context).withSource(request.getExpression()).directEval().evaluate();
            return new EvaluateResponse(request, result, true, this.debugger.isRunning());
        } catch (ThrowException e) {
            return new EvaluateResponse(request, Types.UNDEFINED, true, this.debugger.isRunning());
        }
    }
}
