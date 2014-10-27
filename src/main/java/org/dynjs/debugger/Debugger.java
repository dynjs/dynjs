package org.dynjs.debugger;

import org.dynjs.parser.Statement;
import org.dynjs.parser.ast.Expression;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.Runner;

/**
 * @author Bob McWhirter
 */
public class Debugger extends Runner {

    private boolean await;

    public Debugger(DynJS runtime) {
        super(runtime);
    }

    public Debugger await() {
        return await(true);
    }

    public Debugger await(boolean await) {
        this.await = await;
        return this;
    }

    public void debug(Statement statement) {
    }

    public void debug(Expression expr) {
    }


}
