package org.dynjs.runtime.linker;

import org.dynjs.parser.js.Position;
import org.dynjs.runtime.ExecutionContext;
import org.projectodd.rephract.RephractLinker;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodType.methodType;

/**
 * @author Bob McWhirter
 */
public class CallSiteFactory {

    private final RephractLinker linker;

    public CallSiteFactory(RephractLinker linker) {
        this.linker = linker;
    }

    public CallSite createGet(Position pos) {
        try {
            return linker.bootstrap(
                    MethodHandles.lookup(),
                    "dyn:getProperty|getMethod", methodType(Object.class, Object.class, ExecutionContext.class, String.class),
                    pos.getFileName(), pos.getLine(), pos.getColumn());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public CallSite createSet() {
        try {
            return linker.bootstrap("dyn:setProperty", void.class, Object.class, ExecutionContext.class, String.class, Object.class);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public CallSite createCall(Position pos) {
        try {
            return linker.bootstrap(
                    MethodHandles.lookup(),
                    "dyn:call", methodType( Object.class, Object.class, ExecutionContext.class, Object.class, Object[].class ),
                    pos.getFileName(), pos.getLine(), pos.getColumn() );
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public CallSite createConstruct(Position pos) {
        try {
            return linker.bootstrap(
                    MethodHandles.lookup(),
                    "dyn:construct", methodType( Object.class, Object.class, ExecutionContext.class, Object[].class),
                    pos.getFileName(), pos.getLine(), pos.getColumn() );
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }
}
