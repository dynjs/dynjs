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

import java.lang.invoke.CallSite;
import java.util.ArrayList;
import java.util.List;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.*;
import org.dynjs.runtime.builtins.types.BuiltinObject;
import org.dynjs.runtime.linker.DynJSBootstrapper;

public class ObjectLiteralExpression extends BaseExpression {

    private final List<PropertyAssignment> propertyAssignments;
    private final List<CallSite> assignmentGets;

    public ObjectLiteralExpression(Position position, final List<PropertyAssignment> propertyAssignments) {
        super(position);
        this.propertyAssignments = propertyAssignments;
        this.assignmentGets = new ArrayList<>();
        for ( PropertyAssignment each : propertyAssignments ) {
            this.assignmentGets.add(DynJSBootstrapper.factory().createGet( each.getPosition() ) );
        }
    }
    
    public List<PropertyAssignment> getPropertyAssignments() {
        return this.propertyAssignments;
    }
    
    public List<FunctionDeclaration> getFunctionDeclarations() {
        List<FunctionDeclaration> decls = new ArrayList<>();
        for ( PropertyAssignment each : this.propertyAssignments ) {
            if ( each instanceof NamedValue ) {
                decls.addAll ( ((NamedValue) each).getExpr().getFunctionDeclarations() );
            }
        }
        return decls;
    }

    @Override
    public Object interpret(ExecutionContext context, boolean debug) {
        DynObject obj = BuiltinObject.newObject(context);

        int numAssignments = this.propertyAssignments.size();

        //for (PropertyAssignment each : assignments) {
        for ( int i = 0 ; i < numAssignments ; ++i ) {
            PropertyAssignment each = this.propertyAssignments.get(i);
            CallSite eachGet = assignmentGets.get(i);
            Object ref = each.interpret( context, debug );
            String debugName = each.getName();

            if (ref instanceof Reference) {
                debugName = ((Reference) ref).getReferencedName();
            }
            Object value = getValue(eachGet, context, ref);
            Object original = obj.getOwnProperty(context, each.getName());
            if (each.getName().equals("__proto__")) {
                obj.put(context, each.getName(), value, false);
            } else {
                PropertyDescriptor desc = null;
                if (each instanceof PropertyGet) {
                    desc = PropertyDescriptor.newPropertyDescriptorForObjectInitializerGet(original, debugName, (JSFunction) value);
                } else if (each instanceof PropertySet) {
                    desc = PropertyDescriptor.newPropertyDescriptorForObjectInitializerSet(original, debugName, (JSFunction) value);
                } else {
                    desc = PropertyDescriptor.newPropertyDescriptorForObjectInitializer(debugName, value);
                }
                obj.defineOwnProperty(context, each.getName(), desc, false);
            }
        }

        return(obj);
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("{ ");
        boolean first = true;

        for (PropertyAssignment each : this.propertyAssignments) {
            if (!first) {
                buf.append(", ");
            }
            first = false;
            buf.append(each.toString());

        }
        buf.append(" }");

        return buf.toString();
    }
    
    public int getSizeMetric() {
        int size = 0;
        
        for ( PropertyAssignment each : this.propertyAssignments ) {
            size += each.getSizeMetric();
        }
        
        return size + 5;
    }

    @Override
    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit( context, this, strict );
    }
}
