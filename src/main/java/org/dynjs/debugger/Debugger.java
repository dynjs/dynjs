package org.dynjs.debugger;

import io.netty.channel.ChannelHandler;
import org.dynjs.debugger.commands.*;
import org.dynjs.debugger.events.BreakEvent;
import org.dynjs.debugger.model.Breakpoint;
import org.dynjs.debugger.model.Frame;
import org.dynjs.debugger.model.Script;
import org.dynjs.debugger.requests.Request;
import org.dynjs.debugger.requests.Response;
import org.dynjs.parser.Statement;
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
        OUT,
        SUSPEND,
    }

    private ExecutionContext globalContext;

    private String fileName;
    private ExecutionContext basisContext;
    private List<ExecutionContext> contextStack = new LinkedList<>();

    private final AtomicBoolean lock = new AtomicBoolean();
    private DebugListener listener;
    private StepAction mode;

    private Map<String, AbstractCommand> commands = new HashMap<>();
    private List<ChannelHandler> handlers = new ArrayList<>();

    private AtomicLong breakPointCounter = new AtomicLong();
    private List<Breakpoint> breakPoints = new ArrayList<>();

    private ReferenceManager referenceManager = new ReferenceManager();

    private Set<SourceProvider> sources = new HashSet<>();

    private boolean paused = false;
    private Statement currentStatement = null;

    public Debugger() {
        this.mode = StepAction.RUN;
        register("version", new Version(this));
        register("scripts", new Scripts(this));
        register("source", new Source(this));
        register("suspend", new Suspend(this));
        register("continue", new Continue(this));
        register("evaluate", new Evaluate(this));
        register("lookup", new Lookup(this));
        register("setbreakpoint", new SetBreakpoint(this));
        register("listbreakpoints", new ListBreakpoints(this));
        register("clearbreakpoint", new ClearBreakpoint(this));
        register("backtrace", new Backtrace(this));
    }

    public void setGlobalContext(ExecutionContext globalContext) {
        this.globalContext = globalContext;
    }

    public ExecutionContext getGlobalContext() {
        return this.globalContext;
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

    public ReferenceManager getReferenceManager() {
        return this.referenceManager;
    }

    public DebugListener getListener() {
        return this.listener;
    }

    public synchronized void setListener(DebugListener listener) {
        this.listener = listener;
        this.notifyAll();
    }

    public List<Frame> getFrames(int fromFrame, int toFrame) {

        int frameIndex = 0;
        List<Frame> frames = new ArrayList<>();

        for (ExecutionContext each : contextStack) {
            if (frameIndex >= fromFrame && frameIndex <= toFrame) {
                frames.add(new Frame(frameIndex, each));
            }
            ++frameIndex;
            if (frameIndex > toFrame) {
                break;
            }
        }

        return frames;
    }

    public ExecutionContext getCurrentContext() {
        if (this.contextStack.isEmpty()) {
            return null;
        }
        return this.contextStack.get(0);
    }

    public ExecutionContext getContext(int frame) {
        return this.contextStack.get(frame);
    }

    public synchronized void enterContext(ExecutionContext context) {
        this.contextStack.add(0, context);
        this.sources.add(context.getSource());
    }

    public synchronized void exitContext(ExecutionContext context) {
        this.contextStack.remove(0);
    }

    public Set<SourceProvider> getSources() {
        return this.sources;
    }

    public boolean isRunning() {
        return !this.paused;
    }

    public void debug(ExecutionContext context, Statement statement, Statement previousStatement) throws InterruptedException {
        if (this.paused) {
            // only got here because of an `evaluate` ?
            // so do not debug...
            return;
        }

        this.currentStatement = statement;
        if (statement.getPosition() != null) {
            this.fileName = statement.getPosition().getFileName();
        }
        if (shouldBreak(statement, previousStatement)) {
            this.paused = true;
            try {
                doBreak(context, statement);
            } finally {
                this.paused = false;
            }
        }
    }

    public void setBreakpoint(Breakpoint breakpoint) {
        this.breakPoints.add(breakpoint);
    }

    public boolean removeBreakpoint(long number) {
        Iterator<Breakpoint> iter = this.breakPoints.iterator();

        while (iter.hasNext()) {
            Breakpoint each = iter.next();
            if (each.getNumber() == number) {
                iter.remove();
                return true;
            }
        }

        return false;
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

    public void suspend() {
        this.mode = StepAction.SUSPEND;
        if (this.paused) {
            this.listener.on(new BreakEvent(this, this.currentStatement.getPosition().getLine() - 1, this.currentStatement.getPosition().getColumn(), new Script(getCurrentContext().getSource(), false)));
        }
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

        this.paused = true;

        /*
        synchronized (this) {
            while (this.listener == null) {
                this.wait();
            }
        }
        */

        this.basisContext = getCurrentContext();

        setBreak();

        if (this.listener != null) {
            this.listener.on(new BreakEvent(this, statement.getPosition().getLine() - 1, statement.getPosition().getColumn(), new Script(getCurrentContext().getSource(), false)));
        }
        synchronized (this.lock) {
            while (this.lock.get()) {
                this.lock.wait();
            }
            this.lock.set(false);
            this.referenceManager.reset();
            this.paused = false;
        }
    }

    private boolean shouldBreak(Statement statement, Statement previousStatement) {
        boolean result = false;
        switch (this.mode) {
            case SUSPEND:
                result = true;
                break;
            case RUN:
                result = checkBreakpoints(statement, previousStatement);
                break;
            case NEXT:
                result = (isCurrentlyInBasisContext() || hasExitedBasisContext());
                break;
            case IN:
                result = !hasExitedBasisContext();
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
        for (Breakpoint each : this.breakPoints) {
            if (each.shouldBreak(statement, previousStatement)) {
                return true;
            }
        }

        return false;
    }

    public List<Breakpoint> getBreakPoints() {
        return this.breakPoints;
    }

    private boolean isCurrentlyInBasisContext() {
        return this.basisContext == getCurrentContext();
    }

    private boolean hasExitedBasisContext() {

        for (ExecutionContext cur : this.contextStack) {
            if (cur == this.basisContext) {
                return false;
            }
        }

        return true;
    }

}
