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

import org.dynjs.codegen.DereferencedReference;
import org.dynjs.exception.ThrowException;
import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.EnvironmentRecord;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.linker.DynJSBootstrapper;

import java.lang.invoke.CallSite;
import java.util.ArrayList;
import java.util.List;

public class FunctionCallExpression extends AbstractExpression {

    private final Expression memberExpr;
    private final List<Expression> argExprs;

    private final CallSite functionGet;
    private final List<CallSite> argGets;
    private final CallSite functionCall;

    public FunctionCallExpression(Expression memberExpr, List<Expression> argExprs) {
        this.memberExpr = memberExpr;
        this.argExprs = argExprs;
        functionGet = DynJSBootstrapper.factory().createGet( memberExpr.getPosition() );
        this.argGets = new ArrayList<>();
        for ( Expression each : argExprs ) {
            this.argGets.add( DynJSBootstrapper.factory().createGet( each.getPosition() ) );
        }
        this.functionCall = DynJSBootstrapper.factory().createCall( memberExpr.getPosition() );
    }
    
    public Position getPosition() {
        return this.memberExpr.getPosition();
    }
    
    public List<Expression> getArgumentExpressions() {
        return this.argExprs;
    }
    
    public Expression getMemberExpression() {
        return this.memberExpr;
    }
    
    public int getSizeMetric() {
        int size = this.memberExpr.getSizeMetric();
        
        for ( Expression each : argExprs ) {
            size += each.getSizeMetric();
        }
        
        return size + 5;
    }

    @Override
    public Object interpret(ExecutionContext context, boolean debug) {
        Object ref = getMemberExpression().interpret(context, debug);
        Object function = getValue(this.functionGet, context, ref);

        List<Expression> argExprs = getArgumentExpressions();

        Object[] args = new Object[argExprs.size()];

        int numArgs = argExprs.size();
        for ( int i = 0 ; i < numArgs ; ++i ) {
            Expression each = this.argExprs.get(i);
            CallSite eachGet = this.argGets.get(i);
            Object value = getValue(eachGet, context, each.interpret(context, debug));
            //System.err.println( "ARG: " + i + " -> " + each + " // " + value );
            args[i] = value;
        }

        Object thisValue = null;

        if (ref instanceof Reference) {
            if (((Reference) ref).isPropertyReference()) {
                thisValue = ((Reference) ref).getBase();
            } else {
                thisValue = ((EnvironmentRecord) ((Reference) ref).getBase()).implicitThisValue();
            }
        }

        if (thisValue == null) {
            thisValue = Types.UNDEFINED;
        }

        if (ref instanceof Reference) {
            function = new DereferencedReference((Reference) ref, function);
        }

        try {
            //System.err.println( "CALL: "+ function + " // " + thisValue + " :: " + Arrays.asList( args ) );
            return this.functionCall.getTarget().invoke( function, context, thisValue, args);
        } catch (ThrowException e) {
            throw e;
        } catch (NoSuchMethodError e) {
            throw new ThrowException((ExecutionContext) context, ((ExecutionContext) context).createTypeError("not callable: " + function.toString()));
        } catch (Throwable e) {
            throw new ThrowException((ExecutionContext) context, e);
        }
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(this.memberExpr).append("(");
        boolean first = true;
        for (Expression each : this.argExprs) {
            if (!first) {
                buf.append(", ");
            }
            buf.append(each.toString());
            first = false;

        }
        buf.append(")");
        return buf.toString();
    }
    
    public String dump(String indent) {
        StringBuilder buffer = new StringBuilder();
        buffer.append( super.dump( indent ) );
        buffer.append( this.memberExpr.dump( indent + "  " ) );
        for ( Expression arg : argExprs ) {
            buffer.append( arg.dump( indent + "  " ) );
        }
        return buffer.toString();
    }

    @Override
    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit( context, this, strict );
    }
}
