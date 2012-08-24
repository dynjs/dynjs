package org.dynjs.runtime;

import java.util.List;

public class StackGetter extends AbstractNativeFunction {

    private String errorName;
    private List<StackElement> stack;

    public StackGetter(GlobalObject globalObject, String errorName, List<StackElement> stack) {
        super(globalObject, false );
        this.errorName = errorName;
        this.stack = stack;
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        StringBuffer buf = new StringBuffer();
        if ( this.errorName == null ) {
            buf.append( "<unknown>\n" );
        } else {
            buf.append( this.errorName + "\n" );
        }
        for ( StackElement each : stack ) {
            buf.append( "  at " + each + "\n" );
        }
        return buf.toString();
    }

}
