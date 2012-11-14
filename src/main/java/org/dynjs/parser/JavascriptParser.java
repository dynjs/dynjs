package org.dynjs.parser;

import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.TokenStream;
import org.dynjs.runtime.ExecutionContext;

public class JavascriptParser extends ECMAScriptParser {

    public JavascriptParser(ExecutionContext context, TokenStream input) {
        this(context, input, new RecognizerSharedState());
    }
    
    public JavascriptParser(ExecutionContext context, TokenStream input, RecognizerSharedState state) {
        super(input, state);
        setTreeAdaptor( new ParserWatcher( context ) );
    }

}
