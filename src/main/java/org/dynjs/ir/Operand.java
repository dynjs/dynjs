/*
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dynjs.ir;

import java.util.List;
import java.util.Map;
import org.dynjs.ir.operands.Variable;
import org.dynjs.runtime.ExecutionContext;

public abstract class Operand {

    private final OperandType type;

    protected Operand(OperandType type) {
        this.type = type;
    }

    public OperandType getType() {
        return type;
    }

    public abstract void addUsedVariables(List<Variable> l);

    public Operand getSimplifiedOperand(Map<Operand, Operand> valueMap, boolean force) {
        return this;
    }

    public Object retrieve(ExecutionContext context, Object[] temps) {
        throw new RuntimeException("Operand: " + getClass().getSimpleName() + "should not be retrieved.");
    }
}
