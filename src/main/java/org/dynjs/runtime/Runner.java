package org.dynjs.runtime;

import org.dynjs.exception.ThrowException;
import org.dynjs.parser.js.ParserException;
import org.dynjs.parser.js.SyntaxError;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Reader;

public class Runner {

    private final DynJS runtime;
    private Compiler compiler;
    private ExecutionContext context;

    private boolean directEval;

    private JSProgram source;

    public Runner(DynJS runtime) {
        this.compiler = new Compiler(runtime.getConfig());
        this.runtime = runtime;
    }

    public Runner forceStrict() {
        this.compiler.forceStrict();
        return this;
    }

    public Runner forceStrict(boolean forceStrict) {
        this.compiler.forceStrict( forceStrict );
        return this;
    }

    public Runner directEval() {
        return directEval(true);
    }

    public Runner directEval(boolean directEval) {
        this.directEval = directEval;
        return this;
    }

    public Runner withSource(JSProgram source) {
        this.source = source;
        return this;
    }

    public Runner withSource(String source) {
        this.compiler.withSource( source );
        return this;
    }

    public Runner withSource(Reader source) {
        this.compiler.withSource( source );
        return this;
    }

    public Runner withSource(File source) throws FileNotFoundException {
        this.compiler.withSource( source );
        return this;
    }

    public Runner withContext(ExecutionContext context) {
        this.context = context;
        this.compiler.withContext( context );
        return this;
    }

    public Runner withFileName(String fileName) {
        this.compiler.withFileName( fileName );
        return this;
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

    public Object execute() {
        try {
            Completion completion = executionContext().execute(program());
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

    public Object evaluate() {
        try {
            return executionContext().eval(program(), this.directEval);
        } catch (SyntaxError e) {
            throw new ThrowException(executionContext(), executionContext().createSyntaxError(e.getMessage()));
        } catch (ParserException e) {
            throw new ThrowException(executionContext(), e);
        }
    }



}
