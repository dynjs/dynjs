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

import java.util.List;

import org.dynjs.parser.Statement;
import org.dynjs.parser.js.Position;

public abstract class AbstractForInStatement extends AbstractIteratingStatement {

    private final Expression rhs;
    private final Statement block;

    public AbstractForInStatement(Position position, final Expression rhs, final Statement block) {
        super( position );
        this.rhs = rhs;
        this.block = block;
    }

    public Expression getRhs() {
        return this.rhs;
    }

    public Statement getBlock() {
        return this.block;
    }
    
    public List<VariableDeclaration> getVariableDeclarations() {
        return this.block.getVariableDeclarations();
    }

    @Override
    public List<FunctionDeclaration> getFunctionDeclarations() {
        return this.block.getFunctionDeclarations();
    }

    public int getSizeMetric() {
        if ( this.rhs != null ) {
            return this.rhs.getSizeMetric() + 7;
        }
        
        return 7;
    }

}
