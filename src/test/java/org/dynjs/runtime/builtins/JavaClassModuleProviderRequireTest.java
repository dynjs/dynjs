/*
 * Copyright 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dynjs.runtime.builtins;

import static org.fest.assertions.Assertions.*;

import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.dynjs.runtime.JSFunction;
import org.junit.Test;

public class JavaClassModuleProviderRequireTest extends AbstractDynJSTestSupport {

    @Test
    public void testJavaImplementedRequires() {
        assertThat( eval("require('console').log" ) ).isInstanceOf(JSFunction.class);
        eval( "require('console').log( 'howdy' )" );
    }
    
    

}
