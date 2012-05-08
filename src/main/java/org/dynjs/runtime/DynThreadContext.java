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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import me.qmx.jitescript.CodeBlock;

import org.dynjs.api.Function;
import org.dynjs.api.Scope;
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.runtime.builtins.DefineProperty;
import org.dynjs.runtime.builtins.Eval;
import org.dynjs.runtime.builtins.Require;

public class DynThreadContext {

    public static final Object UNDEFINED = new Undefined();
    public static final Object NULL = new Null();

    private static final Map<String, Object> BUILTINS = new LinkedHashMap<String, Object>() {{
        put("undefined", UNDEFINED);
        put("NaN", Double.NaN);
        put("Infinity", Double.POSITIVE_INFINITY);
        put("-Infinity", Double.NEGATIVE_INFINITY);
        put("Object", new DynObject() {{
            setProperty("defineProperty", new DefineProperty());
        }});
        put("Number", new DynObject());
        put("Array", new DynObject());
        put("Date", new DynObject());
        put("String", new DynObject());
        put("Boolean", new DynObject());
        put("Error", new DynObject());
        put("Function", new DynObject() {{
            setProperty("prototype", get("Object"));
        }});
        put("eval", DynJSCompiler.wrapFunction(get("Function"), new Eval()));
        put("require", DynJSCompiler.wrapFunction(get("Function"), new Require()));
        put("Math", new DynObject());
    }};

    private ThreadLocal<DynJS> runtime = new ThreadLocal<>();
    private AtomicInteger storageCounter = new AtomicInteger();
    private Map<Integer, CodeBlock> storage = new HashMap<>();
    private Scope scope = new DynObject();
    private Deque<Function> callStack = new LinkedList<>();
    private DynamicClassLoader classLoader;
    private List<String> loadPaths = Collections.synchronizedList(new ArrayList<String>());

    public DynThreadContext() {
        for (Map.Entry<String, Object> builin : BUILTINS.entrySet()) {
            scope.define(builin.getKey(), builin.getValue());
        }
        loadPaths.add(System.getProperty("user.dir") + "/");
        loadPaths.add(System.getProperty("user.home") + "/.node_modules/");
        loadPaths.add(System.getProperty("user.home") + "/.node_libraries/");
        loadPaths.add("/usr/local/lib/node/");
    }

    public DynJS getRuntime() {
        return this.runtime.get();
    }

    public void setRuntime(DynJS runtime) {
        this.runtime.set(runtime);
    }
    
    public String defineStringLiteral(final String value) {
        return value;
    }

    public Number defineDecimalLiteral(final String value) {
        return Double.parseDouble(value);
    }

    public Number defineOctalLiteral(final String value) {
        return Integer.parseInt(value, 8);
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    /**
     * stores codeblock on internal storage
     *
     * @param block
     */
    public int store(CodeBlock block) {
        Integer slot = storageCounter.getAndIncrement();
        storage.put(slot, block);
        return slot;
    }

    /**
     * retrieves codeblock from internal storage
     *
     * @param id
     * @return
     */
    public CodeBlock retrieve(int id) {
        return storage.get(id);
    }

    public Deque<Function> getCallStack() {
        return callStack;
    }

    public void setClassLoader(DynamicClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public DynamicClassLoader getClassLoader() {
        return classLoader;
    }

    public Object getBuiltin(String name) {
        return BUILTINS.get(name);
    }

    public static class Undefined {

        private Undefined() {
        }

        public String typeof() {
            return "undefined";
        }

        @Override
        public String toString() {
            return "undefined";
        }
    }

    public static class Null {
        private Null() {
        }

        public String typeof() {
            return "object";
        }

        @Override
        public String toString() {
            return "null";
        }
    }

    public void addLoadPath(String loadPath) {
    	loadPaths.add(loadPath);
    }
    
    public List<String> getLoadPaths() {
    	return Collections.unmodifiableList(loadPaths);
    }

	public void setLoadPaths(List<String> newLoadPaths) {
	    loadPaths = Collections.synchronizedList(newLoadPaths);		
	}
}
