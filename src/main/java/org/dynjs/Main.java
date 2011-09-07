package org.dynjs;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        run(encapsulateArguments(args));
    }

    private static void run(List<String> arguments) {
        if (arguments.isEmpty() || arguments.contains("--help")) {
            showUsage();
        } else if (arguments.contains("--repl") || arguments.contains("--console")) {
            startRepl();
        } else if (arguments.contains("--version")) {
            showVersion();
        } else {
            executeFile(arguments.get(0));
        }
    }

    private static void executeFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("File " + filename + " not found");
        } else {
            System.out.println("OK, you got me. Come back later please.");
        }
    }

    private static void showVersion() {
        System.out.println("Dyn.JS version " + DynJSVersion.FULL);
    }

    private static void startRepl() {
        System.out.println("Not so fast Chuck");
    }

    private static void showUsage() {
        StringBuilder usageText = new StringBuilder("Usage: dynjs [--help | --version | --console | --repl | FILENAME]\n");
        usageText.append("Starts the dynjs console or executes FILENAME depending the parameters\n")
                .append("\n")
                .append("The parameters are:\n")
                .append("   --console   opens a REPL console to test small expressions.\n")
                .append("   --help      shows current screen with. Running without parameters also shows this.\n")
                .append("   --repl      opens a REPL console to test small expressions.\n")
                .append("   --version   shows current version.\n")
                .append("   FILENAME    is the file to execute. Duh.\n")
                .append("\n");

        System.out.println(usageText.toString());
    }

    private static List<String> encapsulateArguments(String[] arguments) {
        List<String> argumentList = new ArrayList<>();
        Collections.addAll(argumentList, arguments);
        return argumentList;
    }
}