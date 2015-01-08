package org.dynjs.runtime;

/**
 * @author Bob McWhirter
 */
public interface ExternalIndexedData {

    Object get(long index);
    void put(long index, Object value);

}
