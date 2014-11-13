package org.dynjs.jsr223;

import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Types;

import javax.script.Bindings;
import javax.script.ScriptContext;

/**
 * @author Bob McWhirter
 */
public class ScriptEngineGlobalObject extends DynObject {

    private final ScriptContext context;

    public ScriptEngineGlobalObject(ScriptContext context) {
        this.context = context;
    }

    public ScriptEngineGlobalObject(ScriptEngineGlobalObject parent, Bindings bindings) {
        super( parent );
        this.context = parent.context;
    }

    @Override
    public Object get(ExecutionContext context, String name) {
        Object value = super.get( context, name );
        if ( value == null || value == Types.UNDEFINED  ) {
            value = this.context.getAttribute( name );
        }

        if ( value == null ) {
            return Types.UNDEFINED;
        }

        return value;
    }

    @Override
    public Object put(String key, Object value) {
        return super.put(key, value);
    }

    @Override
    public boolean hasProperty(ExecutionContext context, String name) {
        if ( super.hasProperty( context, name ) ) {
            return true;
        }

        return ( this.context.getAttributesScope( name ) >= 0 );
    }



}
