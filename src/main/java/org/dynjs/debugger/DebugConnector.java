package org.dynjs.debugger;

import org.dynjs.parser.Statement;
import org.dynjs.runtime.ExecutionContext;

/**
 * @author Bob McWhirter
 */
public interface DebugConnector {

    void debug(ExecutionContext context, Statement statement) throws InterruptedException;
    long setBreakPoint(String fileName, long line, long column);
}
