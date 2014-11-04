package org.dynjs.debugger.requests;

/**
 * @author Bob McWhirter
 */
public class ContinueRequest  extends AbstractRequest<ContinueResponse> {

    public ContinueRequest() {
        super("continue");
    }

}
