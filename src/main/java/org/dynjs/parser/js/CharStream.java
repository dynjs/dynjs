package org.dynjs.parser.js;

import java.io.IOException;


public interface CharStream {
    
    char peek() throws IOException;
    char peek(int pos) throws IOException;
    char consume() throws IOException;
    
}
