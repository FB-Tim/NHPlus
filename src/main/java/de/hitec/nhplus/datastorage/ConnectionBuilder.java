package de.hitec.nhplus.datastorage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import io.github.cdimascio.dotenv.Dotenv;
import org.sqlite.SQLiteConfig;

/**
 * Utility class to manage the creation and closure of a singleton SQLite database connection,
 * including encryption support using SQLCipher.
 */
public class ConnectionBuilder {

    private static final String DB_NAME = "nursingHome.db";
    private static final String URL = "jdbc:sqlite:db/" + DB_NAME;

    private static final Dotenv dotenv = Dotenv.configure()
            .directory("src/main/resources")
            .load();
    public static final String DB_PASSWORD = dotenv.get("SQLCIPHER_KEY");

    private static Connection connection;


    /**
     * Returns the singleton {@link Connection} to the SQLite database. Initializes the connection
     * if it does not already exist. Also configures foreign key constraints and sets the encryption key.
     *
     * @return The SQLite {@link Connection}, or {@code null} if an error occurs.
     */
    synchronized public static Connection getConnection() {
        try {
            if (ConnectionBuilder.connection == null) {
                SQLiteConfig configuration = new SQLiteConfig();
                configuration.enforceForeignKeys(true);
                
                configuration.setPragma(SQLiteConfig.Pragma.KEY, DB_PASSWORD);

                ConnectionBuilder.connection = DriverManager.getConnection(URL, configuration.toProperties());
            }
        } catch (SQLException exception) {
            System.out.println("Verbindung zur Datenbank konnte nicht aufgebaut werden!");
            exception.printStackTrace();
        }
        return ConnectionBuilder.connection;
    }

    /**
     * Closes the current SQLite {@link Connection} if it exists and resets it to {@code null}.
     */
    synchronized public static void closeConnection() {
        try {
            if (ConnectionBuilder.connection != null) {
                ConnectionBuilder.connection.close();
                ConnectionBuilder.connection = null;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
