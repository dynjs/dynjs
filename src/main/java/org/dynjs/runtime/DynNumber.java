package org.dynjs.runtime;

import org.dynjs.runtime.primitives.DynPrimitiveNumber;

import static java.lang.Double.NaN;

public class DynNumber extends DynObject {

    public static final DynNumber NAN = new DynNumber(NaN);
    private final double value;

    public DynNumber(DynPrimitiveNumber primitiveNumber) {
        this.value = primitiveNumber.getDoubleValue();
    }

    public DynNumber(double value) {
        this.value = value;
    }

    public DynNumber add(final DynNumber other) {
        return new DynNumber(this.value + other.value);
    }

    public DynNumber sub(final DynNumber other) {
        return new DynNumber(this.value - other.value);
    }

    public DynNumber mul(final DynNumber other) {
        return new DynNumber(this.value * other.value);
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
        return new DynNumber(1);
    }

    private static String removeRightInvalidCharacter(String given, int radix) {
        System.out.println("-->" + given);
        return "";
    }

    public double getValue() {
        return value;
    }

    public static boolean isNaN(final DynNumber number) {
        return Double.isNaN(number.getValue());
    }
}
