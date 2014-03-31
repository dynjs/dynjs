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

public class Instruction {
    // Mutable state to make it easier for jumping around
    private int ipc = -1;

    public int getIPC() {
        return ipc;
    }

    public void setIPC(int ipc) {
        this.ipc = ipc;
    }

    /**
     * This instruction can set or hint or some useful information onto the scope it belongs
     * to.
     *
     * @param scope is where this instruciton lives
     * @return true if scope had information added otherwise return false
     */
    public boolean computeScopeFlags(Scope scope) {
        return false;
    }

    /**
     * Dump out this instruction in a string frield format for debugging purposes.
     */
    public String dump(String indent) {
        return indent + getClass().getSimpleName();
    }
}
