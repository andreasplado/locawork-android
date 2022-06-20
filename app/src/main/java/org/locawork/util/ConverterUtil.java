package org.locawork.util;

public class ConverterUtil {
    public static Float convertToFloat(Double doubleValue) {
        return doubleValue == null ? null : doubleValue.floatValue();
    }
}
