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
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.ExecutionContext;

public class ContinueStatement extends BaseStatement {

    private String target;

    public ContinueStatement(Position position, String target) {
        super(position);
        this.target = target;
    }

    public String getTarget() {
        return this.target;
    }
    
    public int getSizeMetric() {
        return 1;
    }

    @Override
    public Completion interpret(ExecutionContext context, boolean debug) {
        return(Completion.createContinue(getTarget()));
    }

    public String toIndentedString(String indent) {
        return indent + "continue" + (this.target == null ? ";" : this.target + ";");
    }

    @Override
    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit( context, this, strict );
    }
}
