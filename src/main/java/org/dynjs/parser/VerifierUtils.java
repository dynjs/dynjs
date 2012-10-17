package org.dynjs.parser;

import java.util.HashSet;
import java.util.Set;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.ExecutionContext;

public class VerifierUtils {
    
    private static Set<String> DISALLOWED_STRICT_IDENTIFIERS = new HashSet<String>() {{
        add( "eval" );
        add( "arguments" );
    }};
    
    private static final Set<String> FUTURE_RESERVED_WORDS = new HashSet<String>() {
        {
            add("implements");
            add("let");
            add("private");
            add("public");
            add("yield");
            add("interface");
            add("package");
            add("protected");
            add("static");
        }
    };
    
    public static void verifyStrictIdentifier(ExecutionContext context, String identifier) {
        if ( DISALLOWED_STRICT_IDENTIFIERS.contains( identifier ) || FUTURE_RESERVED_WORDS.contains( identifier ) ) {
            throw new ThrowException(context, context.createSyntaxError( identifier + " not allowed as an identifier in strict-mode" ) );
        }
    }

}
