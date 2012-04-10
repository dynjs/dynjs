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
         clazz = Class.forName(className);
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
