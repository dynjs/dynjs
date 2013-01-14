package org.dynjs.parser.js;

import java.io.IOException;


public interface CharStream {
    
    int peek() throws IOException;
    int peek(int pos) throws IOException;
    int consume() throws IOException;
    
}
