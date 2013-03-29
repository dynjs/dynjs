package org.dynjs.runtime.files;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * @author Stephen Connolly
 */
public class NativeFilesystemProvider implements FilesystemProvider {
    @Override
    public ProxyFile createFile(String filePath) {
        return wrap(new File(filePath));
    }

    @Override
    public ProxyFile createFile(String parent, String child) {
        return wrap(new File(parent, child));
    }

    @Override
    public ProxyFile createFile(ProxyFile parentFile, String child) {
        if (parentFile instanceof NativeFile) {
            return wrap(new File(((NativeFile) parentFile).delegate, child));
        }
        return wrap(new File(parentFile.getPath(), child));
    }

    @Override
    public ProxyFile createTempFile(String prefix, String suffix) throws IOException {
        return wrap(File.createTempFile(prefix, suffix));
    }

    @Override
    public ProxyFile[] listRoots() {
        return wrap(File.listRoots());
    }

    private static ProxyFile[] wrap(File[] files) {
        if (files == null) {
            return null;
        }
        ProxyFile[] result = new ProxyFile[files.length];
        for (int i = 0; i < files.length; i++) {
            result[i] = wrap(files[i]);
        }
        return result;
    }

    private static ProxyFile wrap(File file) {
        if (file == null) {
            return null;
        }
        return new NativeFile(file);
    }

    private static FilenameFilter wrap(ProxyFilenameFilter filter) {
        return new FilenameFilterImpl(filter);
    }

    private static FileFilter wrap(ProxyFileFilter filter) {
        return new FileFilterImpl(filter);
    }

    private static class NativeFile implements ProxyFile {
        private final File delegate;

        private NativeFile(File delegate) {
            this.delegate = delegate;
        }

        @Override
        public ProxyFileInputStream createInputStream() throws FileNotFoundException {
            return new NativeFileInputStream(new FileInputStream(delegate));
        }

        @Override
        public ProxyFileOutputStream createOutputStream() throws FileNotFoundException {
            return new NativeFileOutputStream(new FileOutputStream(delegate));
        }

        @Override
        public ProxyFileOutputStream createOutputStream(boolean append) throws FileNotFoundException {
            return new NativeFileOutputStream(new FileOutputStream(delegate, append));
        }

        public boolean canExecute() {
            return delegate.canExecute();
        }

        public boolean setReadable(boolean readable, boolean ownerOnly) {
            return delegate.setReadable(readable, ownerOnly);
        }

        public ProxyFile[] listFiles() {
            return wrap(delegate.listFiles());
        }

        public long lastModified() {
            return delegate.lastModified();
        }

        public long length() {
            return delegate.length();
        }

        public boolean canWrite() {
            return delegate.canWrite();
        }

        public ProxyFile getCanonicalFile() throws IOException {
            return wrap(delegate.getCanonicalFile());
        }

        public String getParent() {
            return delegate.getParent();
        }

        public ProxyFile createTempFile(String prefix, String suffix) throws IOException {
            return wrap(File.createTempFile(prefix, suffix, delegate));
        }

        public boolean mkdirs() {
            return delegate.mkdirs();
        }

        public String getAbsolutePath() {
            return delegate.getAbsolutePath();
        }

        public boolean isHidden() {
            return delegate.isHidden();
        }

        public ProxyFile getParentFile() {
            return wrap(delegate.getParentFile());
        }

        public String[] list(ProxyFilenameFilter filter) {
            return delegate.list(wrap(filter));
        }

        public String[] list() {
            return delegate.list();
        }

        public boolean setWritable(boolean writable, boolean ownerOnly) {
            return delegate.setWritable(writable, ownerOnly);
        }

        public boolean setExecutable(boolean executable) {
            return delegate.setExecutable(executable);
        }

        public String getName() {
            return delegate.getName();
        }

        public boolean setWritable(boolean writable) {
            return delegate.setWritable(writable);
        }

        public boolean exists() {
            return delegate.exists();
        }

        public boolean renameTo(ProxyFile dest) {
            if (dest instanceof NativeFile) {
                return delegate.renameTo(((NativeFile) dest).delegate);
            } else {
                return false;
            }
        }

