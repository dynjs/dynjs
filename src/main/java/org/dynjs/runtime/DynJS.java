/**
 *  Copyright 2012 Douglas Campos, and individual contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.dynjs.runtime;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import me.qmx.jitescript.CodeBlock;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.dynjs.api.Scope;
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.parser.ES3Lexer;
import org.dynjs.parser.ES3Parser;
import org.dynjs.parser.ES3Walker;
import org.dynjs.parser.Executor;
import org.dynjs.parser.Statement;
import org.dynjs.parser.SyntaxError;
import org.dynjs.runtime.loader.Builtin;

public class DynJS {

    private final DynJSCompiler compiler;
    private final DynJSConfig config;

    public DynJS(DynJSConfig config) {
        this.config = config;
        compiler = new DynJSCompiler(this.config);
    }

    public void eval(DynThreadContext context, String expression) {
        execute(context, parseSourceCode(context, expression, null));
    }

    public void eval(DynThreadContext context, String expression, String filename) {
        execute(context, parseSourceCode(context, expression, filename));
    }

    public void eval(DynThreadContext context, InputStream is) {
        execute(context, parseSourceCode(context, is, null));
    }

    public void eval(DynThreadContext context, InputStream is, String filename) {
        execute(context, parseSourceCode(context, is, filename));
    }

    private List<Statement> parseSourceCode(DynThreadContext context, String code, String filename) {
        try {
            final ANTLRStringStream stream = new ANTLRStringStream(code);
            stream.name = filename;
            ES3Lexer lexer = new ES3Lexer(stream);
            return parseSourceCode(context, lexer);
        } catch (RecognitionException e) {
            throw new SyntaxError(e);
        }
    }

    private List<Statement> parseSourceCode(DynThreadContext context, InputStream inputStream, String filename) {
        try {
            final ANTLRInputStream stream = new ANTLRInputStream(inputStream);
            stream.name = filename;
            ES3Lexer lexer = new ES3Lexer(stream);
            return parseSourceCode(context, lexer);
        } catch (RecognitionException e) {
            throw new SyntaxError(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Statement> parseSourceCode(DynThreadContext context, ES3Lexer lexer) throws RecognitionException {
        CommonTokenStream stream = new CommonTokenStream(lexer);
        ES3Parser parser = new ES3Parser(stream);
        ES3Parser.program_return program = parser.program();
        CommonTree tree = (CommonTree) program.getTree();
        CommonTreeNodeStream treeNodeStream = new CommonTreeNodeStream(tree);
        treeNodeStream.setTokenStream(stream);
        ES3Walker walker = new ES3Walker(treeNodeStream);

        context.setRuntime(this);
        context.setClassLoader(config.getClassLoader());
        Executor executor = new Executor(context);
        walker.setExecutor(executor);
        walker.program();
        return walker.getResult();
    }

    private void execute(DynThreadContext context, List<Statement> result) {
        Script script = compiler.compile(result.toArray(new Statement[]{}));
        Scope globalScope = context.getScope();
        initBuiltins(globalScope);
        script.setGlobalScope(globalScope);
        script.execute(context);
    }

    private void initBuiltins(Scope globalScope) {
        Set<Builtin> builtins = config.getBuiltins();
        for (Builtin builtin : builtins) {
            globalScope.define(builtin.getBindingName(), builtin.getBoundObject());
        }
    }

    public Object compile(DynThreadContext context, CodeBlock codeBlock, final String[] args) {
        return this.compiler.compile(context, new DynFunction(codeBlock) {
            public String[] getArguments() {
                return args;
            }
        });
    }
}
