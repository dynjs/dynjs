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
import java.io.PrintStream;

public class Repl {

    public static final String WELCOME_MESSAGE = "dynjs console."
            + System.lineSeparator()
            + "Type exit and press ENTER to leave."
            + System.lineSeparator();
    public static final String PROMPT = "dynjs> ";
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
            stream.println(WELCOME_MESSAGE);
            ConsoleReader reader = new ConsoleReader();
            String statement = null;
            while ((statement = reader.readLine(PROMPT)) != null) {
                if ("exit".equals(statement.trim())) {
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
        } finally {
            stream.close();
        }
    }
}
