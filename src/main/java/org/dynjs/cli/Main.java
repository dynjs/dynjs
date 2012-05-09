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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import org.dynjs.DynJSVersion;
import org.dynjs.api.Scope;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.DynJSConfig;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.DynThreadContext;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

public class Main {

    private Arguments dynJsArguments;
    private CmdLineParser parser;
    private String[] arguments;
    private DynThreadContext context;
    private PrintStream stream;

    public Main(PrintStream stream, String[] args) {
        dynJsArguments = new Arguments();
        parser = new CmdLineParser(dynJsArguments);
        parser.setUsageWidth(80);
        arguments = args;
        context = new DynThreadContext();
        this.stream = stream;
    }

    public static void main(String[] args) {
        new Main(System.out, args).run();
    }

    void run() {
        try {
            parser.parseArgument(arguments);

            if (dynJsArguments.isHelp() || dynJsArguments.isEmpty()) {
                showUsage();
            } else if (dynJsArguments.isConsole()) {
                startRepl();
            } else if (dynJsArguments.isVersion()) {
                showVersion();
            } else if (!dynJsArguments.getFilename().isEmpty()) {
                executeFile(dynJsArguments.getFilename());
            }

        } catch (CmdLineException e) {
            stream.println(e.getMessage());
            stream.println();
            showUsage();
        }
    }

    private void executeFile(String filename) {
        try {
            final DynJSConfig cfg = new DynJSConfig();
            new DynJS(cfg).eval(context, new FileInputStream(filename), filename);
        } catch (FileNotFoundException e) {
            stream.println("File " + filename + " not found");
        }
    }

    private void showVersion() {
        stream.println("dynjs version " + DynJSVersion.FULL);
    }

    private void startRepl() {
        final DynJSConfig cfg = dynJsArguments.getDynJSConfig();
        Scope scope = new DynObject();
        final DynJS environment = new DynJS(cfg);
        Repl repl = new Repl(environment, context, scope, System.in, stream);
        repl.run();
    }

    private void showUsage() {
        StringBuilder usageText = new StringBuilder("Usage: dynjs [--console |--debug | --help | --version |FILE]\n");
        usageText.append("Starts the dynjs console or executes FILENAME depending the parameters\n");

        stream.println(usageText.toString());

        parser.printUsage(stream);
    }
}
