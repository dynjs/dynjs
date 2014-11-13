package org.dynjs.parser.js;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.dynjs.compiler.CompilationContext;
import org.dynjs.parser.ast.ProgramTree;
import org.dynjs.runtime.ExecutionContext;

public class JavascriptParser {

    private CompilationContext context;
    private ASTFactory factory;

    public JavascriptParser(CompilationContext context) {
        this(context, new ASTFactory());
    }

    public JavascriptParser(CompilationContext context, ASTFactory factory) {
        this.context = context;
        this.factory = factory;
    }

    public ProgramTree parse(File file) throws IOException {
        return parse( file, false );
        
    }
    public ProgramTree parse(File file, boolean forceStrict) throws IOException {
        FileReader reader = new FileReader(file);
        try {
            return parse(reader, file.getName(), forceStrict);
        } finally {
            reader.close();
        }
    }

    public ProgramTree parse(String source) {
        return parse(source, null);
    }

    public ProgramTree parse(String source, String fileName) {
        return parse( source, fileName, false );
    }
    
    public ProgramTree parse(String source, String fileName, boolean forceStrict) {
        StringReader reader = new StringReader(source);
        try {
            return parse(reader, fileName, forceStrict);
        } finally {
            reader.close();
        }
    }

    public ProgramTree parse(Reader source) {
        return parse( source, false );
    }
    
    public ProgramTree parse(Reader source, boolean forceStrict) {
        return parse(source, null, forceStrict);
    }
    
    public ProgramTree parse(Reader source, String fileName) {
        return parse( source, fileName, false );
    }

    public ProgramTree parse(Reader source, String fileName, boolean forceStrict) {
        try {
            return parse(new CircularCharBuffer(source), fileName, forceStrict);
        } catch (IOException e) {
            throw new ParserException(e);
        }
    }
    
    public ProgramTree parse(CharStream source, String fileName) {
        return parse( source, fileName, false );
    }

    public ProgramTree parse(CharStream source, String fileName, boolean forceStrict) {
        Lexer lexer = new Lexer(source);
        lexer.setFileName(fileName);
        TokenStream tokens = new TokenQueue(lexer);
        Parser parser = new Parser(this.context, this.factory, tokens);
        parser.forceStrict( forceStrict );
        return parser.program();
    }

}
