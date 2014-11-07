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
package org.dynjs.parser.ast;

import java.util.ArrayList;
import java.util.List;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.ExecutionContext;

public class FunctionDeclaration extends AbstractStatement {
    public static final List<FunctionDeclaration> EMPTY_LIST = new ArrayList<>();

    private FunctionDescriptor descriptor;

    public FunctionDeclaration(FunctionDescriptor descriptor) {
        this.descriptor = descriptor;
    }
    
    public Position getPosition() {
        return this.descriptor.getPosition();
    }

    public String getIdentifier() {
        return this.descriptor.getIdentifier();
    }

    public String[] getFormalParameters() {
        return this.descriptor.getFormalParameterNames();
    }

    public BlockStatement getBlock() {
        return this.descriptor.getBlock();
    }

    public boolean isStrict() {
        return this.descriptor.isStrict();
    }
    
    public int getSizeMetric() {
        return this.descriptor.getSizeMetric();
    }

    @Override
    public Completion interpret(ExecutionContext context, boolean debug) {
        return Completion.createNormal();
    }

    public String toString() {
        return this.descriptor.toString();
    }
    
    public List<FunctionDeclaration> getFunctionDeclarations() {
        return descriptor.getBlock().getFunctionDeclarations();
    }

    public String dump(String indent) {
        return super.dump(indent) + this.descriptor.getBlock().dump(indent + "  ");
    }

    public String dumpData() {
        return this.getIdentifier() + "(" + this.descriptor.getFormalParametersAsString() + ")";
    }


    public String toIndentedString(String indent) {
        StringBuilder buf = new StringBuilder();
        buf.append(indent).append("function").append(this.descriptor.getIdentifier() == null ? "" : " " + this.descriptor.getIdentifier()).append("(");
        buf.append(this.descriptor.getFormalParametersAsString());
        buf.append(") {\n");
        buf.append(this.descriptor.getBlock().toIndentedString(indent + "  "));
        buf.append("}");
        return buf.toString();
    }

    @Override
    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit(context, this, strict);
    }
}
