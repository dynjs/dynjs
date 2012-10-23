package org.dynjs.parser;

import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.TokenStream;

public class JavascriptParser extends ES3Parser {

    public JavascriptParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    
    public JavascriptParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
        setTreeAdaptor( new ParserWatcher() );
    }

}
