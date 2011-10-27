/**
 *  Copyright 2011 Douglas Campos
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
package org.dynjs.cli;

import org.junit.Test;

import java.net.URL;

public class MainTest {
    @Test
    public void callMainWithNoArguments(){
        new Main(new String[]{}).run();
    }

    @Test
    public void callMainWithInvalidFile(){
        new Main(new String[]{"meh.js"}).run();
    }

    @Test
    public void callMainWithValidFile(){
        URL url = this.getClass().getResource("valid.js");
        new Main(new String[]{url.getPath()}).run();
    }

    @Test
    public void callMainWithInvalidArguments(){
        new Main(new String[]{"--invalid"}).run();
    }
}
