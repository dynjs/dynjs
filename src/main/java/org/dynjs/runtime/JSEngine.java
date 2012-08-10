package org.dynjs.runtime;

import java.util.List;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.dynjs.Config;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.parser.ES3Lexer;
import org.dynjs.parser.ES3Parser;
import org.dynjs.parser.ES3Walker;
import org.dynjs.parser.Executor;
import org.dynjs.parser.Statement;
import org.dynjs.parser.SyntaxError;

public class JSEngine {

    private Config config;

    public JSEngine() {
        this( new Config() );
    }

    public JSEngine(Config config) {
        this.config = config;
    }

    public void execute(String program, String filename, int lineNumber) {
        ExecutionContext context = ExecutionContext.createGlobalExecutionContext( this.config );
        JSCompiler compiler = context.getCompiler();
        List<Statement> statements = parseSourceCode( context, program, filename );
        compiler.compileProgram( statements.toArray( new Statement[statements.size()] ) );
    }

    private List<Statement> parseSourceCode(ExecutionContext context, String code, String filename) {
        try {
            final ANTLRStringStream stream = new ANTLRStringStream( code );
            stream.name = filename;
            ES3Lexer lexer = new ES3Lexer( stream );
            return parseSourceCode( context, lexer );
        } catch (RecognitionException e) {
            throw new SyntaxError( e );
        }
    }

    private List<Statement> parseSourceCode(ExecutionContext context, ES3Lexer lexer) throws RecognitionException, SyntaxError {
        CommonTokenStream stream = new CommonTokenStream( lexer );
        ES3Parser parser = new ES3Parser( stream );
        ES3Parser.program_return program = parser.program();
        List<String> errors = parser.getErrors();
        if (!errors.isEmpty()) {
            throw new SyntaxError( errors );
        }
        CommonTree tree = (CommonTree) program.getTree();
        CommonTreeNodeStream treeNodeStream = new CommonTreeNodeStream( tree );
        treeNodeStream.setTokenStream( stream );
        ES3Walker walker = new ES3Walker( treeNodeStream );

        Executor executor = new Executor();
        executor.setBlockManager( context.getBlockManager() );
        walker.setExecutor( executor );
        walker.program();
        return walker.getResult();
    }

}
