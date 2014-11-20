package org.dynjs.debugger.requests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Bob McWhirter
 */
public class LookupResponse extends AbstractResponse<LookupRequest> {

    private List<Object> objects = new ArrayList<>();

    public LookupResponse(LookupRequest request, boolean success, boolean running) {
        super(request, success, running);
    }

    public List<Object> getList() {
        return this.objects;
    }
}

