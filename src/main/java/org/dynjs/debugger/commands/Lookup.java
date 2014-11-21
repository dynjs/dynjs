package org.dynjs.debugger.commands;

import org.dynjs.debugger.Debugger;
import org.dynjs.debugger.ReferenceManager;
import org.dynjs.debugger.requests.LookupRequest;
import org.dynjs.debugger.requests.LookupResponse;

/**
 * @author Bob McWhirter
 */
public class Lookup extends AbstractCommand<LookupRequest, LookupResponse> {

    public Lookup(Debugger debugger) {
        super(debugger, LookupRequest.class, LookupResponse.class);
    }

    @Override
    public LookupResponse handle(LookupRequest request) {
        LookupResponse response = new LookupResponse( request, true, false );

        ReferenceManager refManager = this.debugger.getReferenceManager();

        for ( Integer handle : request.getHandles() ) {
            Object value = refManager.getObject( handle );

            if ( value != null ) {
                response.getList().add(value);
            }
        }
        return response;
    }
}
