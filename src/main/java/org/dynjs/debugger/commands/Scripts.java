package org.dynjs.debugger.commands;

import org.dynjs.debugger.Debugger;
import org.dynjs.debugger.model.Script;
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
        Set<SourceProvider> allSources = this.debugger.getSources();
        Set<Script> selectedScripts = new HashSet<>();

        if (request.getIds() == null) {
            for (SourceProvider each : allSources) {
                selectedScripts.add(new Script(each, request.isIncludeSource()));
            }
        } else {
            selectedScripts = new HashSet<>();

            int[] ids = request.getIds();

            for (SourceProvider each : allSources) {
                LOOKUP:
                for (int i = 0; i < ids.length; ++i) {
                    if (each.getId() == ids[i]) {
                        selectedScripts.add(new Script(each, request.isIncludeSource()));
                        break LOOKUP;
                    }
                }
            }

        }

        return new ScriptsResponse(request, selectedScripts, request.isIncludeSource(), true, this.debugger.isRunning());
    }
}
