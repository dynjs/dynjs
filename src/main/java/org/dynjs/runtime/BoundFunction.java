package org.dynjs.runtime;

import java.util.Arrays;

public class BoundFunction extends AbstractNativeFunction {

    private JSFunction target;
    private Object boundThis;
    private Object[] boundArgs;

    public BoundFunction(LexicalEnvironment scope, boolean strict, final JSFunction target, final Object boundThis, final Object[] boundArgs) {
        super(scope, strict);
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
                set("Writable", false );
                set("Configurable", false );
                set("Enumerable", false );
            }
        }, false);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.3.4.5.1
        Object[] allArgs = new Object[ boundArgs.length + args.length ];
        
        for ( int i = 0 ; i < boundArgs.length ;++i) {
            allArgs[i] = boundArgs[i];
        }
        
        System.err.println( "--" );
        for ( int i = boundArgs.length, j=0 ; i < boundArgs.length + args.length; ++i, ++j ) {
            allArgs[i] = args[j];
        }
        
        return context.call( this.target, this.boundThis, allArgs );
    }

}
