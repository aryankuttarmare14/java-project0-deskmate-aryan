package com.deskmate.util;

import com.deskmate.exception.ValidationException;

public final class ValidationUtil {

    // Private constructor to prevent instantiation
    private ValidationUtil() {
    }

    public static String normalizePhone(String phone) {
        if (phone == null) {
            throw new ValidationException("Phone is required");
        }

        String p = phone.trim().replaceAll("\\s+", "");

        if (!p.matches("\\d{10}")) {
            throw new ValidationException("Phone must be 10 digits");
        }

        return p;
    }

    public static void requireNonBlank(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(field + " is required");
        }
    }
}
