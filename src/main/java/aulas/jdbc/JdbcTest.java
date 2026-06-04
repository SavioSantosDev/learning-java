package aulas.jdbc;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

@Log4j2
public class JdbcTest {

    static void main() throws IOException {
        Properties props = loadProperties();

        String url      = props.getProperty("db.url");
        String user     = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            log.info("Conectado ao banco: {}", conn.getMetaData().getDatabaseProductName());

            createTable(conn);
            insertData(conn);
            selectData(conn);

        } catch (SQLException e) {
            log.error("Erro ao conectar: {}", e.getMessage());
        }
    }

    private static void createTable(Connection conn) throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS usuarios (
                    id   SERIAL PRIMARY KEY,
                    nome VARCHAR(100) NOT NULL,
                    email VARCHAR(150) NOT NULL
                )
                """;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            log.info("Tabela 'usuarios' criada (ou já existia).");
        }
    }

    private static void insertData(Connection conn) throws SQLException {
        String sql = "INSERT INTO usuarios (nome, email) VALUES (?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "Alice");
            ps.setString(2, "alice@email.com");
            ps.executeUpdate();

            ps.setString(1, "Bob");
            ps.setString(2, "bob@email.com");
            ps.executeUpdate();

            log.info("Dados inseridos.");
        }
    }

    private static void selectData(Connection conn) throws SQLException {
        String sql = "SELECT id, nome, email FROM usuarios";

        try (Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {

            log.info("--- Usuários ---");
            while (rs.next()) {
                log.info("id={} | nome={} | email={}",
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email"));
            }
        }
    }

    private static Properties loadProperties() throws IOException {
        Properties props = new Properties();
        try (InputStream is = JdbcTest.class
                .getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (is == null) {
                throw new IOException("Arquivo db.properties não encontrado no classpath.");
            }
            props.load(is);
        }
        return props;
    }
}
