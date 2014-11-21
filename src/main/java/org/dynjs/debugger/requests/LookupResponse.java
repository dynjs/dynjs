package org.dynjs.debugger.requests;

import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.NameEnumerator;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;

import java.util.*;

/**
 * @author Bob McWhirter
 */
public class LookupResponse extends AbstractResponse<LookupRequest> {

    private List<Object> objects = new ArrayList<>();

    public LookupResponse(LookupRequest request, boolean success, boolean running) {
        super(request, success, running);
    }

    public List<Object> getList() {
        return this.objects;
    }

    @Override
    public Collection<?> getRefs() {
        List<Object> refs = new ArrayList<>();

        for ( Object each : this.objects ) {
            getRefs( each, refs );
        }

        return refs;
    }

    protected void getRefs(Object value, List<Object> refs) {
        if (value instanceof JSObject) {
            NameEnumerator enumerator = ((JSObject) value).getAllEnumerablePropertyNames();

            while (enumerator.hasNext()) {
                String name = enumerator.next();
                Object propResult = ((JSObject) value).getProperty(null, name);

                if (propResult != Types.UNDEFINED) {
                    PropertyDescriptor prop = (PropertyDescriptor) propResult;

                    if (prop.hasValue()) {
                        Object v = prop.getValue();
                        refs.add(v);
                    }
                }
            }
        }
    }
}

