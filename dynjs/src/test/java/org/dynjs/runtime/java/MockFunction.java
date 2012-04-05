package org.dynjs.runtime.java;

import org.dynjs.api.Function;
import org.dynjs.api.Scope;
import org.dynjs.runtime.DynThreadContext;

public class MockFunction implements Function {
   @Override
   public Object call(DynThreadContext context, Object[] arguments) {
      System.out.println("Just printing my function argument: " + arguments[0]);
      return arguments[0];
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
