package org.dynjs;

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
        }
    }

    private static void showUsage() {
        StringBuilder usageText = new StringBuilder("Usage: dynjs [--help | --version | --console | --repl | FILENAME]\n");
        usageText.append("Starts the dynjs console or executes FILE depending the parameters\n")
                .append("\n")
                .append("The parameters are:\n")
                .append("   --console   opens a REPL console to test small expressions.\n")
                .append("   --help      shows current screen with. Running without parameters also shows this.\n")
                .append("   --repl      opens a REPL console to test small expressions.\n")
                .append("   --version   shows current version.\n")
                .append("\n")
                .append("FILENAME is the file to execute. Duh.");

        System.out.println(usageText.toString());
    }

    private static List<String> encapsulateArguments(String[] arguments) {
        List<String> argumentList = new ArrayList<>();
        Collections.addAll(argumentList, arguments);
        return argumentList;
    }
}