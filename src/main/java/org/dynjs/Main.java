package org.dynjs;

import org.dynjs.api.Scope;
import org.dynjs.cli.Arguments;
import org.dynjs.cli.Repl;
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
