/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dynjs;

import java.lang.reflect.Method;
import org.dynjs.Compiler.DynamicClassLoader;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.BufferedTokenStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenSource;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.dynjs.parser.ES3Lexer;
import org.dynjs.parser.ES3Parser;
import org.dynjs.parser.ES3Parser.program_return;
import org.dynjs.parser.ES3Walker;
import org.dynjs.parser.Executor;
import org.dynjs.parser.Statement;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author qmx
 */
public class DynJsBootstrapTest {

    @Test
    public void testBytecodeGeneration() throws Exception {
        ES3Lexer lexer = new ES3Lexer(new ANTLRStringStream("print('hello world');"));
        CommonTokenStream stream = new CommonTokenStream(lexer);
        ES3Parser parser = new ES3Parser(stream);
        program_return program = parser.program();
        CommonTree tree = (CommonTree)program.getTree();
        CommonTreeNodeStream treeNodeStream = new CommonTreeNodeStream(tree);
        ES3Walker walker = new ES3Walker(treeNodeStream);
        walker.setExecutor(new Executor());
        walker.program();

        DynamicClassLoader classloader = new Compiler.DynamicClassLoader();

        Class<?> helloWorldClass = classloader.define("WTF", walker.getResult());

        Method method = helloWorldClass.getMethod("main", String[].class);

        method.invoke(null, (Object) new String[]{});

    }
}
