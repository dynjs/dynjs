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

import org.dynjs.codegen.DereferencedReference;
import org.dynjs.exception.ThrowException;
import org.dynjs.parser.CodeVisitor;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.linker.DynJSBootstrapper;

public class NewOperatorExpression extends AbstractUnaryOperatorExpression {

    private List<Expression> argExprs;

    private final CallSite ctorGet;
    private final List<CallSite> argGets;
    private final CallSite ctorCall;

    public NewOperatorExpression(final Expression expr) {
        this( expr, new ArrayList<Expression>() );
    }
    
    public NewOperatorExpression(final Expression expr, final List<Expression> argExprs) {
        super(expr, "new" );
        this.ctorGet = DynJSBootstrapper.factory().createGet( expr.getPosition() );
        this.ctorCall = DynJSBootstrapper.factory().createConstruct( expr.getPosition() );
        this.argExprs = argExprs;
        this.argGets = new ArrayList<>();
        for ( Expression each : argExprs ) {
            this.argGets.add( DynJSBootstrapper.factory().createGet( each.getPosition()) );
        }
    }
    
    public List<Expression> getArgumentExpressions() {
        return this.argExprs;
    }
    
    public String toString() {
        return "new " + getExpr();
    }
    
    public String dump(String indent) {
        return super.dump(indent) + "new " + getExpr().dump( indent + "  " );
    }

    @Override
    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit( context, this, strict);
    }

    @Override
    public Object interpret(ExecutionContext context, boolean debug) {
        Object ref = getExpr().interpret( context, debug);
        Object memberExpr = getValue(this.ctorGet, context, ref);
        Object[] args = new Object[getArgumentExpressions().size()];

        int numArgs = getArgumentExpressions().size();

        for ( int i = 0 ; i < numArgs ; ++i ) {
            Expression eachArg = this.argExprs.get(i);
            CallSite eachGet = this.argGets.get(i);
            args[i] = getValue(eachGet, context, eachArg.interpret(context, debug));
        }

        Object ctor = memberExpr;

        if ( ref instanceof Reference && ctor instanceof JSFunction) {
            ctor = new DereferencedReference((Reference) ref, ctor);
        }

        try {
            //return( DynJSBootstrapper.getInvokeHandler().construct(ctor, (ExecutionContext) context, args) );
            return this.ctorCall.getTarget().invoke( ctor, context, args );
        } catch (NoSuchMethodError e) {
            throw new ThrowException((ExecutionContext) context, ((ExecutionContext) context).createTypeError("cannot construct with: " + ref));
        } catch (ThrowException e) {
            throw e;
        } catch (Throwable e) {
            throw new ThrowException((ExecutionContext) context, e);
        }
    }
}
