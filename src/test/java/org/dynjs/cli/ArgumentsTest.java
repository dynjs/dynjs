package org.dynjs.cli;

import org.junit.Before;
import org.junit.Test;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ExampleMode;

import static org.dynjs.cli.Arguments.CONSOLE;
import static org.dynjs.cli.Arguments.HELP;
import static org.dynjs.cli.Arguments.VERSION;

import static org.fest.assertions.Assertions.assertThat;

public class ArgumentsTest {
    private CmdLineParser parser;
    private Arguments arguments;

    @Before
    public void setUp() throws Exception {
        arguments = new Arguments();
        parser = new CmdLineParser(arguments);
        parser.setUsageWidth(80);
    }

    @Test
    public void usageCannotBeEmpty(){
        assertThat(parser.printExample(ExampleMode.ALL)).isNotEmpty();
    }

    @Test
    public void callWithNoArgument() throws CmdLineException {
        parser.parseArgument(new String[]{});

        assertThat(arguments.isEmpty()).isTrue();
    }

    @Test
    public void callWithConsoleArgument() throws CmdLineException {
        parser.parseArgument(new String[]{CONSOLE});

        assertThat(arguments.isEmpty()).isFalse();
        assertThat(arguments.isConsole()).isTrue();
    }

    @Test
    public void callWithHelpArgument() throws CmdLineException {
        parser.parseArgument(new String[]{HELP});

        assertThat(arguments.isEmpty()).isFalse();
        assertThat(arguments.isHelp()).isTrue();
    }

    @Test
    public void callWithVersionArgument() throws CmdLineException {
        parser.parseArgument(new String[]{VERSION});

        assertThat(arguments.isEmpty()).isFalse();
        assertThat(arguments.isVersion()).isTrue();
    }

    @Test
    public void callWithFilenameArgument() throws CmdLineException {
        final String filename = "meh.js";
        parser.parseArgument(new String[]{filename});

        assertThat(arguments.isEmpty()).isFalse();
        assertThat(arguments.getFilename()).isNotEmpty();
        assertThat(arguments.getFilename()).isEqualTo(filename);
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
