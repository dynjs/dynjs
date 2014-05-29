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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

import org.dynjs.exception.DynJSException;
import org.dynjs.runtime.DynJS;
import org.jboss.aesh.console.AeshConsoleCallback;
import org.jboss.aesh.console.Console;
import org.jboss.aesh.console.ConsoleOperation;
import org.jboss.aesh.console.Prompt;
import org.jboss.aesh.console.settings.QuitHandler;
import org.jboss.aesh.console.settings.Settings;
import org.jboss.aesh.console.settings.SettingsBuilder;

public class Repl {

    public static final String WELCOME_MESSAGE = "dynjs console."
            + System.lineSeparator()
            + "Type exit and press ENTER to leave."
            + System.lineSeparator();
    public static final String PROMPT = "dynjs> ";
    private final DynJS runtime;
    private final PrintStream out;
    private final InputStream in;
    private final String welcome;
    private final String prompt;

    private PrintWriter log;

    public Repl(DynJS runtime, InputStream in, OutputStream out) {
        this(runtime, in, out, WELCOME_MESSAGE, PROMPT);
    }

    public Repl(DynJS runtime, InputStream in, OutputStream out, String welcome, String prompt) {
        this(runtime, in, out, welcome, prompt, System.getProperty("user.dir") + "/dynjs.log");
    }

    public Repl(DynJS runtime, InputStream in, OutputStream out, String welcome, String prompt, String log) {
        this.prompt = prompt;
        this.welcome = welcome;
        this.runtime = runtime;
        this.out = new PrintStream(out);
        this.in = in;
        try {
            this.log = new PrintWriter(log);
        } catch (IOException e) {
            System.err.println("Cannot create error log " + log);
        }
    }

    public void run() {
        final Settings consoleSettings = new SettingsBuilder()
            .outputStream(this.out)
            .inputStream(this.in)
            .disableHistory(true)
            .parseOperators(false)
            .quitHandler(new QuitHandler() {
                @Override
                public void quit() {
                    if (log != null) {
                        log.close();
                    }
                }
            }).create();

        final Console console = new Console(consoleSettings);
        console.getShell().out().println(welcome);
        console.setPrompt(new Prompt(prompt));
        console.setConsoleCallback(new AeshConsoleCallback() {
            @Override
            public int execute(ConsoleOperation output) {
                String statement = output.getBuffer();
                log.write(statement + "\n");
                if (statement.equalsIgnoreCase("exit")) {
                    console.stop();
                    return 0;
                } else {
                    try {
                        Object object = runtime.evaluate(statement);
                        log.write(object.toString() + "\n");
                        console.getShell().out().println(object.toString());
                    } catch (DynJSException e) {
                        console.getShell().out().println(e.getLocalizedMessage());
                        logException(e);
                    } catch (IncompatibleClassChangeError e) {
                        console.getShell().err().println("ERROR> " + e.getLocalizedMessage());
                        console.getShell().err().println("Error parsing statement: " + statement);
                        logException(e);
                    } catch (Exception e) {
                        e.printStackTrace(new PrintWriter(out));
                        logException(e);
                    }
                }
                return 0;
            }
        });
        console.start();
    }

    private void logException(Throwable e) {
        log.write(e.getLocalizedMessage() + "\n");
        e.printStackTrace(log);
        log.write("\n");
    }
}
