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

import jline.console.ConsoleReader;
import org.dynjs.api.Scope;
import org.dynjs.exception.DynJSException;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.DynThreadContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public class Repl {

    public static final String WELCOME_MESSAGE = "dynjs console."
            + System.lineSeparator()
            + "Type exit and press ENTER to leave."
            + System.lineSeparator();
    public static final String PROMPT = "dynjs> ";
    private final DynJS dynJS;
    private final DynThreadContext context;
    private final Scope scope;
    private final InputStream in;
    private final OutputStream out;

    public Repl(DynJS dynJS, DynThreadContext context, Scope scope, InputStream in, OutputStream out) {
        this.dynJS = dynJS;
        this.context = context;
        this.scope = scope;
        this.in = in;
        this.out = out;
    }

    public void run() {
        ConsoleReader console = null;
        try {
            console = new ConsoleReader(in, out);
            console.println(WELCOME_MESSAGE);
            String statement = null;
            while ((statement = console.readLine(PROMPT)) != null) {
                if ("exit".equals(statement.trim())) {
                    return;
                } else {
                    try {
                        dynJS.eval(context, statement);
                    } catch (DynJSException e) {
                        console.println(e.getClass().getSimpleName());
                    } catch (Exception e) {
                        e.printStackTrace(new PrintWriter(out));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
