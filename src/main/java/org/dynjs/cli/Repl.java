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

import org.dynjs.exception.DynJSException;
import org.dynjs.runtime.DynJS;
import org.jboss.jreadline.console.Console;
import org.jboss.jreadline.console.ConsoleOutput;
import org.jboss.jreadline.console.settings.Settings;

public class Repl {

    public static final String WELCOME_MESSAGE = "dynjs console."
            + System.lineSeparator()
            + "Type exit and press ENTER to leave."
            + System.lineSeparator();
    public static final String PROMPT = "dynjs> ";
    private final DynJS runtime;
    private final OutputStream out;
    private final InputStream in;

    public Repl(DynJS runtime, InputStream in, OutputStream out) {
        this.runtime = runtime;
        this.out = out;
        this.in = in;
    }

    public void run() {
        try {
            Settings consoleSettings = Settings.getInstance();
            consoleSettings.setStdOut(this.out);
            consoleSettings.setInputStream(this.in);
            Console console = new Console();
            console.pushToStdOut(WELCOME_MESSAGE);
            ConsoleOutput line = null;
            while ((line = console.read(PROMPT)) != null) {
                String statement = line.getBuffer();
                if (statement.equalsIgnoreCase("exit")) {
                    break;
                } else {
                    try {
                        Object object = runtime.evaluate(statement);
                        console.pushToStdOut(object.toString() + "\n");
                    } catch (DynJSException e) {
                        console.pushToStdErr(e.getLocalizedMessage() + "\n");
                    } catch (IncompatibleClassChangeError e) {
                        console.pushToStdErr("ERROR> " + e.getLocalizedMessage() + "\n");
                        console.pushToStdErr("Error parsing statement: " + statement + "\n");
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
