package org.dynjs.runtime;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.dynjs.Config;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.exception.ThrowException;
import org.dynjs.ir.Builder;
import org.dynjs.parser.ast.ProgramTree;
import org.dynjs.parser.js.JavascriptParser;
import org.dynjs.parser.js.ParserException;
import org.dynjs.parser.js.SyntaxError;

public class Runner {

    private ExecutionContext context;
    private String fileName;
    private Reader source;
    private boolean shouldClose = false;
    private boolean forceStrict = false;
    private boolean directEval = true;

    Runner(ExecutionContext context) {
        withContext(context);
    }

    public Runner forceStrict() {
        return forceStrict(true);
    }

    public Runner forceStrict(boolean forceStrict) {
        this.forceStrict = forceStrict;
        return this;
    }

    public Runner directEval() {
        return directEval(true);
    }

    public Runner directEval(boolean directEval) {
        this.directEval = directEval;
        return this;
    }

    public Runner withSource(String source) {
        this.source = new StringReader(source);
        this.shouldClose = true;
        return this;
    }

    public Runner withSource(Reader source) {
        this.source = source;
        this.shouldClose = false;
        return this;
    }

    public Runner withSource(File source) throws FileNotFoundException {
        this.source = new FileReader(source);
        this.shouldClose = true;
        this.fileName = source.getName();
        return this;
    }

    public Runner withContext(ExecutionContext context) {
        this.context = context;
        return this;
    }

    public Runner withFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public Object execute() {
        try {
            ProgramTree tree = parseSourceCode();
            JSProgram program = compile(tree);

            Completion completion = this.context.execute(program);
            if (completion.type == Completion.Type.BREAK || completion.type == Completion.Type.CONTINUE) {
                throw new ThrowException(this.context, this.context.createSyntaxError("illegal break or continue"));
            }
            Object v = completion.value;
            if (v instanceof Reference) {
                return ((Reference) v).getValue(context);
            }
            return v;
        } catch (SyntaxError e) {
            throw new ThrowException(this.context, this.context.createSyntaxError(e.getMessage()));
        } catch (ParserException e) {
            throw new ThrowException(this.context, e);
        }
    }

    public Object evaluate() {
        try {
            ProgramTree tree = parseSourceCode();
            JSProgram program = compile(tree);
            return this.context.eval(program, this.directEval);
        } catch (SyntaxError e) {
            throw new ThrowException(this.context, this.context.createSyntaxError(e.getMessage()));
        } catch (ParserException e) {
            throw new ThrowException(this.context, e);
        }
    }

    public ProgramTree parseSourceCode() {
        JavascriptParser parser = new JavascriptParser( this.context );
        try {
            return parser.parse(this.source, this.fileName, this.forceStrict );
        } finally {
            if (this.shouldClose) {
                try {
                    this.source.close();
                } catch (IOException e) {
                    throw new ParserException(e);
                }
            }
        }
    }

    private JSProgram compile(ProgramTree tree) {
        // FIXME: getCompiler will go away so just add special IR check for now.
        final Config.CompileMode compileMode = context.getConfig().getCompileMode();
        if (compileMode == Config.CompileMode.IR) {
            return Builder.compile(tree);
        }

        JSCompiler compiler = this.context.getCompiler();
        return compiler.compileProgram(this.context, tree, this.forceStrict);
    }

}
