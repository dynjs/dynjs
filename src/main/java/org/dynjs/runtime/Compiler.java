package org.dynjs.runtime;

import org.dynjs.Config;
import org.dynjs.compiler.CompilationContext;
import org.dynjs.compiler.DefaultCompilationContext;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.ir.Builder;
import org.dynjs.parser.ast.ProgramTree;
import org.dynjs.parser.js.JavascriptParser;
import org.dynjs.parser.js.ParserException;

import java.io.*;

/**
 * @author Bob McWhirter
 */
public class Compiler {

    private final Config config;
    private CompilationContext compilationContext;

    private Reader source;
    private boolean shouldClose;
    private String fileName;

    private boolean forceStrict;

    public Compiler() {
        this.config = new Config();
    }

    public Compiler(Config config) {
        this.config = config;
    }

    public Compiler withContext(ExecutionContext context) {
        this.compilationContext = context;
        return this;
    }

    public Compiler forceStrict() {
        return forceStrict(true);
    }

    public Compiler forceStrict(boolean forceStrict) {
        this.forceStrict = forceStrict;
        return this;
    }

    public Compiler withSource(String source) {
        this.source = new StringReader(source);
        this.shouldClose = true;
        return this;
    }

    public Compiler withSource(Reader source) {
        this.source = source;
        this.shouldClose = false;
        return this;
    }

    public Compiler withSource(File source) throws FileNotFoundException {
        this.source = new FileReader(source);
        this.shouldClose = true;
        this.fileName = source.getName();
        return this;
    }

    public Compiler withFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public JSProgram compile() {
        return compile(parse());
    }

    public ProgramTree parse() {
        JavascriptParser parser = new JavascriptParser(compilationContext());
        try {
            return parser.parse(this.source, this.fileName, this.forceStrict);
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

    protected CompilationContext compilationContext() {
        if (this.compilationContext == null) {
            this.compilationContext = new DefaultCompilationContext(this.config);
        }
        if ( this.compilationContext.getBlockManager() == null ) {
            this.compilationContext = new DefaultCompilationContext(this.compilationContext);
        }
        return this.compilationContext;
    }


    protected JSProgram compile(ProgramTree tree) {
        // FIXME: getCompiler will go away so just add special IR check for now.

        final Config.CompileMode compileMode = compilationContext().getConfig().getCompileMode();
        if (compileMode == Config.CompileMode.IR) {
            return Builder.compile(compilationContext(), tree);
        }

        JSCompiler compiler = compilationContext().getCompiler();
        return compiler.compileProgram(compilationContext(), tree, this.forceStrict);
    }

}
