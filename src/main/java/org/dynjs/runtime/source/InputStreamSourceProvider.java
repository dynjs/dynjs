package org.dynjs.runtime.source;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * @author Bob McWhirter
 */
public class InputStreamSourceProvider extends ReaderSourceProvider {


    public InputStreamSourceProvider(InputStream source) throws IOException {
        super( new InputStreamReader( source ) );
    }

}
