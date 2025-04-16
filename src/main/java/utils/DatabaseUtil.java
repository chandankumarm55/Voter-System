package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseUtil {
    private static final String PROPERTIES_FILE = "/config.properties";
    private static String jdbcUrl;
    private static String username;
    private static String password;a

    static {
        try {
            loadProperties();
        } catch (IOException e) {
            System.err.println("Failed to load database properties: " + e.getMessage());
        }
    }

    private static void loadProperties() throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = DatabaseUtil.class.getResourceAsStream(PROPERTIES_FILE);

        if (inputStream != null) {
            properties.load(inputStream);
            jdbcUrl = properties.getProperty("db.url");
            username = properties.getProperty("db.username");
            password = properties.getProperty("db.password");
            inputStream.close();
        } else {
            // Default values if properties file not found
            jdbcUrl = "jdbc:mysql://localhost:3306/voting_system";
            username = "root";
            password = "";
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(jdbcUrl, username, password);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found: " + e.getMessage());
        }
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}