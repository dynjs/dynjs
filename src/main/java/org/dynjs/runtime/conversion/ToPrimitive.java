package org.dynjs.runtime.conversion;

import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.DynThreadContext.Null;
import org.dynjs.runtime.DynThreadContext.Undefined;

public class ToPrimitive {
	public static Object toPrimitive(Object o) {
		if (o instanceof Undefined) {
			return toPrimitive((Undefined) o);
		}

		if (o instanceof Null) {
			return toPrimitive((Null) o);
		}

		if (o instanceof Boolean) {
			return toPrimitive((Boolean) o);
		}

		if (o instanceof Double) {
			return toPrimitive((Double) o);
		}

		if (o instanceof String) {
			return toPrimitive((String) o);
		}

		if (o instanceof DynObject) {
			return toPrimitive((DynObject) o);
		}

		return null;
	}

	public static Object toPrimitive(Undefined undefined) {
		return undefined;
	}

	public static Object toPrimitive(Null nil) {
		return nil;
	}

	public static Object toPrimitive(Boolean b) {
		return b;
	}

	public static Object toPrimitive(Double d) {
		return d;
	}

	public static Object toPrimitive(String s) {
		return s;
	}

	public static Object toPrimitive(DynObject o) {
		return o.resolve("DefaultValue");
	}
}
