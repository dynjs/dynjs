package org.dynjs.runtime.files;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author Stephen Connolly
 */
public interface ProxyFile extends Serializable, Comparable<ProxyFile> {
    ProxyFileInputStream createInputStream() throws FileNotFoundException;

    ProxyFileOutputStream createOutputStream() throws FileNotFoundException;

    ProxyFileOutputStream createOutputStream(boolean append) throws FileNotFoundException;

    ProxyFile createTempFile(String prefix, String suffix) throws IOException;

    boolean renameTo(ProxyFile dest);

    int compareTo(ProxyFile pathname);

    long length();

    long getTotalSpace();

    long getFreeSpace();

    long getUsableSpace();

    String getName();

    String getParent();

    ProxyFile getParentFile();

    String getPath();

    boolean isAbsolute();

    String getAbsolutePath();

    ProxyFile getAbsoluteFile();

    String getCanonicalPath() throws IOException;

    ProxyFile getCanonicalFile() throws IOException;

    String[] list();

    String[] list(ProxyFilenameFilter filter);

    ProxyFile[] listFiles();

    ProxyFile[] listFiles(ProxyFileFilter filter);

    ProxyFile[] listFiles(ProxyFilenameFilter filter);

    boolean exists();

    boolean isFile();

    boolean createNewFile() throws IOException;

    boolean mkdir();

    boolean isDirectory();

    boolean mkdirs();

    boolean delete();

    void deleteOnExit();

    boolean isHidden();

    long lastModified();

    boolean setLastModified(long time);

    boolean canRead();

    boolean setReadOnly();

    boolean setReadable(boolean readable);

    boolean setReadable(boolean readable, boolean ownerOnly);

    boolean canWrite();

    boolean setWritable(boolean writable);

    boolean setWritable(boolean writable, boolean ownerOnly);

    boolean canExecute();

    boolean setExecutable(boolean executable);

    boolean setExecutable(boolean executable, boolean ownerOnly);
}
