package org.dynjs.parser;

import com.toolazydogs.aunit.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.toolazydogs.aunit.Assert.assertTree;
import static com.toolazydogs.aunit.CoreOptions.*;
import static com.toolazydogs.aunit.Work.*;

@RunWith(AntlrTestRunner.class)
public class TestParser {

    @Configuration
    @AppliesTo("test.*")
    public static Option[] configureTest() {
        return options(
                lexer(ES3Lexer.class),
                parser(ES3Parser.class),
                walker(ES3Walker.class, new TreeParserSetup<ES3Walker>() {
                    @Override
                    public void config(ES3Walker es3Walker) {
                        es3Walker.setExecutor(new Executor());
                    }
                })
        );
    }

    @Test
    public void testVarStatement() throws Exception {
        parse("var x;", rule("variableStatement"));
    }

    @Test
    public void testVarAsPrintStatement() throws Exception {
        parse("var print;", rule("variableStatement"));
    }

    @Test
    public void testPrintStatement() throws Exception {
        parse("print (x);", rule("statement"));
    }

    @Test
    public void testTreePrintStatement() throws Exception {
        assertTree("print", "(print x)", parse("print (x);", rule("statement")));
        assertTree(ES3Parser.SK_PRINT, "(print x)", parse("print (x);", rule("statement")));
    }

    @Test
    public void testTreePrintWalkerStatement() throws Exception {
        walk(withRule("statement"), resultOf(parse("print (x);", rule("statement"))));
    }

    @Test
    public void testBlock() throws Exception {
        assertTree(ES3Parser.BLOCK, "(BLOCK (var (= a 1)))", parse("{ var a = 1; }", rule("block")));

        walk(withRule("block"), resultOf(parse("{ var a = 1; }", rule("block"))));
    }

    @Test
    public void testFunctionDeclaration() throws Exception {
        parse("function x(){}", rule("functionDeclaration"));
    }
}
