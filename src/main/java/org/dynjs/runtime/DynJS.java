package org.dynjs.runtime;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.dynjs.Config;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.exception.DynJSException;
import org.dynjs.exception.ThrowException;
import org.dynjs.parser.ES3Lexer;
import org.dynjs.parser.ES3Parser;
import org.dynjs.parser.ES3Walker;
import org.dynjs.parser.Executor;
import org.dynjs.parser.SyntaxError;
import org.dynjs.parser.ast.BlockStatement;
import org.dynjs.runtime.builtins.types.BuiltinSyntaxError;

public class DynJS {

    private Config config;
    private JSCompiler compiler;
    private ExecutionContext context;

    public DynJS() {
        this(new Config());
    }

    public DynJS(Config config) {
        this.config = config;
        this.compiler = new JSCompiler(config);
        this.context = ExecutionContext.createGlobalExecutionContext(this);
    }

    public Config getConfig() {
        return this.config;
    }

    public JSCompiler getCompiler() {
        return this.compiler;
    }

    public ExecutionContext getExecutionContext() {
        return this.context;
    }

    public Object execute(File file) throws IOException {
        return execute(file, false);
    }

    public Object execute(File file, boolean forceStrict) throws IOException {
        return execute(this.context, file, forceStrict);
    }

    public Object execute(ExecutionContext execContext, File file) throws IOException {
        return execute(execContext, file, false);
    }

    public Object execute(ExecutionContext execContext, File file, boolean forceStrict) throws IOException {
        FileInputStream in = new FileInputStream(file);
        try {
            return execute(execContext, in, file.getPath(), forceStrict);
        } finally {
            in.close();
        }
    }

    public Object execute(InputStream program, String filename) throws IOException {
        return execute(program, filename, false);
    }

    public Object execute(InputStream program, String filename, boolean forceStrict) throws IOException {
        return execute(this.context, program, filename, forceStrict);
    }

    public Object execute(ExecutionContext execContext, InputStream program, String filename) throws IOException {
        return execute(execContext, program, filename, false);

    }

    public Object execute(ExecutionContext execContext, InputStream program, String filename, boolean forceStrict) throws IOException {
        JSProgram programObj = compile(execContext, program, filename, forceStrict);
        Completion completion = execContext.execute(programObj);
        return completion.value;
    }

    public Object execute(String program, String filename, int lineNumber) {
        return execute(program, filename, lineNumber, false);

    }

    public Object execute(String program, String filename, int lineNumber, boolean forceStrict) {
        JSProgram programObj = compile(this.context, program, filename, forceStrict);
        Completion completion = this.context.execute(programObj);
        if ( completion.type == Completion.Type.BREAK || completion.type == Completion.Type.CONTINUE ) {
            throw new ThrowException( this.context, this.context.createSyntaxError( "illegal break or continue" ));
        }
        Object v = completion.value;
        if (v instanceof Reference) {
            return ((Reference) v).getValue(context);
        }
        return v;
    }

    public Object execute(String program) {
        return execute(program, null, 0);
    }

    public Object evaluate(String... code) {
        StringBuffer fullCode = new StringBuffer();

        for (int i = 0; i < code.length; ++i) {
            fullCode.append(code[i]);
            fullCode.append("\n");
        }
        return execute(fullCode.toString(), null, 0);
    }

    public JSProgram compile(String program) {
        return compile(this.context, program, null, false);
    }

    public JSProgram compile(String program, String filename) {
        return compile(this.context, program, filename, false);
    }

    public JSProgram compile(ExecutionContext execContext, String program, String filename, boolean forceStrict) {
        JSCompiler compiler = execContext.getCompiler();
        BlockStatement statements = parseSourceCode(execContext, program, filename);
        if (statements == null) {
            return new NullProgram(filename);
        }
        JSProgram programObj = compiler.compileProgram(statements, forceStrict);
        return programObj;
    }

    public JSProgram compile(InputStream program, String filename) throws IOException {
        return compile(this.context, program, filename, false);
    }

    public JSProgram compile(ExecutionContext execContext, InputStream program, String filename, boolean forceStrict) throws IOException {
        JSCompiler compiler = execContext.getCompiler();
        BlockStatement statements = parseSourceCode(execContext, program, filename);
        if (statements == null) {
            return new NullProgram(filename);
        }
        JSProgram programObj = compiler.compileProgram(statements, forceStrict);
        return programObj;
    }

    private BlockStatement parseSourceCode(ExecutionContext context, String code, String filename) {
        try {
            final ANTLRStringStream stream = new ANTLRStringStream(code);
            stream.name = filename;
            ES3Lexer lexer = new ES3Lexer(stream);
            return parseSourceCode(context, lexer);
        } catch (RecognitionException e) {
            throw new SyntaxError(e);
        }
    }

    private BlockStatement parseSourceCode(ExecutionContext context, InputStream code, String filename) throws IOException {
        try {
            final ANTLRStringStream stream = new ANTLRInputStream(code);
            stream.name = filename;
            ES3Lexer lexer = new ES3Lexer(stream);
            return parseSourceCode(context, lexer);
        } catch (RecognitionException e) {
            throw new SyntaxError(e);
        }
    }

    private BlockStatement parseSourceCode(ExecutionContext context, ES3Lexer lexer) throws RecognitionException, SyntaxError {
        CommonTokenStream stream = new CommonTokenStream(lexer);
        ES3Parser parser = new ES3Parser(stream);
        ES3Parser.program_return program = parser.program();
        List<String> errors = parser.getErrors();
        if (!errors.isEmpty()) {
            throw new SyntaxError(errors);
        }
        CommonTree tree = (CommonTree) program.getTree();
        if (tree == null) {
            return null;
        }
        // System.err.println( ">>>" );
        // dump(tree);
        // System.err.println( "<<<" );
        CommonTreeNodeStream treeNodeStream = new CommonTreeNodeStream(tree);
        treeNodeStream.setTokenStream(stream);
        ES3Walker walker = new ES3Walker(treeNodeStream);

        Executor executor = new Executor();
        executor.setBlockManager(context.getBlockManager());
        walker.setExecutor(executor);
        walker.program();
        return walker.getResult();
    }

    private void dump(CommonTree tree) {
        dump(tree, "");
    }

    private void dump(CommonTree tree, String indent) {
        System.err.println(indent + tree.getText());

        if (tree.getChildCount() > 0) {
            Iterator<?> childIter = tree.getChildren().iterator();

            while (childIter.hasNext()) {
                CommonTree child = (CommonTree) childIter.next();
                dump(child, indent + "  ");
            }
        }

    }

}
