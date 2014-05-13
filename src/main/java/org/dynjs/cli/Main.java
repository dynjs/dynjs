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
import org.kohsuke.args4j.OptionHandlerFilter;

public class Main {

    private Arguments dynJsArguments;
    private CmdLineParser parser;
    private String[] arguments;
    private PrintStream stream;
    private Config config;
    private DynJS runtime;

    public Main(PrintStream stream, String[] args) {
        this.arguments = args;
        this.stream = stream;
    }

    public static void main(String[] args) throws IOException {
        new Main(System.out, args).run();
    }

    protected void run() throws IOException {
        try {
            getParser().parseArgument(arguments);

            // short circuit options
            if (getArguments().isHelp()) {
                showUsage();
                return;
            } else if (getArguments().isProperties()) {
                showProperties();
                return;
            } else if (getArguments().isVersion()) {
                showVersion();
                return;
            }

            if (getArguments().isConsole()) {
                startRepl();
                return;
            }

            if (getArguments().isAST()) {
                if (!getArguments().getEval().isEmpty()) {
                    showAST(getArguments().getEval());
                } else if (!getArguments().getFilename().isEmpty()) {
                    showAST(new File(getArguments().getFilename()));
                } else {
                    getOutputStream().println("please specify source to eval or file");
                }
                return;
            }

            if (!dynJsArguments.getEval().isEmpty()) {
                executeSource(dynJsArguments.getEval());
                return;
            } else if (dynJsArguments.getFilename() != null) {
                executeFile(new File(dynJsArguments.getFilename()));
                return;
            } else {
                stream.println("please specify source to eval or file");
            }

            if (!getArguments().getEval().isEmpty()) {
                executeSource(getArguments().getEval());
                return;
            } else if (getArguments().getFilename() != null) {
                executeFile(new File(getArguments().getFilename()));
                return;
            } else {
                getOutputStream().println("please specify source to eval or file");
            }

            // last resort, show usage
            showUsage();


        } catch (CmdLineException e) {
            getOutputStream().println(e.getMessage());
            getOutputStream().println();
            showUsage();
        }
    }

    private void showAST(File file) {
        try {
            initializeRuntime();
            getOutputStream().println(runtime.newRunner().withSource(file).parseSourceCode().dump("  "));
        } catch (FileNotFoundException e) {
            getOutputStream().println("File " + file.getName() + " not found");
        }
    }

    private void executeSource(String eval) {
        initializeRuntime();
        runtime.newRunner().withSource( eval).execute();
    }

    private void showAST(String source) throws IOException {
        initializeRuntime();
        stream.println(runtime.newRunner().withSource(source).parseSourceCode().dump("  "));
    }

    private void showProperties() {
        StringBuilder sb = new StringBuilder();
        sb
                .append("# These properties can be used to alter runtime behavior for perf or compatibility.\n")
                .append("# Specify them by passing directly to Java -Ddynjs.<property>=<value>\n");
        getOutputStream().print(sb.toString());
        getOutputStream().println(Option.formatOptions(Options.PROPERTIES));
    }

    private void executeFile(File file) throws IOException {
        try {
            initializeRuntime();
            runtime.newRunner().withSource( file ).execute();
        } catch (FileNotFoundException e) {
            getOutputStream().println("File " + file.getName() + " not found");
        }
    }

    private void showUsage() {
        getOutputStream().println("usage: " + getBinaryName() + getParser().printExample(OptionHandlerFilter.ALL, null) + "\n");
        getParser().printUsage(getOutputStream());
    }

    protected void startRepl() {
        initializeRuntime();
        Repl repl = new Repl(runtime, System.in, getOutputStream(), getWelcomeMessage(), getPrompt());
        repl.run();
    }

    protected void showVersion() {
        getOutputStream().println(DynJS.VERSION_STRING);
    }

    protected DynJS initializeRuntime() {
        config = getArguments().getConfig();
        config.setOutputStream(getOutputStream());
        runtime = new DynJS(config);
        return runtime;
    }

    protected Arguments getArguments() {
        if (this.dynJsArguments == null) {
            this.dynJsArguments = new Arguments();
        }
        return dynJsArguments;
    }

    protected CmdLineParser getParser() {
        if (this.parser == null) {
            this.parser = new CmdLineParser(getArguments());
            this.parser.setUsageWidth(80);
        }
        return parser;
    }

    protected String getBinaryName() {
        return "dynjs";
    }

    protected PrintStream getOutputStream() {
        return stream;
    }

    protected String getPrompt() {
        return Repl.PROMPT;
    }

    protected String getWelcomeMessage() {
        return Repl.WELCOME_MESSAGE;
    }
}
