package de.hitec.nhplus.utils;

import de.hitec.nhplus.datastorage.ConnectionBuilder;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class to handle encryption of the SQLite database.
 * <p>
 * This class attempts to encrypt the database by applying a key using PRAGMA statements.
 * If the database is already encrypted, it detects this and informs the user.
 * </p>
 */
public class EncryptDB {

    /**
     * Main entry point of the utility.
     * <p>
     * When run, this method attempts to encrypt the database if it is not already encrypted.
     * </p>
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        encryptDatabaseIfNeeded();
    }

    /**
     * Attempts to encrypt the SQLite database if it is not already encrypted.
     * <p>
     * The method first tries to open the database without a key.
     * If successful, it sets a new encryption key (rekey) to encrypt the database.
     * If an error occurs during the query, it assumes the database is already encrypted.
     * </p>
     */
    private static void encryptDatabaseIfNeeded() {
        Connection connection = ConnectionBuilder.getConnection();

        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA key = '';");

            try {
                statement.executeQuery("SELECT count(*) FROM sqlite_master;");

                statement.execute("PRAGMA rekey = '" + ConnectionBuilder.DB_PASSWORD + "';");
                System.out.println("✔ Datenbank wurde verschlüsselt.");
            } catch (SQLException e) {
                System.out.println("Datenbank scheint bereits verschlüsselt zu sein.");
            }
        } catch (SQLException e) {
            System.out.println("Fehler beim Verschlüsseln der Datenbank.");
            e.printStackTrace();
        }
    }
}
