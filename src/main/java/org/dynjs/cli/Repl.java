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

import org.dynjs.api.Scope;
import org.dynjs.exception.DynJSException;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.DynThreadContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class Repl {

    private final DynJS environment;
    private final DynThreadContext context;
    private final Scope scope;
    private PrintStream stream;

    public Repl(DynJS environment, DynThreadContext context, Scope scope, PrintStream stream) {
        this.environment = environment;
        this.context = context;
        this.scope = scope;
        this.stream = stream;
    }

    public void run() {
        try {
            String NEW_LINE = System.getProperty("line.separator");
            StringBuilder consoleHello = new StringBuilder();
            consoleHello.append(NEW_LINE)
                    .append("dyn.js console.")
                    .append(NEW_LINE)
                    .append("Type exit and press ENTER to leave.")
                    .append(NEW_LINE);
            stream.println(consoleHello.toString());
            while (true) {
                stream.print("> ");
                String statement = input();
                if (statement.equals("exit")) {
                    return;
                } else {
                    try {
                        environment.eval(context, statement);
                    } catch (DynJSException e) {
                        stream.println(e.getClass().getSimpleName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String input() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        return in.readLine();
    }
}
