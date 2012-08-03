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
package org.dynjs.runtime.extensions;

import org.dynjs.runtime.DynThreadContext;
import org.dynjs.runtime.RT;
import org.dynjs.runtime.linker.anno.CompanionFor;

@CompanionFor(Object.class)
public class ObjectOperations {

	public static Boolean eq(Object o1, Object o2) {
		if (RT.allArgsAreSameType(o1, o2)) {
			if (DynThreadContext.UNDEFINED == o1 || DynThreadContext.NULL == o1) {
				return true;
			}

			if (o1 instanceof Double) {
				Double n1 = (Double) o1;
				Double n2 = (Double) o2;
				if (n1.equals(Double.NaN) || n2.equals(Double.NaN == n2)) {
					return false;
				}

				if ((n1 == +0.0 && n2 == -0.0) || (n1 == -0.0 && n2 == +0.0)) {
					return true;
				}
			}
		}

		if ((DynThreadContext.NULL == o1 && DynThreadContext.UNDEFINED == o2)
				|| (DynThreadContext.UNDEFINED == o1 && DynThreadContext.NULL == o2)) {
			return true;
		}

		if (o1 instanceof Double && o2 instanceof String) {
			return o1.equals(Double.parseDouble((String) o2));
		}

		if (o1 instanceof String && o2 instanceof Double) {
			return o2.equals(Double.parseDouble((String) o1));
		}

		if (o1 instanceof Boolean) {
			o1 = ((Boolean) o1) ? 1.0 : 0.0;
		}

		if (o2 instanceof Boolean) {
			o2 = ((Boolean) o2) ? 1.0 : 0.0;
		}

		// missing #8 and #9 from http://es5.github.com/#x11.9

		return o1.equals(o2);
	}

	public static Boolean strict_eq(Object o1, Object o2) {
		return o1.equals(o2);
	}
}
