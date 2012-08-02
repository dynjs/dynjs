/**
 *  Copyright 2012 Douglas Campos, and individual contributors
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

package org.dynjs.parser;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class Position {

    private final int line;
    private final int charPositionInLine;
    private final String fileName;

    public Position(final Tree tree) {
        String tempFileName = null;
        if (tree != null) {
            this.line = tree.getLine();
            this.charPositionInLine = tree.getCharPositionInLine();

            if (tree instanceof CommonTree && ((CommonTree) tree).getToken().getInputStream() != null) {
                tempFileName = ((CommonTree) tree).getToken().getInputStream().getSourceName();
            }

        } else {
            this.line = -1;
            this.charPositionInLine = -1;
        }
        this.fileName = tempFileName;
    }

    @Override
    public String toString() {
        if (fileName == null) {
            return "<" + line + ":" + charPositionInLine + ">";
        }
        return "<" + fileName + " -> " + line + ":" + charPositionInLine + ">";
    }

    public int getLine() {
        return line;
    }
    
    public String getFileName() {
        return this.fileName;
    }
}
