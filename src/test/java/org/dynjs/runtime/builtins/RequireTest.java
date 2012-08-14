///*
// * Copyright 2012 JBoss Inc
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *       http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package org.dynjs.runtime.builtins;
//
//import static org.fest.assertions.Assertions.assertThat;
//
//import org.dynjs.exception.ReferenceError;
//import org.dynjs.runtime.DynJS;
//import org.dynjs.runtime.DynJSConfig;
//import org.dynjs.runtime.DynThreadContext;
//import org.junit.Before;
//import org.junit.Test;
//
//public class RequireTest {
//    private DynJS dynJS;
//    private DynThreadContext context;
//    private DynJSConfig config;
//
//    @Before
//    public void setUp() {
//        config = new DynJSConfig();
//        new Require();
//        context = new DynThreadContext();
//        context.addLoadPath( System.getProperty( "user.dir" ) + "/src/test/resources/org/dynjs/runtime/builtins/" );
//        context.addModuleProvider( new TestModuleProvider() );
//        dynJS = new DynJS( config );
//    }
//
//    @Test
//    public void testReturnsNullWithoutAnArgument() {
//        check( "var result = require();", null );
//    }
//
//    @Test
//    public void testReturnsNullWhenTheFileIsNotFound() {
//        check( "var result = require('nonexistant_module');", null );
//    }
//
//    @Test
//    public void testReturnsExportsWhenTheFileIsFound() {
//        check( "var result = require('my_module').message;", "Hello world" );
//    }
//
//    @Test
//    public void testAllowsFileExtension() {
//        check( "var result = require('my_module.js').message;", "Hello world" );
//    }
//
//    @Test
//    public void testFindsPackagedModules() {
//        check( "var result = require('amodule').message", "Hello world" );
//    }
//
//    @Test
//    public void testExportsFunctions() {
//        check( "var result = require('my_module').sayHello();", "Hello again" );
//    }
//
//    @Test
//    public void testAllowsSettingExportsToAnArbitraryThing() {
//        // check("var result = require('my_export_thingy').doesSomething();",
//        // "A thingy!");
//    }
//
//    @Test
//    public void testHasPrivateFunctions() {
//        check( "var result = require('my_module').farewell();", "Goodbye, cruel world." );
//        try {
//            check( "var result = require('my_module').sayGoodbye", null );
//        } catch (ReferenceError error) {
//            assertThat( error.getMessage() ).isEqualTo( "sayGoodbye not found" );
//        }
//    }
//
//    @Test
//    public void testKeepsPrivateVariablesPrivate() {
//        try {
//            check( "var result = require('my_module').privateVariable", null );
//        } catch (ReferenceError error) {
//            assertThat( error.getMessage() ).isEqualTo( "privateVariable not found" );
//        }
//    }
//
//    @Test
//    public void testSupportsNestedRequires() {
//        check( "var x = require('outer'); var result = x.quadruple(4);", 16.0 );
//    }
//
//    @Test
//    public void testJavaImplementedRequires() {
//        check( "var x = require('java_impl'); var result = x.cheese();", "cheddar" );
//    }
//
//    private void check(String scriptlet, Object expected) {
//        dynJS.eval( context, scriptlet );
//        Object result = context.getScope().resolve( "result" );
//        assertThat( result ).isEqualTo( expected );
//    }
//}
