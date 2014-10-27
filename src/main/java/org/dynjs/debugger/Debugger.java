package org.dynjs.debugger;

import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.Runner;

/**
 * @author Bob McWhirter
 */
public class Debugger extends Runner {

    private boolean waitConnect;

    public Debugger(DynJS runtime) {
        super(runtime);
    }

    public Debugger waitConnect() {
        return waitConnect(true);
    }

    public Debugger waitConnect(boolean waitConnect) {
        this.waitConnect = waitConnect;
        return this;
    }


}
