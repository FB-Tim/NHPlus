package de.hitec.nhplus.datastorage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import io.github.cdimascio.dotenv.Dotenv;
import org.sqlite.SQLiteConfig;

public class ConnectionBuilder {

    private static final String DB_NAME = "nursingHome.db";
    private static final String URL = "jdbc:sqlite:db/" + DB_NAME;

    private static final Dotenv dotenv = Dotenv.configure()
            .directory("src/main/resources")
            .load();
    public static final String DB_PASSWORD = dotenv.get("SQLCIPHER_KEY");

    private static Connection connection;

    synchronized public static Connection getConnection() {
        try {
            if (ConnectionBuilder.connection == null) {
                SQLiteConfig configuration = new SQLiteConfig();
                configuration.enforceForeignKeys(true);
                
                configuration.setPragma(SQLiteConfig.Pragma.KEY, DB_PASSWORD);
                
                Connection conn = DriverManager.getConnection(URL, configuration.toProperties());
            }
        } catch (SQLException exception) {
            System.out.println("Verbindung zur Datenbank konnte nicht aufgebaut werden!");
            exception.printStackTrace();
        }
        return ConnectionBuilder.connection;
    }

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
