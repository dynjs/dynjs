/**
 *  Copyright 2012 Douglas Campos, and individual contributors
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import jline.console.ConsoleReader;

import org.dynjs.exception.DynJSException;
import org.dynjs.runtime.DynJS;

public class Repl {

    public static final String WELCOME_MESSAGE = "dynjs console."
            + System.lineSeparator()
            + "Type exit and press ENTER to leave."
            + System.lineSeparator();
    public static final String PROMPT = "dynjs> ";
    private final DynJS runtime;
    private final InputStream in;
    private final OutputStream out;

    public Repl(DynJS runtime, InputStream in, OutputStream out) {
        this.runtime = runtime;
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
                        Object object = runtime.evaluate(statement);
                        System.out.println(object.toString());
                    } catch (DynJSException e) {
                        console.println(e.getClass().getSimpleName());
                        console.println(e.getLocalizedMessage());
                        console.println("Error parsing statement: " + statement.toString());
                    } catch (IncompatibleClassChangeError e) {
                        console.println("Error parsing statement: " + statement.toString());
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