        public int compareTo(ProxyFile pathname) {
            if (pathname instanceof NativeFile) {
                return delegate.compareTo(((NativeFile) pathname).delegate);
            }
            return delegate.compareTo(new File(pathname.getPath(), pathname.getName()));
        }

        public long getTotalSpace() {
            return delegate.getTotalSpace();
        }

        public long getFreeSpace() {
            return delegate.getFreeSpace();
        }

        public boolean isAbsolute() {
            return delegate.isAbsolute();
        }

        public boolean setLastModified(long time) {
            return delegate.setLastModified(time);
        }

        public ProxyFile[] listFiles(ProxyFileFilter filter) {
            return wrap(delegate.listFiles(wrap(filter)));
        }

        public boolean createNewFile() throws IOException {
            return delegate.createNewFile();
        }

        public boolean mkdir() {
            return delegate.mkdir();
        }

        public boolean canRead() {
            return delegate.canRead();
        }

        public String getCanonicalPath() throws IOException {
            return delegate.getCanonicalPath();
        }

        public ProxyFile[] listFiles(ProxyFilenameFilter filter) {
            return wrap(delegate.listFiles(wrap(filter)));
        }

        public long getUsableSpace() {
            return delegate.getUsableSpace();
        }

        public boolean setReadOnly() {
            return delegate.setReadOnly();
        }

        public ProxyFile getAbsoluteFile() {
            return wrap(delegate.getAbsoluteFile());
        }

        public boolean isFile() {
            return delegate.isFile();
        }

        public void deleteOnExit() {
            delegate.deleteOnExit();
        }

        public boolean delete() {
            return delegate.delete();
        }

        public boolean setExecutable(boolean executable, boolean ownerOnly) {
            return delegate.setExecutable(executable, ownerOnly);
        }

        public boolean isDirectory() {
            return delegate.isDirectory();
        }

        public String getPath() {
            return delegate.getPath();
        }

        public boolean setReadable(boolean readable) {
            return delegate.setReadable(readable);
        }

    }

    private static class NativeFileInputStream extends ProxyFileInputStream {
        private final FileInputStream delegate;

        private NativeFileInputStream(FileInputStream delegate) {
            this.delegate = delegate;
        }

        public int available() throws IOException {
            return delegate.available();
        }

        public long skip(long n) throws IOException {
            return delegate.skip(n);
        }

        public void mark(int readlimit) {
            delegate.mark(readlimit);
        }

        public int read() throws IOException {
            return delegate.read();
        }

        public boolean markSupported() {
            return delegate.markSupported();
        }

        public void reset() throws IOException {
            delegate.reset();
        }

        public int read(byte[] b, int off, int len) throws IOException {
            return delegate.read(b, off, len);
        }

        public int read(byte[] b) throws IOException {
            return delegate.read(b);
        }

        public void close() throws IOException {
            delegate.close();
        }
    }

    private static class NativeFileOutputStream extends ProxyFileOutputStream {
        private final FileOutputStream delegate;

        private NativeFileOutputStream(FileOutputStream delegate) {
            this.delegate = delegate;
        }

        public void close() throws IOException {
            delegate.close();
        }

        public void write(byte[] b) throws IOException {
            delegate.write(b);
        }

        public void write(byte[] b, int off, int len) throws IOException {
            delegate.write(b, off, len);
        }

        public void write(int b) throws IOException {
            delegate.write(b);
        }

        public void flush() throws IOException {
            delegate.flush();
        }
    }

    private static class FilenameFilterImpl implements FilenameFilter {
        private final ProxyFilenameFilter filter;

        public FilenameFilterImpl(ProxyFilenameFilter filter) {
            this.filter = filter;
        }

        @Override
        public boolean accept(File dir, String name) {
            return filter.accept(wrap(dir), name);
        }
    }

    private static class FileFilterImpl implements FileFilter {
        private final ProxyFileFilter filter;

        public FileFilterImpl(ProxyFileFilter filter) {
            this.filter = filter;
        }

        @Override
        public boolean accept(File pathname) {
            return filter.accept(wrap(pathname));
        }
    }

}
