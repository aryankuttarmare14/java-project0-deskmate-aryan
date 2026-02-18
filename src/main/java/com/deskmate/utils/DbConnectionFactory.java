package com.deskmate.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DbConnectionFactory {

    private static final String PROPERTIES_FILE = "db.properties";
    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    // Private constructor to prevent instantiation
    private DbConnectionFactory() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    private static void loadProperties() {
        try (InputStream input =
                     DbConnectionFactory.class.getClassLoader()
                             .getResourceAsStream(PROPERTIES_FILE)) {

            if (input == null) {
                throw new IllegalStateException(PROPERTIES_FILE + " not found in resources folder.");
            }

            PROPERTIES.load(input);

            // Validate required properties
            if (PROPERTIES.getProperty("db.url") == null ||
                PROPERTIES.getProperty("db.username") == null ||
                PROPERTIES.getProperty("db.password") == null) {

                throw new IllegalStateException("Missing required database configuration properties.");
            }

        } catch (Exception e) {
            throw new ExceptionInInitializerError(
                    "Failed to load database configuration: " + e.getMessage());
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    PROPERTIES.getProperty("db.url"),
                    PROPERTIES.getProperty("db.username"),
                    PROPERTIES.getProperty("db.password")
            );
        } catch (SQLException e) {
            throw new RuntimeException("Database connection failed.", e);
        }
    }
}
