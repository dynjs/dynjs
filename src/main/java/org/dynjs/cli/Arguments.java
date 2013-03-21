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

import java.util.ArrayList;
import java.util.List;

import org.dynjs.Config;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

public class Arguments {

    static final String CONSOLE = "--console";
    static final String HELP = "--help";
    static final String VERSION = "--version";
    static final String DEBUG = "--debug";

    @Option(name = CONSOLE, usage = "Opens a REPL console to test small expressions.")
    private boolean console;

    @Option(name = HELP, usage = "Shows current screen. Running without parameters also shows this.")
    private boolean help;

    @Option(name = VERSION, usage = "Shows current dynjs version.")
    private boolean version;

    @Option(name = DEBUG, usage = "Run REPL in debug mode.")
    private boolean debug;

    @Argument(usage = "File to be executed by dynjs", required = false, metaVar = "FILE")
    private List<String> arguments = new ArrayList<>();

    public Config getConfig() {
        Config config = new Config();
        if (this.isDebug()) {
            config.setDebug(true);
        }
        return config;
    }

    public boolean isEmpty() {
        return !(console || help || version || debug) && arguments.isEmpty();
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

    public boolean isDebug() {
        return debug;
    }

    public String getFilename() {
        return arguments.get( 0 );
    }
}
