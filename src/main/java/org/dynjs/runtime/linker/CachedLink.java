package org.dynjs.runtime.linker;

import org.projectodd.rephract.Link;
import org.projectodd.rephract.PreconditionFailedException;
import org.projectodd.rephract.SmartLink;

import java.lang.invoke.MethodHandle;

/**
 * @author Bob McWhirter
 */
public class CachedLink extends Link {

    private final Link delegate;

    private MethodHandle guard;
    private MethodHandle target;

    public CachedLink(Link delegate) {
        this.delegate = delegate;
    }

    @Override
    public MethodHandle test(Object... args) throws Throwable {
        if (!(boolean) guard().invokeWithArguments(args)) {
            return null;
        }
        return target();
    }

    @Override
    public Object tryInvoke(Object... args) throws Throwable {
        Class<?>[] argTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; ++i) {
            argTypes[i] = args[i].getClass();
        }

        MethodHandle target = test(args);
        if (target == null) {
            throw new PreconditionFailedException();
        }
        return target.invokeWithArguments(args);
    }

    @Override
    public MethodHandle guard() throws Exception {
        if (this.guard == null) {
            this.guard = this.delegate.guard();
        }
        return this.guard;
    }

    @Override
    public MethodHandle target() throws Exception {
        if ( this.target == null ) {
            this.target = this.delegate.target();
        }
        return this.target;
    }

    public String toString() {
        return "[CachedLink: delegate=" + this.delegate.toString() + "]";
    }
}
