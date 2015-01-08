package org.dynjs.runtime;

import java.io.IOException;
import java.io.Reader;

/**
 * @author Bob McWhirter
 */
public interface SourceProvider {

    String getName();
    void setName(String name);

    long getSourceLength();

    long getLineCount();

    int getId();

    Reader openReader() throws IOException;

    String getSource() throws IOException;
}
