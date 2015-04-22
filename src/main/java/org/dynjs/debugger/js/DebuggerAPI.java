package org.dynjs.debugger.js;

import org.dynjs.debugger.Debugger;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.GlobalContext;

/**
 * @author Bob McWhirter
 */
public class DebuggerAPI extends DynObject {

    public DebuggerAPI(GlobalContext context, Debugger debugger) {
        super( context );

        this.put( "Debug", new Debug( context, debugger ) );
    }
}
