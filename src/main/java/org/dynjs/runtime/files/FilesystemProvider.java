package org.dynjs.runtime.files;

import java.io.IOException;

/**
 * Provider for accessing files
 *
 * @author Stephen Connolly
 */
public interface FilesystemProvider {
    ProxyFile createFile(String filePath);

    ProxyFile createFile(String parent, String child);

    ProxyFile createFile(ProxyFile parentFile, String child);

    ProxyFile createTempFile(String prefix, String suffix) throws IOException;

    ProxyFile[] listRoots();

}
