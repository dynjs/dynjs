package org.dynjs.debugger;

import org.dynjs.debugger.agent.handlers.ContinueHandler;
import org.dynjs.debugger.commands.AbstractCommand;
import org.dynjs.debugger.commands.ContinueCommand;
import org.dynjs.debugger.events.BreakEvent;
import org.dynjs.debugger.events.ScriptInfo;
import org.dynjs.debugger.requests.ContinueRequest;
import org.dynjs.debugger.requests.ContinueResponse;
import org.dynjs.debugger.requests.Request;
import org.dynjs.debugger.requests.Response;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.ExecutionContext;

import java.util.HashMap;
import java.util.Map;
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

    private Map<String, AbstractCommand> commands = new HashMap<>();

    public Debugger() {
        this.mode = StepAction.RUN;
        register("continue", new ContinueCommand(this));
    }

    void register(String name, AbstractCommand command) {
        this.commands.put(name, command);
    }

    public AbstractCommand getCommand(String command) {
        return this.commands.get(command);
    }

    public void setWaitConnect(boolean waitConnect) {
        this.mode = StepAction.NEXT;
    }

    public DebugListener getListener() {
        return this.listener;
    }

    public synchronized void setListener(DebugListener listener) {
        System.err.println( this + " // " + listener );
        this.listener = listener;
        this.notifyAll();
    }

    @Override
    public void debug(ExecutionContext context, Statement statement) throws InterruptedException {
        if (shouldBreak(statement)) {
            doBreak(context);
        }
    }

    public <REQUEST extends Request<RESPONSE>, RESPONSE extends Response> RESPONSE handle(REQUEST request) {
        AbstractCommand<REQUEST, RESPONSE> command = getCommand(request.getCommand());
        if (command != null) {
            return command.handle(request);
        }
        return null;
    }

    private void doBreak(ExecutionContext context) throws InterruptedException {

        synchronized (this) {
            while (this.listener == null) {
                System.err.println( "awaiting connect" );
                this.wait();
            }
        }
        System.err.println("blocking, send break");

        ScriptInfo script = new ScriptInfo( context.getFileName() );
        this.listener.on(new BreakEvent(this, context.getLineNumber(), context.getColumnNumber(), script));
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
