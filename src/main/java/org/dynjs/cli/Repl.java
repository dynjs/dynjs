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

import org.dynjs.exception.DynJSException;
import org.dynjs.runtime.DynJS;
import org.jboss.aesh.console.Console;
import org.jboss.aesh.console.ConsoleCallback;
import org.jboss.aesh.console.ConsoleOutput;
import org.jboss.aesh.console.Prompt;
import org.jboss.aesh.console.reader.ConsoleInputSession;
import org.jboss.aesh.console.settings.QuitHandler;
import org.jboss.aesh.console.settings.Settings;

import java.io.*;

public class Repl {

    public static final String WELCOME_MESSAGE = "dynjs console."
            + System.lineSeparator()
            + "Type exit and press ENTER to leave."
            + System.lineSeparator();
    public static final String PROMPT = "dynjs> ";
    private final DynJS runtime;
    private final OutputStream out;
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
        this.out = out;
        this.in = new ConsoleInputSession(in).getExternalInputStream();
        try {
            this.log = new PrintWriter(log);
        } catch (IOException e) {
            System.err.println("Cannot create error log " + log);
        }
    }

    public void run() {
        try {
            Settings consoleSettings = Settings.getInstance();
            consoleSettings.setStdOut(this.out);
            consoleSettings.setInputStream(this.in);
            consoleSettings.setHistoryPersistent(false);
            consoleSettings.enableOperatorParser(false);
            consoleSettings.setQuitHandler(new QuitHandler() {
                @Override
                public void quit() {
                    if (log != null) {
                        log.close();
                    }
                }
            });
            final Console console = Console.getInstance();
            console.pushToStdOut(welcome);
            console.setPrompt(new Prompt(prompt));
            console.setConsoleCallback(new ConsoleCallback() {
                @Override
                public int readConsoleOutput(ConsoleOutput consoleOutput) throws IOException {
                    String statement = consoleOutput.getBuffer();
                    log.write(statement + "\n");
                    if (statement.equalsIgnoreCase("exit")) {
                        console.stop();
                        return 0;
                    } else {
                        try {
                            Object object = runtime.evaluate(statement);
                            log.write(object.toString() + "\n");
                            console.pushToStdOut(object.toString() + "\n");
                        } catch (DynJSException e) {
                            console.pushToStdErr(e.getLocalizedMessage() + "\n");
                            logException(e);
                        } catch (IncompatibleClassChangeError e) {
                            console.pushToStdErr("ERROR> " + e.getLocalizedMessage() + "\n");
                            console.pushToStdErr("Error parsing statement: " + statement + "\n");
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
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void logException(Throwable e) {
        log.write(e.getLocalizedMessage() + "\n");
        e.printStackTrace(log);
        log.write("\n");
    }
}
