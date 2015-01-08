package org.dynjs.runtime;

import org.dynjs.Config;
import org.dynjs.compiler.CompilationContext;
import org.dynjs.compiler.DefaultCompilationContext;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.debugger.Debugger;
import org.dynjs.ir.Builder;
import org.dynjs.parser.ast.ProgramTree;
import org.dynjs.parser.js.JavascriptParser;
import org.dynjs.parser.js.ParserException;
import org.dynjs.runtime.source.FileSourceProvider;
import org.dynjs.runtime.source.StringSourceProvider;

import java.io.*;

/**
 * @author Bob McWhirter
 */
public class Compiler {

    private final Config config;
    private CompilationContext compilationContext;

    private SourceProvider sourceProvider;
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
        this.sourceProvider = new StringSourceProvider(source);
        return this;
    }

    public Compiler withSource(SourceProvider source) {
        this.sourceProvider = source;
        return this;
    }

    public Compiler withSource(File source) throws IOException {
        this.sourceProvider = new FileSourceProvider(source);
        return this;
    }

    public Compiler withFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public JSProgram compile() throws IOException {
        JSProgram program = compile(parse());
        return program;
    }

    public ProgramTree parse() throws IOException {
        JavascriptParser parser = new JavascriptParser(compilationContext());
        Reader source = null;
        try {
            if ( this.fileName != null ) {
                this.sourceProvider.setName( this.fileName );
            } else {
                this.fileName = this.sourceProvider.getName();
            }
            source = this.sourceProvider.openReader();
            ProgramTree tree = parser.parse(source, this.fileName, this.forceStrict);
            tree.setSource( this.sourceProvider );
            this.fileName = null;
            return tree;
        } finally {
            if (source != null) {
                source.close();
            }
        }
    }

    protected CompilationContext compilationContext() {
        if (this.compilationContext == null) {
            this.compilationContext = new DefaultCompilationContext(this.config);
        }
        if (this.compilationContext.getBlockManager() == null) {
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
