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

import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;

public class FunctionExpression extends AbstractExpression {

    private FunctionDescriptor descriptor;

    public FunctionExpression(FunctionDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public Position getPosition() {
        return this.descriptor.getPosition();
    }

    public FunctionDescriptor getDescriptor() {
        return this.descriptor;
    }

    public int getSizeMetric() {
        return this.descriptor.getSizeMetric() + 15;
    }

    @Override
    public Object interpret(ExecutionContext context, boolean debug) {
        JSFunction compiledFn = ((ExecutionContext) context).getCompiler().compileFunction((ExecutionContext) context,
                getDescriptor().getIdentifier(),
                getDescriptor().getFormalParameterNames(),
                getDescriptor().getBlock(),
                getDescriptor().isStrict() || context.isStrict());
        compiledFn.setSource( context.getSource() );
        return(compiledFn);
    }

    public String dump(String indent) {
        return super.dump(indent) + this.descriptor.getBlock().dump(indent + "  ");
    }

    public String dumpData() {
        return "function(" + this.descriptor.getFormalParametersAsString() + ")";
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("function").append(this.descriptor.getIdentifier() == null ? "" : " " + this.descriptor.getIdentifier()).append("(");
        String[] params = this.descriptor.getFormalParameterNames();
        for (int i = 0; i < params.length; ++i) {
            if (i > 0) {
                buf.append(", ");
            }
            buf.append(params[i]);
        }
        buf.append(") {");
        buf.append(this.descriptor.getBlock().toIndentedString("  "));
        buf.append("}");
        return buf.toString();

    }

    @Override
    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit(context, this, strict);
    }

}
