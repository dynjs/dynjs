package org.dynjs.debugger;

import io.netty.channel.ChannelHandler;
import org.dynjs.debugger.commands.AbstractCommand;
import org.dynjs.debugger.commands.Source;
import org.dynjs.debugger.events.BreakEvent;
import org.dynjs.debugger.events.ScriptInfo;
import org.dynjs.debugger.requests.Request;
import org.dynjs.debugger.requests.Response;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.ExecutionContext;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

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

    private String fileName;
    private ExecutionContext currentContext;

    private final AtomicBoolean lock = new AtomicBoolean();
    private DebugListener listener;
    private StepAction mode;

    private Map<String, AbstractCommand> commands = new HashMap<>();
    private List<ChannelHandler> handlers = new ArrayList<>();

    private AtomicLong breakPointCounter = new AtomicLong();
    private List<LineBreakPoint> breakPoints = new ArrayList<>();

    public Debugger() {
        this.mode = StepAction.RUN;
        register("source", new Source(this));
        //register("continue", new ContinueCommand(this));
    }

    void register(String name, AbstractCommand command) {
        this.commands.put(name, command);
    }

    public AbstractCommand getCommand(String command) {
        return this.commands.get(command);
    }

    public Collection<AbstractCommand> getCommands() {
        return this.commands.values();
    }

    public void setWaitConnect(boolean waitConnect) {
        this.mode = StepAction.NEXT;
    }

    public DebugListener getListener() {
        return this.listener;
    }

    public synchronized void setListener(DebugListener listener) {
        this.listener = listener;
        this.notifyAll();
    }

    public ExecutionContext getCurrentContext() {
        return this.currentContext;
    }

    @Override
    public void debug(ExecutionContext context, Statement statement) throws InterruptedException {
        this.currentContext = context;
        if (statement.getPosition() != null) {
            this.fileName = statement.getPosition().getFileName();
        }
        if (shouldBreak(statement)) {
            doBreak(context, statement);
        }
    }

    @Override
    public long setBreakPoint(String fileName, long line, long column) {
        LineBreakPoint breakPoint = new LineBreakPoint(this.breakPointCounter.incrementAndGet(), fileName, line, column);
        this.breakPoints.add(breakPoint);
        return breakPoint.getNumber();
    }

    public String getFileName() {
        return this.fileName;
    }

    public <REQUEST extends Request<RESPONSE>, RESPONSE extends Response> RESPONSE handle(REQUEST request) {
        AbstractCommand<REQUEST, RESPONSE> command = getCommand(request.getCommand());
        if (command != null) {
            return command.handle(request);
        }
        return null;
    }

    private void doBreak(ExecutionContext context, Statement statement) throws InterruptedException {

        synchronized (this) {
            while (this.listener == null) {
                this.wait();
            }
        }
        ScriptInfo script = new ScriptInfo(statement.getPosition().getFileName());
        System.err.println( "BREAK ON " + ( statement.getPosition().getLine() -1 ) );
        this.listener.on(new BreakEvent(this, statement.getPosition().getLine() - 1, statement.getPosition().getColumn(), script));
        synchronized (this.lock) {
            while (this.lock.get()) {
                this.lock.wait();
            }
            this.lock.set(false);
        }
    }

    private boolean shouldBreak(Statement statement) {
        if (this.mode == Debugger.StepAction.RUN) {
            for (LineBreakPoint each : this.breakPoints) {
                if (each.shouldBreak(statement)) {
                    synchronized (this.lock) {
                        this.lock.set(true);
                    }
                    return true;
                }
            }
        }

        return false;
    }

}
