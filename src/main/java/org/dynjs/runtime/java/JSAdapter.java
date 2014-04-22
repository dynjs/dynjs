/**
 *  Copyright 2014 Red Hat, and individual contributors
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
package org.dynjs.runtime.java;

import org.dynjs.runtime.*;
import org.dynjs.runtime.builtins.types.BuiltinObject;

/**
 * Provides Nashorn-like extensions in Javascript code. See: https://wiki.openjdk.java.net/display/Nashorn/Nashorn+extensions
 *
 * Example:
 *
 * var obj = new JSAdapter({
 *     var store = {};
 *     __get__: function(name) { return store[name]; }
 *     __put__: function(name, value) { store[name] = value; }
 * });
 *
 */
public class JSAdapter extends BuiltinObject {
    public JSAdapter(GlobalObject globalObject) {
        super(globalObject);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        Object arg = Types.UNDEFINED;
        if (args.length > 0) { arg = args[0]; }
        return new JSAdapterObject(context.getGlobalObject(), arg);
    }

    public static DynObject newObject(ExecutionContext context) {
        return (DynObject) context.construct((Object) null, (JSFunction) context.getGlobalObject().get(context, "__JS_Adapter"));
    }

    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/java/JSAdapter.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: JSAdapter>";
    }

    private class JSAdapterObject extends DynObject {

        private final JSObject proxy;

        public JSAdapterObject(GlobalObject globalObject, Object arg) {
            super(globalObject);
            setClassName("JSObject");
            if ((arg instanceof JSObject)) proxy = (JSObject) arg;
            else proxy = new DynObject(globalObject);
        }

        @Override
        public Object get(ExecutionContext context, String name) {
            final Object getter = proxy.get(context, "__get__");
            if (getter != Types.UNDEFINED) {
                return context.call((JSFunction)getter, this, name);
            }
            return super.get(context, name);
        }

        @Override
        public void put(ExecutionContext context, final String name, final Object value, final boolean shouldThrow) {
            final Object setter = proxy.get(context, "__set__");
            if (setter != Types.UNDEFINED) {
                context.call((JSFunction)setter, this, name, value);
                return;
            }
            super.put(context, name, value, shouldThrow);
        }
    }
}
