/**
 *  Copyright 2013 Douglas Campos, and individual contributors
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

public class MainTest {

    private PrintStream stream;

    @Before
    public void setup() {
        stream = new PrintStream(new ByteArrayOutputStream());
    }

    @Test
    public void callMainWithNoArguments() throws IOException {
        new Main(stream, new String[] {}).run();
    }

    @Test
    public void callMainWithInvalidFile() throws IOException {
        new Main(stream, new String[] { "meh.js" }).run();
    }

    @Test
    public void callMainWithValidFile() throws IOException {
        URL url = this.getClass().getResource("valid.js");
        new Main(stream, new String[] { url.getPath() }).run();
    }

    @Test
    public void callMainWithInvalidArguments() throws IOException {
        new Main(stream, new String[] { "--invalid" }).run();
    }
}
