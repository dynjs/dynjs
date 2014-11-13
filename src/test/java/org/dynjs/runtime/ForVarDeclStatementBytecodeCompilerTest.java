package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.dynjs.Config;
import org.junit.Ignore;
import org.junit.Test;

public class ForVarDeclStatementBytecodeCompilerTest extends AbstractDynJSTestSupport {

    @Override
    protected Config createConfig() {
        Config config = super.createConfig();
        config.setCompileMode(Config.CompileMode.FORCE);
        return config;
    }

    @Ignore
    public void testBasicLoop() {
        eval("for (var i = 0; false; ) {",
             "  var y = (i = 1);",
             "}");

        Object i = getContext().resolve("i").getValue(getContext());
        assertThat(i).isEqualTo(0L);

        Object y = getContext().resolve("y").getValue(getContext());
        assertThat(y).isEqualTo(Types.UNDEFINED);
    }
}
