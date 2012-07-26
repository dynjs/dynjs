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

import me.qmx.jitescript.CodeBlock;
import org.dynjs.api.Function;
import org.dynjs.api.Scope;
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.runtime.builtins.DefineProperty;
import org.dynjs.runtime.builtins.Eval;
import org.dynjs.runtime.builtins.ParseFloat;
import org.dynjs.runtime.builtins.ParseInt;
import org.dynjs.runtime.builtins.Require;
import org.dynjs.runtime.modules.ConsoleModule;
import org.dynjs.runtime.modules.FilesystemModuleProvider;
import org.dynjs.runtime.modules.JavaClassModuleProvider;
import org.dynjs.runtime.modules.ModuleProvider;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DynThreadContext {

    public static final Object UNDEFINED = new Undefined();
    public static final Object NULL = new Null();
    public static final Function NOOP = new Noop();

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
        put("parseFloat", DynJSCompiler.wrapFunction(get("Function"), new ParseFloat()));
        put("parseInt", DynJSCompiler.wrapFunction(get("Function"), new ParseInt()));
        put("Math", new DynObject());
    }};

    private ThreadLocal<DynJS> runtime = new ThreadLocal<>();
    private AtomicInteger storageCounter = new AtomicInteger();
    private Map<Integer, CodeBlock> storage = new HashMap<>();
    private Scope scope = new DynObject();
    private Deque<Frame> frameStack = new LinkedList<>();
    private DynamicClassLoader classLoader;
    private List<String> loadPaths = Collections.synchronizedList(new ArrayList<String>());
    private List<ModuleProvider> moduleProviders = Collections.synchronizedList(new ArrayList<ModuleProvider>());
    private PrintStream outputStream = System.out;
    private PrintStream errorStream = System.err;

    public DynThreadContext() {
        for (Map.Entry<String, Object> builin : BUILTINS.entrySet()) {
            scope.define(builin.getKey(), builin.getValue());
        }
        loadPaths.add(System.getProperty("user.dir") + "/");
        loadPaths.add(System.getProperty("user.home") + "/.node_modules/");
        loadPaths.add(System.getProperty("user.home") + "/.node_libraries/");
        loadPaths.add("/usr/local/lib/node/");
        loadPaths.add("/usr/local/lib/node_modules/");
        
        moduleProviders.add( new FilesystemModuleProvider() );
        
        JavaClassModuleProvider builtInModuleProvider = new JavaClassModuleProvider();
        builtInModuleProvider.addModule( new ConsoleModule() );
        moduleProviders.add(  builtInModuleProvider );
    }
    
    
    /** Copy/child constructor.
     * 
     * <p>Build a new context based upon a parent. While a new
     * frame-stack and scope is provided, the following are
     * initialized through the passed-on parent context:</p>
     * 
     * <ul>
     *   <li>Load paths</li>
     *   <li>Module providers</li>
     *   <li>Class loader</li>
     * </ul>
     * @param original The original context to seed the child.
     */
    public DynThreadContext(DynThreadContext original) {
    	this();
    	setLoadPaths( new ArrayList<>( original.getLoadPaths() ) );
    	setModuleProviders( new ArrayList<>( original.getModuleProviders() ) );
    	setClassLoader( original.getClassLoader() );
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

    public void setClassLoader(DynamicClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public DynamicClassLoader getClassLoader() {
        return classLoader;
    }

    public Object getBuiltin(String name) {
        return BUILTINS.get(name);
    }

    public Map<Class<?>, Scope> getCapturedScopeStore() {
        return getRuntime().getCapturedScopeStore();
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
    
    public void setOutputStream(PrintStream outputStream) {
        this.outputStream = outputStream;
    }
    
    public PrintStream getOutputStream() {
        return this.outputStream;
    }
    
    public void setErrorStream(PrintStream errorStream) {
        this.errorStream = errorStream;
    }
    
    public PrintStream getErrorStream() {
        return this.errorStream;
    }
    
    public void addModuleProvider(ModuleProvider moduleProvider) {
    	moduleProviders.add( moduleProvider );
    }
    
    public List<ModuleProvider> getModuleProviders() {
    	return Collections.unmodifiableList( this.moduleProviders);
    }
    
    public void setModuleProviders(List<ModuleProvider> newModuleProviders) {
    	moduleProviders = Collections.synchronizedList( moduleProviders );
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

    public Deque<Frame> getFrameStack() {
        return frameStack;
    }

    private static class Noop implements Function {
        @Override
        public Object call(Object self, DynThreadContext context, Object... arguments) {
            return null;
        }

        @Override
        public String[] getParameters() {
            return new String[0];
        }
    }
}
