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

            // short circuit options
            if (dynJsArguments.isHelp()) {
                showUsage();
                return;
            } else if (dynJsArguments.isProperties()) {
                showProperties();
                return;
            } else if (dynJsArguments.isVersion()) {
                showVersion();
                return;
            }

            if (dynJsArguments.isConsole()) {
                startRepl();
                return;
            }

            if (dynJsArguments.isAST()) {
                if (!dynJsArguments.getEval().isEmpty()) {
                    showAST(dynJsArguments.getEval());
                } else if (!dynJsArguments.getFilename().isEmpty()) {
                    showAST(new File(dynJsArguments.getFilename()));
                } else {
                    stream.println("please specify source to eval or file");
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

            // last resort, show usage
            showUsage();


        } catch (CmdLineException e) {
            stream.println(e.getMessage());
            stream.println();
            showUsage();
        }
    }

    private void showAST(File file) {
        try {
            initializeRuntime();
            stream.println(runtime.newRunner().withSource( file ).parseSourceCode().dump("  "));
        } catch (FileNotFoundException e) {
            stream.println("File " + file.getName() + " not found");
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
        stream.print(sb.toString());
        stream.println(Option.formatOptions(Options.PROPERTIES));
    }

    private void executeFile(File file) throws IOException {
        try {
            initializeRuntime();
            runtime.newRunner().withSource( file ).execute();
        } catch (FileNotFoundException e) {
            stream.println("File " + file.getName() + " not found");
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
        stream.println("usage: dynjs" + parser.printExample(OptionHandlerFilter.ALL, null) + "\n");
        parser.printUsage(stream);
    }
    
    private void initializeRuntime() {
        config = dynJsArguments.getConfig();
        config.setOutputStream(this.stream);
        runtime = new DynJS(config);
    }
}
