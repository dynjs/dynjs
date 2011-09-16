/**
 *  Copyright 2011 dynjs contributors
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

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.dynjs.api.Scope;
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.exception.SyntaxError;
import org.dynjs.parser.ES3Lexer;
import org.dynjs.parser.ES3Parser;
import org.dynjs.parser.ES3Walker;
import org.dynjs.parser.Executor;
import org.dynjs.parser.Statement;

import java.util.List;

public class DynJS {

    private final DynJSConfig config;
    private final DynJSCompiler compiler = new DynJSCompiler();

    public DynJS(DynJSConfig config) {
        this.config = config;
    }

    public DynJS() {
        this.config = new DynJSConfig();
    }

    public void eval(DynThreadContext context, Scope scope, String expression) {
        List<Statement> result;

        try {
            result = parseSourceCode(scope, expression);
            Script script = compiler.compile(result.toArray(new Statement[]{}));
            script.execute(context, scope);
        } catch (RecognitionException e) {
            throw new SyntaxError(e);
        }
    }

    private List<Statement> parseSourceCode(Scope scope, String code) throws RecognitionException {
        ES3Lexer lexer = new ES3Lexer(new ANTLRStringStream(code));
        CommonTokenStream stream = new CommonTokenStream(lexer);
        ES3Parser parser = new ES3Parser(stream);
        ES3Parser.program_return program = parser.program();
        CommonTree tree = (CommonTree) program.getTree();
        CommonTreeNodeStream treeNodeStream = new CommonTreeNodeStream(tree);
        treeNodeStream.setTokenStream(stream);
        ES3Walker walker = new ES3Walker(treeNodeStream);
        walker.setExecutor(new Executor());
        walker.program();
        return walker.getResult();
    }
}
