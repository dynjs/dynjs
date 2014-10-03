package org.dynjs.jsr223;

import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Types;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.SimpleBindings;

/**
 * @author Bob McWhirter
 */
public class ScriptEngineGlobalObject extends DynObject {

    private final DynJSScriptContext context;
    private Bindings bindings;

    public ScriptEngineGlobalObject(DynJSScriptContext context) {
        this.context = context;
        this.bindings = new SimpleBindings();
    }

    public ScriptEngineGlobalObject(ScriptEngineGlobalObject parent, Bindings bindings) {
        super( parent );
        this.context = parent.context;
        this.bindings = bindings;
    }

    Bindings getBindings() {
        return this.bindings;
    }

    void setBindings(Bindings bindings) {
        this.bindings = bindings;
    }

    @Override
    public Object get(ExecutionContext context, String name) {
        Object value = super.get( context, name );
        if ( value == null || value == Types.UNDEFINED  ) {
            value = this.bindings.get( name );
            if ( value == null ) {
                value = this.context.getEngine().getFactory().getGlobalBindings().get(name);
            }
        }
        if ( value == null ) {
            return Types.UNDEFINED;
        }

        return value;
    }

    @Override
    public boolean hasProperty(ExecutionContext context, String name) {
        boolean has = super.hasProperty( context, name );

        if ( has ) {
            return true;
        }

        has = this.context.getEngine().getFactory().getGlobalBindings().containsKey( name );

        if ( has ) {
            return true;
        }

        return this.bindings.containsKey( name );
    }

    /*
    @Override
    public void put(ExecutionContext context, String name, Object value, boolean shouldThrow) {
        if ( this.bindings.containsKey( name ) ) {
            this.bindings.put( name, value );
        } else if ( this.context.getEngine().getFactory().getGlobalBindings().containsKey( value ) ) {
            this.context.getEngine().getFactory().getGlobalBindings().put( name, value );
        } else {
            super.put( context, name, value, shouldThrow );
        }
    }
    */


}
