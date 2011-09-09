package org.dynjs;

import org.junit.Before;
import org.junit.Test;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ExampleMode;

import static org.dynjs.DynJSArguments.CONSOLE;
import static org.dynjs.DynJSArguments.HELP;
import static org.dynjs.DynJSArguments.VERSION;

import static org.fest.assertions.Assertions.assertThat;

public class DynJSArgumentsTest {
    private CmdLineParser parser;
    private DynJSArguments dynJsArguments;

    @Before
    public void setUp() throws Exception {
        dynJsArguments = new DynJSArguments();
        parser = new CmdLineParser(dynJsArguments);
        parser.setUsageWidth(80);
    }

    @Test
    public void usageCannotBeEmpty(){
        assertThat(parser.printExample(ExampleMode.ALL)).isNotEmpty();
    }

    @Test
    public void callWithNoArgument() throws CmdLineException {
        parser.parseArgument(new String[]{});

        assertThat(dynJsArguments.isEmpty()).isTrue();
    }

    @Test
    public void callWithConsoleArgument() throws CmdLineException {
        parser.parseArgument(new String[]{CONSOLE});

        assertThat(dynJsArguments.isEmpty()).isFalse();
        assertThat(dynJsArguments.isConsole()).isTrue();
    }

    @Test
    public void callWithHelpArgument() throws CmdLineException {
        parser.parseArgument(new String[]{HELP});

        assertThat(dynJsArguments.isEmpty()).isFalse();
        assertThat(dynJsArguments.isHelp()).isTrue();
    }

    @Test
    public void callWithVersionArgument() throws CmdLineException {
        parser.parseArgument(new String[]{VERSION});

        assertThat(dynJsArguments.isEmpty()).isFalse();
        assertThat(dynJsArguments.isVersion()).isTrue();
    }

    @Test
    public void callWithFilenameArgument() throws CmdLineException {
        final String filename = "meh.js";
        parser.parseArgument(new String[]{filename});

        assertThat(dynJsArguments.isEmpty()).isFalse();
        assertThat(dynJsArguments.getFilename()).isNotEmpty();
        assertThat(dynJsArguments.getFilename()).isEqualTo(filename);
    }

    @Test(expected = CmdLineException.class)
    public void callWithUnexpectedArgument() throws CmdLineException {
        parser.parseArgument(new String[]{"--whatever"});
    }

    @Test
    public void callWithUnexpectedArgumentOrder() throws CmdLineException {
        parser.parseArgument(new String[]{"meh.js", HELP});
    }


}