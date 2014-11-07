package org.dynjs.debugger;

import io.netty.channel.ChannelHandler;
import org.dynjs.debugger.commands.AbstractCommand;
import org.dynjs.debugger.commands.Continue;
import org.dynjs.debugger.commands.Source;
import org.dynjs.debugger.events.BreakEvent;
import org.dynjs.debugger.events.ScriptInfo;
import org.dynjs.debugger.requests.Request;
import org.dynjs.debugger.requests.Response;
import org.dynjs.parser.Statement;
import org.dynjs.parser.ast.FunctionDeclaration;
import org.dynjs.parser.ast.ProgramTree;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.SourceProvider;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Bob McWhirter
 */
public class Debugger {


    public enum StepAction {
        RUN,
        NEXT,
        IN,
        OUT
    }

    private String fileName;
    private ExecutionContext currentContext;
    private ExecutionContext basisContext;
    private List<ExecutionContext> contextStack = new LinkedList<>();

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
        register("continue", new Continue(this));
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

    public void enterContext(ExecutionContext context) {
        this.contextStack.add(0, context);
    }

    public void exitContext(ExecutionContext context) {
        this.contextStack.remove(0);
    }

    public void debug(ExecutionContext context, Statement statement, Statement previousStatement) throws InterruptedException {
        this.currentContext = context;
        if (statement.getPosition() != null) {
            this.fileName = statement.getPosition().getFileName();
        }
        if (shouldBreak(statement, previousStatement)) {
            doBreak(context, statement);
        }
    }

    public long setBreakPoint(String fileName, long line, long column) {
        LineBreakPoint breakPoint = new LineBreakPoint(this.breakPointCounter.incrementAndGet(), fileName, line, column);
        this.breakPoints.add(breakPoint);
        return breakPoint.getNumber();
    }

    private void setMode(StepAction action) {
        this.mode = action;
        unbreak();
    }

    private void unbreak() {
        synchronized (this.lock) {
            this.lock.set(false);
            this.lock.notifyAll();
        }
    }

    public void run() {
        setMode(StepAction.RUN);
    }

    public void stepIn() {
        setMode(StepAction.IN);
    }

    public void stepOut() {
        setMode(StepAction.OUT);
    }

    public void stepNext() {
        setMode(StepAction.NEXT);
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

        this.basisContext = this.currentContext;

        setBreak();

        ScriptInfo script = new ScriptInfo(statement.getPosition().getFileName());
        this.listener.on(new BreakEvent(this, statement.getPosition().getLine() - 1, statement.getPosition().getColumn(), script));
        synchronized (this.lock) {
            while (this.lock.get()) {
                this.lock.wait();
            }
            this.lock.set(false);
        }
    }

    private boolean shouldBreak(Statement statement, Statement previousStatement) {
        boolean result = false;
        switch (this.mode) {
            case RUN:
                result = checkBreakpoints(statement, previousStatement);
                break;
            case NEXT:
                result = ( isCurrentlyInBasisContext() || hasExitedBasisContext() );
                break;
            case IN:
                result = ! hasExitedBasisContext();
                break;
            case OUT:
                result = hasExitedBasisContext();
                break;
        }
        return result;
    }

    private void setBreak() {
        synchronized (this.lock) {
            this.lock.set(true);
        }
    }

    private boolean checkBreakpoints(Statement statement, Statement previousStatement) {
        for (LineBreakPoint each : this.breakPoints) {
            if (each.shouldBreak(statement, previousStatement)) {
                return true;
            }
        }

        return false;
    }

    private boolean isCurrentlyInBasisContext() {
        return this.basisContext == this.currentContext;
    }

    private boolean hasExitedBasisContext() {

        for ( ExecutionContext cur : this.contextStack ) {
            if ( cur == this.basisContext ) {
                return false;
            }
        }

        return true;
    }

}
