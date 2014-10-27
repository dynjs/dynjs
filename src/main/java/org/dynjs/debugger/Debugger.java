package org.dynjs.debugger;

import org.dynjs.debugger.events.BreakEvent;
import org.dynjs.debugger.requests.ContinueResponse;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.ExecutionContext;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Bob McWhirter
 */
public class Debugger implements DebugConnector {

    public enum StepAction {
        RUN,
        NEXT,
        IN,
        OUT
    }

    private final AtomicBoolean lock = new AtomicBoolean();
    private DebugListener listener;
    private StepAction mode;

    public Debugger() {
        this.mode = StepAction.RUN;
    }

    void setWaitConnect(boolean waitConnect) {
        this.mode = StepAction.NEXT;
    }

    public DebugListener getListener() {
        return this.listener;
    }

    public void setListener(DebugListener listener) {
        this.listener = listener;
    }

    @Override
    public void debug(ExecutionContext context, Statement statement) throws InterruptedException {
        if ( shouldBreak( statement ) ) {
            doBreak();
        }
    }

    public void CONTINUE(StepAction action) {
        synchronized ( this.lock ) {
            this.lock.set( false );
            this.lock.notifyAll();
        }
    }

    private void doBreak() throws InterruptedException {
        System.err.println("blocking");
        this.listener.on(new BreakEvent(this));
        synchronized (this.lock) {
            while (this.lock.get()) {
                this.lock.wait();
            }
            this.lock.set(false);
        }
        System.err.println("running");
    }

    private boolean shouldBreak(Statement statement) {
        if (this.mode != Debugger.StepAction.RUN) {
            synchronized (this.lock) {
                this.lock.set(true);
            }
            System.err.println("should break");
            return true;
        }

        System.err.println("should not break");
        return false;
    }

}
