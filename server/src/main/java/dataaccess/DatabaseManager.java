package dataaccess;

import java.sql.*;
import java.util.Properties;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

/**
 * The `DatabaseManager` class provides methods to manage the database, including creating the database,
 * establishing connections, executing updates, and configuring tables.
 *
 * <p>This class loads database configuration from a `db.properties` file and provides utility methods
 * for interacting with the database. It supports creating the database if it does not exist,
 * configuring tables, and clearing all tables.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * // Create and configure the database
 * DatabaseManager.configureDatabase();
 *
 * // Execute an update statement
 * int generatedId = DatabaseManager.executeUpdate("INSERT INTO user (username, password) VALUES (?, ?)", "user1", "password1");
 *
 * // Clear all tables
 * DatabaseManager.clearAllTables();
 * }
 * </pre>
 */
public class DatabaseManager {
    private static final String DATABASE_NAME;
    private static final String USER;
    private static final String PASSWORD;
    private static final String CONNECTION_URL;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) {
                    throw new Exception("Unable to load db.properties");
                }
                Properties props = new Properties();
                props.load(propStream);
                DATABASE_NAME = props.getProperty("db.name");
                USER = props.getProperty("db.user");
                PASSWORD = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                CONNECTION_URL = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    static void createDatabase() throws DataAccessException {
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    public static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            conn.setCatalog(DATABASE_NAME);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
         * Executes an update statement (such as INSERT, UPDATE, or DELETE) with the specified parameters.
         *
         * @param statement The SQL statement to execute.
         * @param params The parameters to set in the SQL statement.
         * @return The generated key for the inserted row, or 0 if no key was generated.
         * @throws DataAccessException If an error occurs while executing the update.
         */
    public static int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) {
                        ps.setString(i + 1, p);
                    } else if (param instanceof Integer p) {
                        ps.setInt(i + 1, p);
                    } else if (param == null) {
                        ps.setNull(i + 1, NULL);
                    } else {
                        ps.setString(i + 1, param.toString());
                    }
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    /**
     * Creates the database if it does not already exist and configures tables.
     */
    public static void configureDatabase() throws DataAccessException {
        createDatabase();
        try (var conn = getConnection()) {
            String[] createStatements = {
                    """
                    CREATE TABLE IF NOT EXISTS auth (
                      id INT NOT NULL AUTO_INCREMENT,
                      username VARCHAR(256) NOT NULL,
                      authToken VARCHAR(256) NOT NULL,
                      PRIMARY KEY (id),
                      INDEX(authToken),
                      INDEX(username)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
                    """,
                    """
                    CREATE TABLE IF NOT EXISTS user (
                    `id` INT NOT NULL AUTO_INCREMENT,
                    `username` VARCHAR(256) NOT NULL,
                    `password` VARCHAR(256) NOT NULL,
                    `email` VARCHAR(256),
                    PRIMARY KEY (id),
                    INDEX(username),
                    INDEX(password)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
                    """,
                    """
                    CREATE TABLE IF NOT EXISTS game (
                    `gameID` INT NOT NULL AUTO_INCREMENT,
                    `whiteUsername` VARCHAR(256) DEFAULT NULL,
                    `blackUsername` VARCHAR(256) DEFAULT NULL,
                    `gameName` VARCHAR(256),
                    `json` TEXT DEFAULT NULL,
                    PRIMARY KEY (gameID)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
                    """
            };

            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to configure database: " + ex.getMessage());
        }
    }

    public static void clearAllTables() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statements = new String[]{
                    "TRUNCATE TABLE auth",
                    "TRUNCATE TABLE user",
                    "TRUNCATE TABLE game"
            };

            for (var statement : statements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error clearing tables: " + e.getMessage());
        }
    }
}
