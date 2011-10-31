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

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import java.util.ArrayList;
import java.util.List;

public class Arguments {
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
