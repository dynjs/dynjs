package org.dynjs.runtime;

import java.util.ArrayList;
import java.util.List;

public class ThreadCompilationManager {
    
    private static ThreadLocal<List<DynamicClassLoader>> threadClassLoader = new ThreadLocal<>();
    
    public static DynamicClassLoader currentClassLoader() {
        List<DynamicClassLoader> currentList = threadClassLoader.get();
        if ( currentList == null || currentList.isEmpty() ) {
            return null;
        }
        
        return currentList.get(0);
    }
    
    public static void push(DynamicClassLoader classLoader) {
        List<DynamicClassLoader> currentList = threadClassLoader.get();
        if ( currentList == null ) {
            currentList = new ArrayList<>();
            threadClassLoader.set(currentList);
        }
        currentList.add(classLoader);
    }
    
    public static void pop() {
        List<DynamicClassLoader> currentList = threadClassLoader.get();
        if ( currentList == null ) {
            throw new IllegalStateException( "Cannot pop classloader from empty stack" );
        }
        currentList.remove( currentList.size() - 1 );
    }

}
