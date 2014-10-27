package org.dynjs.debugger;

import org.dynjs.debugger.requests.ContinueResponse;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.Runner;

import java.util.concurrent.Executors;

/**
 * @author Bob McWhirter
 */
public class DebugRunner extends Runner<DebugRunner> {

    private final Debugger debugger;

    public DebugRunner(DynJS runtime) {
        super(runtime);
        this.debugger = new Debugger();
    }

    public DebugRunner waitConnect() {
        return waitConnect(true);
    }

    @Override
    public Debugger getDebugger() {
        return debugger;
    }

    public DebugRunner waitConnect(boolean waitConnect) {
        this.debugger.setWaitConnect( waitConnect );
        return this;
    }

    public DebugRunner withListener(DebugListener listener) {
        this.debugger.setListener( listener );
        return this;
    }

    public DebugListener getListener() {
        return this.debugger.getListener();
    }

}
