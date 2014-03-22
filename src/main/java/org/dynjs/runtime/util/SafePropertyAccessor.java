package org.dynjs.runtime.util;

public class SafePropertyAccessor {
    /**
     * An extension over <code>System.getProperty</code> method.
     * Handles security restrictions, and returns the default
     * value if the access to the property is restricted.
     * @param property The system property name.
     * @param defValue The default value.
     * @return The value of the system property,
     *         or the default value.
     */
    public static String getProperty(String property, String defValue) {
        try {
            return System.getProperty(property, defValue);
        } catch (SecurityException se) {
            return defValue;
        }
    }
}
