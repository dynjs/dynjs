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

import org.dynjs.DynJSVersion;
import org.dynjs.api.Scope;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.DynThreadContext;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.File;

public class Main {
    private Arguments dynJsArguments;
    private CmdLineParser parser;
    private String[] arguments;

    public Main(String[] args) {
        dynJsArguments = new Arguments();
        parser = new CmdLineParser(dynJsArguments);
        parser.setUsageWidth(80);
        arguments = args;
    }

    public static void main(String[] args) {
        new Main(args).run();
    }

    void run() {
        try{
            parser.parseArgument(arguments);

            if (dynJsArguments.isHelp() || dynJsArguments.isEmpty()) {
                showUsage();
            } else if (dynJsArguments.isConsole()) {
                startRepl();
            } else if (dynJsArguments.isVersion()) {
                showVersion();
            } else if (!dynJsArguments.getFilename().isEmpty()){
                executeFile(dynJsArguments.getFilename());
            }

        } catch (CmdLineException e) {
            System.out.println(e.getMessage());
            System.out.println();
            showUsage();
        }
    }

    private void executeFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("File " + filename + " not found");
        } else {
            System.out.println("OK, you got me. Come back later please.");
        }
    }

    private void showVersion() {
        System.out.println("Dyn.JS version " + DynJSVersion.FULL);
    }

    private void startRepl() {
        DynThreadContext threadContext = new DynThreadContext();
        Scope scope = new DynObject();
        DynJS environment = new DynJS();

        Repl repl = new Repl(environment, threadContext, scope);
        repl.run();
    }

    private void showUsage() {
        StringBuilder usageText = new StringBuilder("Usage: dynjs [--console | --help | --version | FILE]\n");
        usageText.append("Starts the dynjs console or executes FILENAME depending the parameters\n");

        System.out.println(usageText.toString());

        parser.printUsage(System.out);
    }
}
