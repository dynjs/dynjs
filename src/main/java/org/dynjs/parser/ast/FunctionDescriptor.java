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
package org.dynjs.parser.ast;

import java.util.List;

import org.dynjs.parser.js.Position;

public class FunctionDescriptor {

    private final Position position;
    private final String identifier;
    private final Parameter[] formalParameters;
    private final BlockStatement block;
    private boolean strict;

    public FunctionDescriptor(Position position, String identifier, List<Parameter> formalParameters, BlockStatement block, boolean strict) {
        this( position, identifier, formalParameters.toArray( new Parameter[ formalParameters.size()] ), block, strict );
    }
    public FunctionDescriptor(Position position, String identifier, Parameter[] formalParameters, BlockStatement block, boolean strict) {
        this.position = position;
        this.identifier = identifier;
        this.formalParameters = formalParameters;
        this.block = block;
        this.strict = strict;
    }
    

    public Position getPosition() {
        return this.position;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String[] getFormalParameterNames() {
        String[] names = new String[ this.formalParameters.length ];
        for ( int i = 0 ; i < names.length; ++i) {
            names[i] = this.formalParameters[i].getIdentifier();
        }
        return names;
    }

    public BlockStatement getBlock() {
        return this.block;
    }
    
    public boolean isStrict() {
        return this.strict;
    }

    public String toString() {
        return "function " + this.identifier + "(...){...}";
    }

}
