package org.dynjs.debugger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bob McWhirter
 */
public class ReferenceManager {

    private Map<Integer,Object> objectByReference = new HashMap<>();
    private Map<Object, Integer> referenceByObject = new HashMap<>();

    private int counter = 0;

    public ReferenceManager() {

    }

    public void reset() {
        this.objectByReference.clear();
        this.referenceByObject.clear();
        this.counter = 0;
    }

    public int getReference(Object object) {
        Integer ref = this.referenceByObject.get(object);
        if ( ref != null ) {
            return ref;
        }

        ref = ++counter;

        this.referenceByObject.put( object, ref );
        this.objectByReference.put( ref, object );

        return ref;
    }

    public Object getObject(int ref) {
        return this.objectByReference.get( ref );
    }
}
