package org.dynjs.debugger.js;

import org.dynjs.debugger.Debugger;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.GlobalContext;

/**
 * @author Bob McWhirter
 */
public class Debug extends DynObject {

    public Debug(GlobalContext context, Debugger debugger) {
        super( context );

        this.put( "setBreakPoint", new SetBreakPoint( context, debugger ) );
    }

}
