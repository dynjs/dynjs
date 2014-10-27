package org.dynjs.debugger;

import org.dynjs.debugger.events.Event;
import org.dynjs.runtime.DynJS;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Bob McWhirter
 */
public class DebugRunnerTest {

    private ExecutorService executor;
    private DynJS dynjs;
    private DebugRunner debugRunner;

    @Before
    public void setupDebugger() {
        this.executor = Executors.newSingleThreadExecutor();
        this.dynjs  = new DynJS();
        this.debugRunner = this.dynjs.newDebugger();
        this.debugRunner.withExecutor( this.executor );
        this.debugRunner.waitConnect();
    }

    @Test
    public void testAwait() throws InterruptedException {
        String script = "System.err.println('one');\nSystem.err.println('two');\n";
        this.debugRunner.withSource(script).withListener(new DefaultDebugListener() ).execute();
        this.debugRunner.join();
    }
}
