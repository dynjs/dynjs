package org.dynjs.runtime.files;

/**
 * @author Stephen Connolly
 */
public interface ProxyFilenameFilter {
    boolean accept(ProxyFile dir, String name);
}
