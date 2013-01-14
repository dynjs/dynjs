package org.dynjs.parser.js;

import java.io.IOException;
import java.io.Reader;

public class CircularCharBuffer implements CharStream {
    private static final int DEFAULT_BUFFER_SIZE = 2048;

    private Reader in;
    private char[] buf;
    private int readPos = 0;
    private int writePos = 0;
    private int available = 0;
    private boolean eof = false;

    public CircularCharBuffer(Reader in) throws IOException {
        this( in, DEFAULT_BUFFER_SIZE );
    }
    
    public CircularCharBuffer(Reader in, int bufferSize) throws IOException {
        this.in = in;
        this.buf = new char[ bufferSize ];
        fill();
    }
    
    public int available() {
        return this.available;
    }
    
    private void fill() throws IOException {
        int len = 0;
        
        if ( this.writePos == this.readPos && available > 0 ) {
            return;
        }
        
        if ( this.writePos >= this.readPos ) {
            len = this.buf.length - this.writePos;
        } else {
            len = this.readPos - this.writePos;
        }
        
        int amountRead = this.in.read(this.buf, this.writePos, len);
        
        if ( amountRead < 0 ) {
            this.eof = true;
            return;
        }
        
        this.available += amountRead;
        this.writePos = ( this.writePos + amountRead ) % this.buf.length;
        
    }
    
    public int peek() throws IOException {
        return peek(1);
    }
    
    public int peek(int pos) throws IOException {
        if ( available() < pos ) {
            fill();
        }
        if ( available() < pos ) {
            return -1;
        }
        int c = ( readPos + pos - 1) % this.buf.length;
        return buf[c];
    }
    
    public int consume() throws IOException {
        int c = peek();
        ++this.readPos;
        --this.available;
        if ( this.readPos == this.buf.length ) {
            this.readPos = 0;
        }
        return c;
    }


}
