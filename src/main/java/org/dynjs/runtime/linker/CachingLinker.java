package org.dynjs.runtime.linker;

import org.projectodd.rephract.ContextualLinker;
import org.projectodd.rephract.Invocation;
import org.projectodd.rephract.Link;
import org.projectodd.rephract.Linker;

import java.lang.invoke.MethodHandle;

/**
 * @author Bob McWhirter
 */
public class CachingLinker extends ContextualLinker {

    private static final Link NO_LINK = new Link() {
        @Override
        public MethodHandle guard() throws Exception {
            return null;
        }

        @Override
        public MethodHandle target() throws Exception {
            return null;
        }
    };

    private final Linker delegate;

    private Link getProperty;
    private Link setProperty;
    private Link getMethod;
    private Link call;
    private Link construct;

    public CachingLinker(Linker delegate) {
        this.delegate = delegate;
    }

    @Override
    public Link preLinkGetProperty(Invocation invocation) throws Exception {
        if (this.getProperty == null) {
            this.getProperty = populate(this.delegate.preLinkGetProperty(invocation));
        }

        return retrieve( this.getProperty );
    }

    @Override
    public Link preLinkSetProperty(Invocation invocation) throws Exception {
        if ( this.setProperty == null ) {
            this.setProperty = populate( this.delegate.preLinkSetProperty(invocation));
        }
        return retrieve( this.setProperty );
    }

    @Override
    public Link preLinkGetMethod(Invocation invocation) throws Exception {
        if ( this.getMethod == null ) {
            this.getMethod = populate( this.delegate.preLinkGetMethod(invocation) );
        }
        return retrieve( this.getMethod );
    }

    @Override
    public Link preLinkCall(Invocation invocation) throws Exception {
        if ( this.call == null ) {
            this.call = populate(this.delegate.preLinkCall(invocation));
        }
        return retrieve( this.call );
    }

    @Override
    public Link preLinkConstruct(Invocation invocation) throws Exception {
        if ( this.construct == null ) {
            this.construct = populate( this.delegate.preLinkConstruct(invocation) );
        }

        return retrieve(this.construct);
    }

    protected Link populate(Link inbound) {
        if ( inbound == null ) {
            return NO_LINK;
        }
        return new CachedLink(inbound);
    }

    protected Link retrieve(Link inbound) {
        if ( inbound == NO_LINK ) {
            return null;
        }
        return inbound;
    }
}
