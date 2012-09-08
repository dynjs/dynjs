package org.dynjs.runtime.builtins.types.date.prototype;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.builtins.types.date.DynDate;
import org.joda.time.DateTime;

public class AbstractDateTimeFormatter extends AbstractNativeFunction {
    protected final String formatPattern;

    public AbstractDateTimeFormatter(GlobalObject globalObject, String formatPattern) {
        super(globalObject);
        this.formatPattern = formatPattern;
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        return new DateTime((Long) ((DynDate) self).getPrimitiveValue()).toString(formatPattern);
    }
}
