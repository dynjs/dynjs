package org.dynjs.debugger.requests;

import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.NameEnumerator;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Bob McWhirter
 */
public class EvaluateResponse extends AbstractResponse<EvaluateRequest> {

    private final Object result;

    public EvaluateResponse(EvaluateRequest request, Object result, boolean success, boolean running) {
        super(request, success, running);
        this.result = result;
    }

    public Object getResult() {
        return this.result;
    }

    @Override
    public Collection<Object> getRefs() {
        List<Object> refs = new ArrayList<>();

        getRefs( this.result, refs );

        return refs;
    }

    private void getRefs(Object value, List<Object> refs) {
        if ( refs.contains( value ) ) {
            return;
        }

        refs.add( value );

        if (value instanceof JSObject) {
            NameEnumerator enumerator = ((JSObject)value).getAllEnumerablePropertyNames();

            while (enumerator.hasNext()) {
                String name = enumerator.next();
                Object propResult = ((JSObject)value).getProperty(null, name);

                if (propResult != Types.UNDEFINED) {
                    PropertyDescriptor prop = (PropertyDescriptor) propResult;

                    if (prop.hasValue()) {
                        Object v = prop.getValue();
                        refs.add( v );
                    }
                }
            }
        }
    }
}
