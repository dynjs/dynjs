package org.dynjs.runtime.files;

/**
 * @author Stephen Connolly
 */
public interface ProxyFileFilter {
    boolean accept(ProxyFile pathname);
}
