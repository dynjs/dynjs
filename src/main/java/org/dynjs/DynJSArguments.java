package org.dynjs;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import java.util.ArrayList;
import java.util.List;

class DynJSArguments {
    static final String CONSOLE = "--console";
    static final String HELP    = "--help";
    static final String VERSION = "--version";

    @Option(name = CONSOLE, usage = "Opens a REPL console to test small expressions.")
    private boolean console;

    @Option(name = HELP, usage = "Shows current screen. Running without parameters also shows this.")
    private boolean help;

    @Option(name = VERSION, usage = "Shows current dyn.js version.")
    private boolean version;

    @Argument(usage = "File to be executed by dyn.js", required = false, metaVar = "FILE")
    private List<String> arguments = new ArrayList<>();

    public boolean isEmpty() {
        return !(console || help || version) && arguments.isEmpty();
    }

    public boolean isConsole() {
        return console;
    }

    public boolean isHelp() {
        return help;
    }

    public boolean isVersion() {
        return version;
    }

    public String getFilename() {
        return arguments.get(0);
    }
}