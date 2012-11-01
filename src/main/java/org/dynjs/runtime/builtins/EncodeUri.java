/*
 * Copyright 2012 JBoss Inc
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

package org.dynjs.runtime.builtins;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNonConstructorFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;

public class EncodeUri extends AbstractNonConstructorFunction {

    public EncodeUri(GlobalObject globalObject) {
        super(globalObject, "uri");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        String uri = Types.toString( context, args[0] );
        
        try {
            return URLEncoder.encode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new ThrowException( context, context.createUriError("uable to encode uri '" + uri + "'") );
        }
    }
}