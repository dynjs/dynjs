package org.dynjs.jsr223;

import org.dynjs.runtime.DynJS;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.SimpleBindings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Bob McWhirter
 */
public class DynJSScriptEngineFactory implements ScriptEngineFactory {

    public static final String ENGINE_NAME = "dynjs";
    public static final List<String> MIME_TYPES = new ArrayList<>();
    public static final List<String> NAMES = new ArrayList<>();
    public static final String LANGUAGE_NAME = "javascript";
    public static final String LANGUAGE_VERSION = "5";

    static {
        NAMES.add( "javascript" );
        NAMES.add( "ecmascript" );
        NAMES.add( "dynjs" );

        MIME_TYPES.add( "application/javascript" );
        MIME_TYPES.add( "text/javascript" );
        MIME_TYPES.add( "application/x-javascript" );
    }

    private Bindings globalBindings;

    public DynJSScriptEngineFactory() {
        this.globalBindings = new SimpleBindings();
    }

    Bindings getGlobalBindings() {
        return this.globalBindings;
    }

    @Override
    public String getEngineName() {
        return ENGINE_NAME;
    }

    @Override
    public String getEngineVersion() {
        return DynJS.VERSION;
    }

    @Override
    public List<String> getExtensions() {
        return Collections.emptyList();
    }

    @Override
    public List<String> getMimeTypes() {
        return Collections.unmodifiableList(MIME_TYPES);
    }

    @Override
    public List<String> getNames() {
        return Collections.unmodifiableList(NAMES);
    }

    @Override
    public String getLanguageName() {
        return LANGUAGE_NAME;
    }

    @Override
    public String getLanguageVersion() {
        return LANGUAGE_VERSION;
    }

    @Override
    public Object getParameter(String key) {
        if ( key.equals( ScriptEngine.ENGINE ) ) {
            return getScriptEngine();
        } else if ( key.equals( ScriptEngine.ENGINE_VERSION ) ) {
            return getEngineVersion();
        } else if ( key.equals( ScriptEngine.NAME ) ) {
            return getEngineName();
        } else if ( key.equals( ScriptEngine.LANGUAGE ) ) {
            return getLanguageName();
        } else if ( key.equals( ScriptEngine.LANGUAGE_VERSION ) ) {
            return getLanguageVersion();
        }

        return null;
    }

    @Override
    public String getMethodCallSyntax(String obj, String m, String... args) {
        StringBuilder builder = new StringBuilder();

        builder.append( obj ).append( "." ).append( m );
        builder.append( "(" );

        for ( int i = 0 ; i < args.length ; ++i ) {
            if ( i > 0 ) {
                builder.append( "," );
            }
            builder.append( args[i] );
        }
        builder.append( ")" );

        return builder.toString();
    }

    @Override
    public String getOutputStatement(String toDisplay) {
        return "System.out.println('" + toDisplay + "');";
    }

    @Override
    public String getProgram(String... statements) {
        StringBuilder builder = new StringBuilder();
        for ( int i = 0 ; i < statements.length ; ++i ) {
            builder.append( statements[i] );
            if ( ! statements[i].endsWith( ";" ) ) {
                builder.append( ";" );
            }
            builder.append( "\n" );
        }
        return builder.toString();
    }

    @Override
    public ScriptEngine getScriptEngine() {
        return new DynJSScriptEngine( this );
    }
}
