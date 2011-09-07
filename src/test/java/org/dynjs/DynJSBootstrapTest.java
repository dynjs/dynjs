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
package org.dynjs;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.dynjs.runtime.DynamicClassLoader;
import org.dynjs.parser.ES3Lexer;
import org.dynjs.parser.ES3Parser;
import org.dynjs.parser.ES3Parser.program_return;
import org.dynjs.parser.ES3Walker;
import org.dynjs.parser.Executor;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * @author qmx
 */
public class DynJSBootstrapTest {

    @Test
    public void testBytecodeGeneration() throws Exception {
        ES3Lexer lexer = new ES3Lexer(new ANTLRStringStream("print('hello world');"));
        CommonTokenStream stream = new CommonTokenStream(lexer);
        ES3Parser parser = new ES3Parser(stream);
        program_return program = parser.program();
        CommonTree tree = (CommonTree) program.getTree();
        CommonTreeNodeStream treeNodeStream = new CommonTreeNodeStream(tree);
        ES3Walker walker = new ES3Walker(treeNodeStream);
        walker.setExecutor(new Executor());
        walker.program();

        DynamicClassLoader classloader = new DynamicClassLoader();

        Class<?> helloWorldClass = classloader.define("WTF", walker.getResult());

        Method method = helloWorldClass.getMethod("main", String[].class);

        method.invoke(null, (Object) new String[]{});

    }
}
