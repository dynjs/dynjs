package org.dynjs.runtime;

import java.io.IOException;
import java.io.Reader;

/**
 * @author Bob McWhirter
 */
public interface SourceProvider {

    Reader openReader() throws IOException;
}
