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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import com.headius.options.Option;
import org.dynjs.Config;
import org.dynjs.runtime.DynJS;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

public class Main {

    private Arguments dynJsArguments;
    private CmdLineParser parser;
    private String[] arguments;
    private PrintStream stream;
    private Config config;
    private DynJS runtime;

    public Main(PrintStream stream, String[] args) {
        this.dynJsArguments = new Arguments();
        this.parser = new CmdLineParser(dynJsArguments);
        this.parser.setUsageWidth(80);
        this.arguments = args;
        this.stream = stream;
    }

    public static void main(String[] args) throws IOException {
        new Main(System.out, args).run();
    }

    void run() throws IOException {
        try {
            parser.parseArgument(arguments);

            if (dynJsArguments.isHelp() || dynJsArguments.isEmpty()) {
                showUsage();
            } else if (dynJsArguments.isProperties()) {
                showProperties();
            } else if (dynJsArguments.getFilename() != null) {
                executeFile(dynJsArguments.getFilename());
            } else if (dynJsArguments.isConsole()) {
                startRepl();
            } else if (dynJsArguments.isVersion()) {
                showVersion();
            }

        } catch (CmdLineException e) {
            stream.println(e.getMessage());
            stream.println();
            showUsage();
        }
    }

    private void showProperties() {
        StringBuilder sb = new StringBuilder();
        sb
                .append("# These properties can be used to alter runtime behavior for perf or compatibility.\n")
                .append("# Specify them by passing directly to Java -Ddynjs.<property>=<value>\n");
        stream.print(sb.toString());
        stream.println(Option.formatOptions(Options.PROPERTIES));
    }

    private void executeFile(String filename) throws IOException {
        try {
            initializeRuntime();
            runtime.newRunner().withSource( new File( filename ) ).execute();
        } catch (FileNotFoundException e) {
            stream.println("File " + filename + " not found");
        }
    }

    private void showVersion() {
        stream.println(DynJS.VERSION_STRING);
    }

    private void startRepl() {
        initializeRuntime();
        Repl repl = new Repl(runtime, System.in, stream);
        repl.run();
    }

    private void showUsage() {
        StringBuilder usageText = new StringBuilder("Usage: dynjs [--console |--debug | --help | --version |FILE]\n");
        usageText.append("Starts the dynjs console or executes FILENAME depending the parameters\n");
        stream.println(usageText.toString());
        parser.printUsage(stream);
    }
    
    private void initializeRuntime() {
        config = dynJsArguments.getConfig();
        config.setOutputStream(this.stream);
        runtime = new DynJS(config);
    }
}
