package org.dynjs.debugger.handlers;

import org.dynjs.debugger.Debugger;
import org.dynjs.debugger.requests.Request;
import org.dynjs.debugger.requests.Response;

import java.util.Map;

/**
 * @author Bob McWhirter
 */
public abstract class AbstractHandler<REQUEST extends Request<RESPONSE>, RESPONSE extends Response> {

    protected final Debugger debugger;

    public AbstractHandler(Debugger debugger) {
        this.debugger = debugger;
    }

    public abstract RESPONSE handle(REQUEST request);
}
