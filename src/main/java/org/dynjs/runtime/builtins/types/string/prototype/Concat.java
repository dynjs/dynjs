package org.dynjs.runtime.builtins.types.string.prototype;

import org.dynjs.runtime.AbstractNonConstructorFunction;
import org.dynjs.runtime.Arguments;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.Types;

public class Concat extends AbstractNonConstructorFunction {

    public Concat(GlobalContext globalContext) {
        super(globalContext, "string1");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.4.5
        Types.checkObjectCoercible(context, self);

        Arguments argsObj = (Arguments) context.resolve("arguments").getValue(context);
        int numArgs = (int) argsObj.get(context, "length");

        StringBuilder s = new StringBuilder();
        
        s.append( Types.toString(context, self));
        
        for ( int i = 0 ; i < numArgs; ++i ) {
            s.append( Types.toString( context, args[i] ) );
        }
        
        return s.toString();
    }

}
