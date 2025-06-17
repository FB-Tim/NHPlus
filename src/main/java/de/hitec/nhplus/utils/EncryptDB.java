package de.hitec.nhplus.utils;

import de.hitec.nhplus.datastorage.ConnectionBuilder;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class EncryptDB {
    public static void main(String[] args) {
        encryptDatabaseIfNeeded();
    }


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
