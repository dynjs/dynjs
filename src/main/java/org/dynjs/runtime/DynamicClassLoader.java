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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class DynamicClassLoader extends URLClassLoader {

    public DynamicClassLoader(ClassLoader parentClassLoader) {
        super(new URL[0], parentClassLoader);
    }

    public DynamicClassLoader() {
        super(new URL[0], DynamicClassLoader.class.getClassLoader());
    }

    public Class<?> define(String className, byte[] bytecode) {
        return super.defineClass(className, bytecode, 0, bytecode.length);
    }

    public void append(String path) throws MalformedURLException {
        final URL url = getURL(path);
        addURL(url);
    }

    private URL getURL(String target) throws MalformedURLException {
        try {
            // First try assuming a protocol is included
            return new URL(target);
        } catch (MalformedURLException e) {
            // Assume file: protocol
            File f = new File(target);
            String path = target;
            if (f.exists() && f.isDirectory() && !path.endsWith("/")) {
                // URLClassLoader requires that directories end with slashes
                path = path + "/";
            }
            return new URL("file", null, path);
        }
    }
}
