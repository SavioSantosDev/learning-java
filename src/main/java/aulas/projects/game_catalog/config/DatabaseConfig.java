package aulas.projects.game_catalog.config;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Log4j2
public class DatabaseConfig {

    private static final Properties PROPS = loadProperties();

    private DatabaseConfig() {
    }

    public static Connection getConnection() throws SQLException {
        String url      = PROPS.getProperty("db.url");
        String user     = PROPS.getProperty("db.user");
        String password = PROPS.getProperty("db.password");
        return DriverManager.getConnection(url, user, password);
    }

    public static Properties getProperties() {
        return PROPS;
    }

    private static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream is = DatabaseConfig.class
                .getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (is == null) {
                throw new IllegalStateException("Arquivo db.properties não encontrado no classpath.");
            }
            props.load(is);
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao carregar db.properties", e);
        }
        return props;
    }
}
