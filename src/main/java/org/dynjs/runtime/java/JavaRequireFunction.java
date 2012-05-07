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
package org.dynjs.runtime.java;

import org.dynjs.api.Function;
import org.dynjs.api.Scope;
import org.dynjs.runtime.DynThreadContext;

public class JavaRequireFunction implements Function {
   @Override
   public Object call(DynThreadContext context, Object[] args) {
      String className = (String) args[0];
      Class clazz = null;
      try {
         clazz = Class.forName(className, true, context.getClassLoader());
      } catch (ClassNotFoundException e) {
         e.printStackTrace();
      }
      return clazz;
   }

   @Override
   public void setContext(DynThreadContext context) {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   @Override
   public Scope getEnclosingScope() {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   @Override
   public Object resolve(String name) {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   @Override
   public void define(String property, Object value) {
      //To change body of implemented methods use File | Settings | File Templates.
   }
}
