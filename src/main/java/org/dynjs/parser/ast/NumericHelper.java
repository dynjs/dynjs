package org.dynjs.parser.ast;

/**
 * @author Bob McWhirter
 */
public class NumericHelper {

    public static boolean isZero(Number n) {
        return n.doubleValue() == 0.0;
    }

    public static boolean isNegativeZero(Number n) {
        return isZero(n) && isNegative(n);
    }

    public static boolean isPositiveZero(Number n) {
        return isZero(n) && isPositive(n);
    }

    public static boolean isNegative(Number n) {
        return (Double.compare(n.doubleValue(), 0.0) < 0);
    }

    public static boolean isPositive(Number n) {
        return (Double.compare(n.doubleValue(), 0.0) >= 0);
    }

    public static boolean isSameSign(Number n1, Number n2) {
        return (isPositive(n1) && isPositive(n2)) || (isNegative(n1) && isNegative(n2));
    }

    public static boolean isDifferentSign(Number n1, Number n2) {
        return (isPositive(n1) && isNegative(n2)) || (isNegative(n1) && isPositive(n2));
    }

    public static boolean isRepresentableByLong(double n) {
        if (isNegativeZero(n)) {
            return false;
        }
        return (n == (long) n);
    }
}
