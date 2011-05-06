/*
 * $Id: 20811 2011-05-05 16:04:20Z porcelli $
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.dynjs.parser;

import com.toolazydogs.aunit.AntlrTestRunner;
import com.toolazydogs.aunit.AppliesTo;
import com.toolazydogs.aunit.Configuration;
import com.toolazydogs.aunit.Option;
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
                walker(ES3Walker.class)
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
