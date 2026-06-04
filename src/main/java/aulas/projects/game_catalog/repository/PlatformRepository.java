package aulas.projects.game_catalog.repository;

import aulas.projects.game_catalog.config.DatabaseConfig;
import aulas.projects.game_catalog.domain.Platform;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class PlatformRepository {

    public List<Platform> findAll() {
        String sql = "SELECT id, name FROM platform ORDER BY name";
        List<Platform> platforms = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                platforms.add(new Platform(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            log.error("Erro ao buscar plataformas: {}", e.getMessage());
        }

        return platforms;
    }
}
