package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;

public class AbstractBuiltinNativeError extends AbstractNativeFunction {

    public AbstractBuiltinNativeError(GlobalObject globalObject, final String name) {
        super(globalObject, true, "message");
        DynObject proto = new DynObject();
        proto.setClassName("String");
        proto.defineOwnProperty(null, "constructor", new PropertyDescriptor() {
            {
                set( "Value", AbstractBuiltinNativeError.this );
            }
        }, false);
        proto.defineOwnProperty(null, "name", new PropertyDescriptor() {
            {
                set( "Value", name );
            }
        }, false);
        proto.defineOwnProperty(null, "message", new PropertyDescriptor() {
            {
                set("Value", "" );
            }
        }, false);
        
        JSObject errorProto = ((JSObject)globalObject.get( null, "Error" )).getPrototype();
        proto.setPrototype( errorProto );
        setPrototype(proto);
    }

    @Override
    public Object call(ExecutionContext context, Object self, final Object... args) {
        JSObject o = null;

        System.err.println( "# A: " + self );
        if (self == Types.UNDEFINED ) {
           System.err.println( "# B" );
            o = createNewObject();
        } else {
           System.err.println( "# C" );
            o = (JSObject) self;
        }

        if (args[0] != Types.UNDEFINED) {
            o.defineOwnProperty(context, "message", new PropertyDescriptor() {
                {
                    set("Value", args[0]);
                }
            }, false);
        }
        
        return o;
    }

    @Override
    public JSObject createNewObject() {
        System.err.println( "*** create new object" ) ;
        DynObject o = new DynObject();
        o.setPrototype(getPrototype());
        o.setClassName("Error");
        return o;
    }

}
