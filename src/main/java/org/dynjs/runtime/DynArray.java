/**
 *  Copyright 2013 Douglas Campos, and individual contributors
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

import org.dynjs.exception.ThrowException;

public class DynArray extends DynObject {

    public DynArray(GlobalContext globalContext) {
        super(globalContext);
        setClassName("Array");
        super.defineOwnProperty(null, "length",
                PropertyDescriptor.newDataPropertyDescriptor(0L, true, true, true), false);
        setPrototype(globalContext.getPrototypeFor("Array"));
    }

    @Override
    public boolean defineOwnProperty(ExecutionContext context, String name, PropertyDescriptor desc, boolean shouldThrow) {
        // 15.4.5.1
        PropertyDescriptor oldLenDesc = (PropertyDescriptor) getOwnProperty(context, "length");
        long oldLen = (long) oldLenDesc.getValue();

        if (name.equals("length")) {
            if (desc.getValue() == null ) {
                return super.defineOwnProperty(context, "length", desc, shouldThrow);
            }

            PropertyDescriptor newLenDesc = desc;
            Long newLen = Types.toUint32(context, desc.getValue());
            if (!Types.compareEquality(context, newLen, Types.toNumber(context, desc.getValue()))) {
                throw new ThrowException(context, context.createRangeError("invalid length: " + newLen));
            }
            newLenDesc.setValue(newLen);
            if (newLen >= oldLen) {
                return super.defineOwnProperty(context, "length", newLenDesc, shouldThrow);
            }

            if (!oldLenDesc.hasWritable() && !oldLenDesc.isWritable()) {
                return reject(context, shouldThrow);
            }

            boolean newWritable = false;
            if ((!newLenDesc.hasWritable()) || newLenDesc.isWritable()) {
                newWritable = true;
            } else {
                newWritable = false;
                newLenDesc.setWritable(true);
            }

            boolean succeeded = super.defineOwnProperty(context, "length", newLenDesc, shouldThrow);

            if (!succeeded) {
                return false;
            }

            while (newLen < oldLen) {
                oldLen = oldLen - 1;
                boolean deleteSucceeded = delete(context, "" + oldLen, false);

                if (!deleteSucceeded) {
                    newLenDesc.setValue(oldLen + 1);
                    if (!newWritable) {
                        newLenDesc.setWritable(false);
                    }
                    super.defineOwnProperty(context, "length", newLenDesc, false);
                    return reject(context, shouldThrow);

                }
            }

            if (newWritable == false) {
                PropertyDescriptor lengthDesc = new PropertyDescriptor();
                lengthDesc.setWritable(false);
                super.defineOwnProperty(context, "length", lengthDesc, false);
            }

            return true;
        } // 'length'

        if (isArrayIndex(context, name)) {
            Long index = Types.toUint32(context, name);
            if (index.longValue() >= oldLen && oldLenDesc.hasWritable() && !oldLenDesc.isWritable()) {
                return reject(context, shouldThrow);
            }
            boolean succeeded = super.defineOwnProperty(context, name, desc, shouldThrow);
            if (!succeeded) {
                return reject(context, shouldThrow);
            }

            if (index.longValue() >= oldLen && index.longValue() < 4294967295L) {
                oldLenDesc.setValue(index.longValue() + 1);
                super.defineOwnProperty(context, "length", oldLenDesc, false);
            }
            return true;
        }

        return super.defineOwnProperty(context, name, desc, shouldThrow);
    }
    
    public long length() {
        return Types.toInt32(null, this.get(null, "length"));
    }

    protected boolean isArrayIndex(ExecutionContext context, String name) {
        return name.equals(Types.toUint32(context, name).toString());
    }

}
