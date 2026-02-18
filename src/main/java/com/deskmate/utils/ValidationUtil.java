package com.deskmate.utils;

import com.exception.ValidationException;

//import com.deskmate.dbps.exception.ValidationException;

public final class ValidationUtil {

    // Prevent instantiation
    private ValidationUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Normalizes and validates a phone number.
     * Removes spaces and ensures it contains exactly 10 digits.
     *
     * @param phone the phone number input
     * @return normalized phone number
     * @throws ValidationException if invalid
     */
    public static String normalizePhone(String phone) {
        if (phone == null) {
            throw new ValidationException("Phone is required");
        }

        String normalized = phone.trim().replaceAll("\\s+", "");

        if (!normalized.matches("\\d{10}")) {
            throw new ValidationException("Phone must be exactly 10 digits");
        }

        return normalized;
    }

    /**
     * Validates that a string is not null or blank.
     *
     * @param value the value to validate
     * @param field the field name (used in error message)
     * @throws ValidationException if blank
     */
    public static void requireNonBlank(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(field + " is required");
        }
    }
}
