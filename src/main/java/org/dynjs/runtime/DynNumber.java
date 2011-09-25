package org.dynjs.runtime;

import org.dynjs.runtime.primitives.DynPrimitiveNumber;

import static java.lang.Double.NaN;

public class DynNumber extends DynObject {

    public static final DynNumber NAN = new DynNumber(NaN);
    public static final DynNumber POSITIVE_INFINITY = new DynNumber(Double.POSITIVE_INFINITY);
    public static final DynNumber NEGATIVE_INFINITY = new DynNumber(Double.NEGATIVE_INFINITY);

    private final double value;

    public DynNumber(DynPrimitiveNumber primitiveNumber) {
        value = primitiveNumber.getDoubleValue();
    }

    public DynNumber(double value) {
        this.value = value;
    }

    public DynNumber add(final DynNumber other) {
        return new DynNumber(getValue() + other.getValue());
    }

    public DynNumber sub(final DynNumber other) {
        return new DynNumber(this.value - other.value);
    }

    public DynNumber mul(final DynNumber other) {
        return new DynNumber(this.value * other.value);
    }

    public DynNumber div(final DynNumber other) {
        if (other.getValue() == 0) {
            return getValue() == 0 ? NAN : (getValue() < 0 ? NEGATIVE_INFINITY : POSITIVE_INFINITY);
        }
        return new DynNumber(getValue() / other.getValue());
    }

    public DynNumber mod(final DynNumber other) {
        if (other.getValue() == 0) {
            return NAN;
        }
        return new DynNumber(getValue() % other.getValue());
    }

    public static DynNumber parseInt(final DynString string) {
        return parseInt(string, new DynNumber(10));
    }

    public static DynNumber parseInt(final DynString string, final DynNumber radix) {
        String given = string.toString().trim().toLowerCase();

        if (given.equals("")) {
            return NAN;
        }

        char firstChar = given.charAt(0);

        if (((firstChar < '0') || (firstChar > '9')) && ((firstChar != '+') && (firstChar != '-'))) {
            return NAN;
        }

        if (firstChar == '0') {
            if (given.startsWith("0x")) {
                return fromHex(removeRightInvalidCharacter
                        (given.substring(2), 16));
            }
        }

        return new DynNumber(0);
    }

    private static DynNumber fromHex(String value) {
        return new DynNumber(Integer.parseInt(value, 16));
    }

    private static String removeRightInvalidCharacter(String given, int radix) {
        System.out.println("-->" + given);
        return given;
    }

    public double getValue() {
        return value;
    }

    public static boolean isNaN(final DynNumber number) {
        return Double.isNaN(number.getValue());
    }
}
