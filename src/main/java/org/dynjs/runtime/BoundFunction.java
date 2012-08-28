package org.dynjs.runtime;

import java.util.Arrays;

public class BoundFunction extends AbstractNativeFunction {

    private JSFunction target;
    private Object boundThis;
    private Object[] boundArgs;

    public BoundFunction(LexicalEnvironment scope, final JSFunction target, final Object boundThis, final Object[] boundArgs) {
        super(scope, true);
        this.target = target;
        this.boundThis = boundThis;
        this.boundArgs = boundArgs;
        int l = 0;
        if (target.getClassName().equals("function")) {
            l = ((Integer) target.get(null, "length")) - boundArgs.length;
            if (l < 0) {
                l = 0;
            }
        }
        final int realLength = l;
        defineOwnProperty(null, "length", new PropertyDescriptor() {
            {
                set("Value", realLength);
                set("Writable", false);
                set("Configurable", false);
                set("Enumerable", false);
            }
        }, false);
        final JSFunction thrower = (JSFunction) scope.getGlobalObject().get( null, "__throwTypeError" );
        defineOwnProperty(null, "caller", new PropertyDescriptor() {
            {
                set( "Set", thrower );
                set( "Get", thrower );
                set( "Configurable", false );
                set( "Enumerable", false );
            }
        }, false);
        defineOwnProperty(null, "arguments", new PropertyDescriptor() {
            {
                set( "Set", thrower );
                set( "Get", thrower );
                set( "Configurable", false );
                set( "Enumerable", false );
            }
        }, false);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.3.4.5.1
        Object[] allArgs = new Object[boundArgs.length + args.length];

        for (int i = 0; i < boundArgs.length; ++i) {
            allArgs[i] = boundArgs[i];
        }

        for (int i = boundArgs.length, j = 0; i < boundArgs.length + args.length; ++i, ++j) {
            allArgs[i] = args[j];
        }

        if (self != Types.UNDEFINED) {
            return context.call(this.target, self, allArgs);
        } else {
            return context.call(this.target, this.boundThis, allArgs);
        }
    }

    @Override
    public boolean hasInstance(Object v) {
        // 15.3.4.5.3
        return target.hasInstance(v);
    }

}
