package com.medisched.util;

import java.util.regex.Pattern;

public final class ValidationUtils {
    private ValidationUtils() {}

    private static final Pattern NAME_PATTERN = Pattern.compile("^[\\p{L}][\\p{L} \\-'\u2019]{1,49}$");

    public static boolean isValidName(String value) {
        if (value == null) return false;
        String s = value.trim();
        if (s.length() < 2 || s.length() > 50) return false;
        return NAME_PATTERN.matcher(s).matches();
    }

    public static boolean isValidAge(Integer age) {
        if (age == null) return false;
        return age >= 0 && age <= 100;
    }
}
