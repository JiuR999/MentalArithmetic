package com.swust.mentalarithmetic.utils;

import androidx.room.TypeConverter;

public class Converters {
    @TypeConverter
    public static String fromTimestamp(Number value) {
        return value == null ? null : value.toString();
    }
    @TypeConverter
    public static Number dateToTimestamp(String value) {
        return value == null ? null : Double.valueOf(value);
    }
}
