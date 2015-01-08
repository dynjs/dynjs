package org.dynjs.debugger.model;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.Types;

/**
 * @author Bob McWhirter
 */
public class Func {

    private final ExecutionContext context;

    public Func(ExecutionContext context) {
        this.context = context;
    }

    public String getName() {
        Object name = this.context.getFunction().get(this.context, "name");
        if ( name == Types.UNDEFINED ) {
            return "";
        }

        return name.toString();
    }

    public String getInferredName() {
        String name = getName();

        if ( ! name.equals( "" ) ) {
            return name;
        }

        Object ref = this.context.getFunctionReference();

        if ( ref instanceof Reference ) {
            return ((Reference) ref).getReferencedName();
        }

        return "";
    }
}
