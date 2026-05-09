package util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Connexion à la base de données MySQL.
 * La configuration est lue depuis src/main/resources/db.properties
 * Pour modifier la base : éditer ce fichier de configuration.
 */
public class DBConnection {

    private static DBConnection instance;
    private Connection connection;

    // Chargement depuis db.properties (géré par Maven dans le classpath)
    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;
    private static final String DRIVER;

    static {
        Properties props = new Properties();
        try (InputStream is = DBConnection.class
                .getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (is == null) {
                throw new RuntimeException("Fichier db.properties introuvable dans le classpath !");
            }
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Erreur de lecture de db.properties : " + e.getMessage(), e);
        }
        URL      = props.getProperty("db.url");
        USER     = props.getProperty("db.user");
        PASSWORD = props.getProperty("db.password");
        DRIVER   = props.getProperty("db.driver");
    }

    private DBConnection() {
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Impossible de se connecter à la base de données : " + e.getMessage(), e);
        }
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
}
