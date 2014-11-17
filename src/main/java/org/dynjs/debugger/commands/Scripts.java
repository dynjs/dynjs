package org.dynjs.debugger.commands;

import org.dynjs.debugger.Debugger;
import org.dynjs.debugger.requests.ScriptsRequest;
import org.dynjs.debugger.requests.ScriptsResponse;
import org.dynjs.runtime.SourceProvider;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Bob McWhirter
 */
public class Scripts extends AbstractCommand<ScriptsRequest, ScriptsResponse> {

    public Scripts(Debugger debugger) {
        super(debugger, ScriptsRequest.class, ScriptsResponse.class);
    }

    @Override
    public ScriptsResponse handle(ScriptsRequest request) {
        Set<SourceProvider> allScripts = this.debugger.getSources();
        Set<SourceProvider> selectedScripts = null;

        if (request.getArguments().getIds() == null) {
            selectedScripts = allScripts;
        } else {
            selectedScripts = new HashSet<>();

            int[] ids = request.getArguments().getIds();

            for (SourceProvider each : allScripts) {
                LOOKUP:
                for (int i = 0; i < ids.length; ++i) {
                    if (each.getId() == ids[i]) {
                        selectedScripts.add(each);
                        break LOOKUP;
                    }
                }
            }

        }

        return new ScriptsResponse(request, selectedScripts, request.getArguments().isIncludeSource(), true, true);
    }
}
