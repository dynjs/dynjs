package org.dynjs.runtime.conversion;

import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.DynThreadContext.Null;
import org.dynjs.runtime.DynThreadContext.Undefined;
import static org.dynjs.runtime.conversion.ToPrimitive.toPrimitive;

public class ToNumber {
	public static Double toNumber(Object o) {
		if (o instanceof Undefined) {
			return toNumber((Undefined) o);
		}

		if (o instanceof Null) {
			return toNumber((Null) o);
		}

		if (o instanceof Boolean) {
			return toNumber((Boolean) o);
		}

		if (o instanceof Double) {
			return toNumber((Double) o);
		}

		if (o instanceof String) {
			return toNumber((String) o);
		}

		if (o instanceof DynObject) {
			return toNumber((DynObject) o);
		}

		return null;
	}

	public static Double toNumber(Undefined u) {
		return Double.NaN;
	}

	public static Double toNumber(Null n) {
		return 0.0;
	}

	public static Double toNumber(Boolean b) {
		return b ? 1.0 : 0.0;
	}

	public static Double toNumber(Double d) {
		return d;
	}

	public static Double toNumber(String s) {
		return s.isEmpty() ? 0.0 : Double.parseDouble(s);
	}

	public static Double toNumber(DynObject o) {
		Object primValue = toPrimitive(o);

		return toNumber(primValue);
	}
}
