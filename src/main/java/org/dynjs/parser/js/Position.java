package org.dynjs.parser.js;

public interface Position {
    
    String getFileName();
    int getLine();
    int getColumn();

}
