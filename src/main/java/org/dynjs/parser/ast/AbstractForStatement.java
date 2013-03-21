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

public abstract class AbstractForStatement extends AbstractIteratingStatement {

    private final Expression test;
    private final Expression increment;
    private final Statement block;

    public AbstractForStatement(final Position position, final Expression test, final Expression increment, final Statement block) {
        super( position );
        this.test = test;
        this.increment = increment;
        this.block = block;
    }

    public Expression getTest() {
        return this.test;
    }

    public Expression getIncrement() {
        return this.increment;
    }

    public Statement getBlock() {
        return this.block;
    }

    public List<VariableDeclaration> getVariableDeclarations() {
        return this.block.getVariableDeclarations();
    }
    
    public int getSizeMetric()  {
        int size = 0;
        if ( this.test != null ) {
            size += this.test.getSizeMetric();
        }
        
        if ( this.increment != null ) {
            size += this.increment.getSizeMetric();
        }
        
        return size + 7;
        
    }
    

}
