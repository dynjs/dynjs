/**
 *  Copyright 2012 Douglas Campos, and individual contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.dynjs.runtime;

import java.util.Arrays;

import org.dynjs.exception.RangeError;

public class DynArray extends DynObject {

    public DynArray() {
        super.defineOwnProperty(null, "length", new PropertyDescriptor() {
            {
                set( "Writable", true );
                set( "Configurable", true );
                set( "Enumerable", true );
                set( "Value", 0 );
            }
        }, false);
    }

    @Override
    public boolean defineOwnProperty(ExecutionContext context, String name, PropertyDescriptor desc, boolean shouldThrow) {
        // 15.4.5.1
        
        System.err.println( "defineOwnProperty: " + name );
        PropertyDescriptor oldLenDesc = (PropertyDescriptor) getOwnProperty(context, "length");
        System.err.println( "oldLenDesc: " + oldLenDesc );
        int oldLen = (int) oldLenDesc.getValue();

        if (name.equals("length")) {
            System.err.println( "A" );
            if (desc.getValue() == Types.UNDEFINED) {
                return super.defineOwnProperty(context, "length", desc, shouldThrow);
            }

            PropertyDescriptor newLenDesc = desc;
            System.err.println( "new desc: " + newLenDesc );
            Integer newLen = Types.toUint32(desc.getValue());
            if (!newLen.equals(Types.toNumber(desc.getValue()))) {
                throw new RangeError();
            }
            newLenDesc.setValue(newLen);
            if (newLen >= oldLen) {
                return super.defineOwnProperty(context, "length", newLenDesc, shouldThrow);
            }
            
            System.err.println( "B" );
            if (oldLenDesc.get("Writable") == Boolean.FALSE) {
                System.err.println( "reject" );
                return reject(shouldThrow);
            }

            boolean newWritable = false;
            if ((!oldLenDesc.hasWritable()) || oldLenDesc.get("Writable") == Boolean.TRUE) {
                newWritable = true;
            } else {
                newWritable = false;
                newLenDesc.set("Writable", true);
            }

            System.err.println( "C" );
            boolean succeeded = super.defineOwnProperty(context, "length", newLenDesc, shouldThrow);
            
            System.err.println( "succeeded? " + succeeded );

            if (!succeeded) {
                return false;
            }

            System.err.println( "D" );
            while (newLen < oldLen) {
                oldLen = oldLen - 1;
                System.err.println( "deleting " + oldLen );
                boolean deleteSucceeded = delete(context, "" + oldLen, false);

                if (!deleteSucceeded) {
                    newLenDesc.setValue(oldLen + 1);
                    if (!newWritable) {
                        newLenDesc.setWritable(false);
                    }
                    super.defineOwnProperty(context, "length", newLenDesc, false);
                    return reject(shouldThrow);

                }
            }

            if (newWritable == false) {
                super.defineOwnProperty(context, "length", new PropertyDescriptor() {
                    {
                        set("Writable", false);
                    }
                }, false);
            }

            return true;
        } // 'length'

        if (isArrayIndex(name)) {
            Integer index = Types.toUint32(name);
            if ((index.intValue() > oldLen) && oldLenDesc.get("Writable") == Boolean.FALSE) {
                return reject(shouldThrow);
            }
            boolean succeeded = super.defineOwnProperty(context, name, desc, shouldThrow);
            if (!succeeded) {
                return reject(shouldThrow);
            }

            if (index.intValue() >= oldLen) {
                oldLenDesc.setValue(index.intValue() + 1);
                super.defineOwnProperty(context, "length", oldLenDesc, false);
            }
            return true;
        }

        return super.defineOwnProperty(context, name, desc, shouldThrow);
    }

    protected boolean isArrayIndex(String name) {
        return name.equals(Types.toUint32(name).toString());
    }

}
