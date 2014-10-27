package org.dynjs.runtime;

import org.dynjs.debugger.Debugger;
import org.dynjs.exception.DynJSException;
import org.dynjs.exception.ThrowException;
import org.dynjs.parser.js.ParserException;
import org.dynjs.parser.js.SyntaxError;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.util.concurrent.Executor;

public class Runner<T extends Runner<T>> {

    private final DynJS runtime;
    private Compiler compiler;
    private ExecutionContext context;

    private boolean directEval;

    private JSProgram source;

    private Executor executor;
    private Object result;
    private State state = State.COMPLETE;

    private enum State {
        RUNNING,
        COMPLETE;
    }

    public Runner(DynJS runtime) {
        this.compiler = new Compiler(runtime.getConfig());
        this.runtime = runtime;
    }

    public T forceStrict() {
        this.compiler.forceStrict();
        return (T) this;
    }

    public T forceStrict(boolean forceStrict) {
        this.compiler.forceStrict( forceStrict );
        return (T) this;
    }

    public T directEval() {
        return (T) directEval(true);
    }

    public T directEval(boolean directEval) {
        this.directEval = directEval;
        return (T) this;
    }

    public T withSource(JSProgram source) {
        this.source = source;
        return (T) this;
    }

    public T withExecutor(Executor executor) {
        this.executor = executor;
        return (T) this;
    }

    public T withSource(String source) {
        this.compiler.withSource( source );
        return (T) this;
    }

    public T withSource(Reader source) {
        this.compiler.withSource( source );
        return (T) this;
    }

    public T withSource(File source) throws FileNotFoundException {
        this.compiler.withSource( source );
        return (T) this;
    }

    public T withContext(ExecutionContext context) {
        this.context = context;
        this.compiler.withContext( context );
        return (T) this;
    }

    public T withFileName(String fileName) {
        this.compiler.withFileName( fileName );
        return (T) this;
    }

    protected ExecutionContext executionContext() {
        if ( this.context == null ) {
            return this.runtime.getDefaultExecutionContext();
        }
        return this.context;
    }

    protected JSProgram program() {
        if ( this.source != null ) {
            return this.source;
        }

        return this.compiler.compile();
    }

    public Object result() {
        return this.result;
    }

    public boolean isRunning() {
        return this.state == State.RUNNING;
    }

    public synchronized boolean isComplete() {
        return this.state == State.COMPLETE;
    }

    public synchronized void join() throws InterruptedException {
        while ( this.state != State.COMPLETE ) {
            wait();
        }
    }

    protected Debugger getDebugger() {
        return null;
    }

    protected void setup() {
        // no-op;
    }

    public synchronized Object execute() {
        if ( this.state != State.COMPLETE ) {
            throw new DynJSException( "Running is currently in-use" );
        }

        setup();

        if ( this.executor == null ) {
            this.result = doExecute();
            return this.result;
        }

        this.state = State.RUNNING;

        this.executor.execute( new Runnable() {
            @Override
            public void run() {
                try {
                    Runner.this.result = doExecute();
                } finally {
                    synchronized ( Runner.this ) {
                        Runner.this.state = State.COMPLETE;
                        Runner.this.notifyAll();
                    }
                }
            }
        });

        return null;
    }

    private Object doExecute() {
        try {
            Completion completion = executionContext().execute(program(), getDebugger());
            if (completion.type == Completion.Type.BREAK || completion.type == Completion.Type.CONTINUE) {
                throw new ThrowException(executionContext(), executionContext().createSyntaxError("illegal break or continue"));
            }
            Object v = completion.value;
            if (v instanceof Reference) {
                return ((Reference) v).getValue(context);
            }
            return v;
        } catch (SyntaxError e) {
            throw new ThrowException(executionContext(), executionContext().createSyntaxError(e.getMessage()));
        } catch (ParserException e) {
            throw new ThrowException(executionContext(), e);
        }
    }

    public synchronized Object evaluate() {
        if ( this.state != State.COMPLETE ) {
            throw new DynJSException( "Running is currently in-use" );
        }

        setup();

        if ( this.executor == null ) {
            this.result = doEvaluate();
            return this.result;
        }

        this.state = State.RUNNING;
        this.executor.execute( new Runnable() {
            @Override
            public void run() {
                try {
                    Runner.this.result = doEvaluate();
                } finally {
                    synchronized ( Runner.this ) {
                        Runner.this.state = State.COMPLETE;
                        Runner.this.notifyAll();
                    }
                }
            }
        });

        return null;
    }

    private Object doEvaluate() {
        try {
            return executionContext().eval(program(), this.directEval);
        } catch (SyntaxError e) {
            throw new ThrowException(executionContext(), executionContext().createSyntaxError(e.getMessage()));
        } catch (ParserException e) {
            throw new ThrowException(executionContext(), e);
        }
    }



}
