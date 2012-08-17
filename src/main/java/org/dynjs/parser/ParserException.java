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

import org.antlr.runtime.tree.Tree;
import org.dynjs.exception.DynJSException;

public class ParserException extends DynJSException {

    public ParserException() {
        super();
    }

    public ParserException(final Exception e) {
        super(e);
    }

    public ParserException(final String message, final Tree tree) {
        this(message, new Position(tree));
    }

    public ParserException(final String message, final Position position) {
        super(position.toString() + " " + message);
    }
}
