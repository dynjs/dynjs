/**
 *  Copyright 2011 dynjs contributors
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
import org.dynjs.api.Scope;

import java.util.Arrays;
import java.util.List;

public class DynFunction extends DynObject implements Scope {

    private DynAtom result;
    private final List<String> arguments;

    public DynFunction(String... arguments) {
        this.arguments = Arrays.asList(arguments);
    }

    public CodeBlock getCodeBlock() {
        return CodeBlock.newCodeBlock();
    }

    protected int getArgumentOffset(String key){
        // list is zero based + context + scope
        return this.arguments.indexOf(key) + 1 + 2;
    }

    @Override
    public Scope getEnclosingScope() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DynAtom resolve(String name) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void define(String property, DynAtom value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
