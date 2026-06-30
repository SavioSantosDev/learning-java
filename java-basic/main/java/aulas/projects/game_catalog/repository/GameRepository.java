package aulas.projects.game_catalog.repository;

import aulas.projects.game_catalog.config.DatabaseConfig;
import aulas.projects.game_catalog.domain.Game;
import aulas.projects.game_catalog.domain.Genre;
import aulas.projects.game_catalog.domain.Platform;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Log4j2
public class GameRepository {

    private static final int PAGE_SIZE = 5;

    public int countAll() {
        String sql = "SELECT COUNT(*) FROM game";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            log.error("Erro ao contar jogos: {}", e.getMessage());
        }
        return 0;
    }

    public List<Game> findAll(int page) {
        String sql = """
                SELECT g.id,
                       g.title,
                       g.release_year,
                       gn.id   AS genre_id,
                       gn.name AS genre_name,
                       STRING_AGG(p.name, ', ' ORDER BY p.name) AS platforms
                  FROM game g
                  JOIN genre gn ON gn.id = g.genre_id
                  LEFT JOIN game_platform gp ON gp.game_id = g.id
                  LEFT JOIN platform p ON p.id = gp.platform_id
                 GROUP BY g.id, g.title, g.release_year, gn.id, gn.name
                 ORDER BY g.title
                 LIMIT ? OFFSET ?
                """;

        List<Game> games = new ArrayList<>();
        int offset = (page - 1) * PAGE_SIZE;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, PAGE_SIZE);
            ps.setInt(2, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Genre genre = new Genre(rs.getInt("genre_id"), rs.getString("genre_name"));
                    String platformsStr = rs.getString("platforms");
                    List<Platform> platforms = parsePlatformString(platformsStr);
                    games.add(new Game(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getObject("release_year", Integer.class),
                            null,
                            genre,
                            platforms));
                }
            }
        } catch (SQLException e) {
            log.error("Erro ao listar jogos: {}", e.getMessage());
        }

        return games;
    }

    private List<Platform> parsePlatformString(String platformsStr) {
        List<Platform> platforms = new ArrayList<>();
        if (platformsStr == null || platformsStr.isBlank()) {
            return platforms;
        }
        for (String name : platformsStr.split(", ")) {
            platforms.add(new Platform(0, name.trim()));
        }
        return platforms;
    }

    public Optional<Game> findById(int id) {
        String sql = """
                SELECT g.id,
                       g.title,
                       g.release_year,
                       g.description,
                       gn.id   AS genre_id,
                       gn.name AS genre_name,
                       p.id    AS platform_id,
                       p.name  AS platform_name
                  FROM game g
                  JOIN genre gn ON gn.id = g.genre_id
                  LEFT JOIN game_platform gp ON gp.game_id = g.id
                  LEFT JOIN platform p ON p.id = gp.platform_id
                 WHERE g.id = ?
                 ORDER BY p.name
                """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return mapToGame(rs);
            }
        } catch (SQLException e) {
            log.error("Erro ao buscar jogo por id {}: {}", id, e.getMessage());
        }

        return Optional.empty();
    }

    public Optional<Game> save(Game game) {
        String insertGame = """
                INSERT INTO game (title, release_year, description, genre_id)
                VALUES (?, ?, ?, ?)
                """;
        String insertPlatform = "INSERT INTO game_platform (game_id, platform_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int gameId;
                try (PreparedStatement ps = conn.prepareStatement(insertGame, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, game.title());
                    setNullableInt(ps, 2, game.releaseYear());
                    ps.setString(3, game.description());
                    ps.setInt(4, game.genre().id());
                    ps.executeUpdate();

                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("Nenhuma chave gerada para o jogo.");
                        }
                        gameId = keys.getInt(1);
                    }
                }

                if (game.platforms() != null && !game.platforms().isEmpty()) {
                    try (PreparedStatement ps = conn.prepareStatement(insertPlatform)) {
                        for (Platform platform : game.platforms()) {
                            ps.setInt(1, gameId);
                            ps.setInt(2, platform.id());
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }
                }

                conn.commit();
                log.info("Jogo '{}' salvo com id={}.", game.title(), gameId);
                return findById(gameId);

            } catch (SQLException e) {
                conn.rollback();
                log.error("Erro ao salvar jogo, rollback realizado: {}", e.getMessage());
                throw e;
            }
        } catch (SQLException e) {
            log.error("Falha na conexão ao salvar jogo: {}", e.getMessage());
        }

        return Optional.empty();
    }

    public Optional<Game> update(Game game) {
        String updateGame = """
                UPDATE game
                   SET title        = ?,
                       release_year = ?,
                       description  = ?,
                       genre_id     = ?
                 WHERE id = ?
                """;
        String deletePlatforms = "DELETE FROM game_platform WHERE game_id = ?";
        String insertPlatform  = "INSERT INTO game_platform (game_id, platform_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement(updateGame)) {
                    ps.setString(1, game.title());
                    setNullableInt(ps, 2, game.releaseYear());
                    ps.setString(3, game.description());
                    ps.setInt(4, game.genre().id());
                    ps.setInt(5, game.id());
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = conn.prepareStatement(deletePlatforms)) {
                    ps.setInt(1, game.id());
                    ps.executeUpdate();
                }

                if (game.platforms() != null && !game.platforms().isEmpty()) {
                    try (PreparedStatement ps = conn.prepareStatement(insertPlatform)) {
                        for (Platform platform : game.platforms()) {
                            ps.setInt(1, game.id());
                            ps.setInt(2, platform.id());
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }
                }

                conn.commit();
                log.info("Jogo id={} atualizado.", game.id());
                return findById(game.id());

            } catch (SQLException e) {
                conn.rollback();
                log.error("Erro ao atualizar jogo, rollback realizado: {}", e.getMessage());
                throw e;
            }
        } catch (SQLException e) {
            log.error("Falha na conexão ao atualizar jogo: {}", e.getMessage());
        }

        return Optional.empty();
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM game WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                log.info("Jogo id={} deletado.", id);
                return true;
            }
        } catch (SQLException e) {
            log.error("Erro ao deletar jogo id={}: {}", id, e.getMessage());
        }

        return false;
    }

    private Optional<Game> mapToGame(ResultSet rs) throws SQLException {
        Map<Integer, Game> map = new LinkedHashMap<>();
        Map<Integer, List<Platform>> platformMap = new LinkedHashMap<>();

        while (rs.next()) {
            int gameId = rs.getInt("id");

            if (!map.containsKey(gameId)) {
                Genre genre = new Genre(rs.getInt("genre_id"), rs.getString("genre_name"));
                map.put(gameId, new Game(
                        gameId,
                        rs.getString("title"),
                        rs.getObject("release_year", Integer.class),
                        rs.getString("description"),
                        genre,
                        new ArrayList<>()));
                platformMap.put(gameId, new ArrayList<>());
            }

            int platformId = rs.getInt("platform_id");
            if (platformId > 0) {
                platformMap.get(gameId).add(new Platform(platformId, rs.getString("platform_name")));
            }
        }

        if (map.isEmpty()) {
            return Optional.empty();
        }

        int gameId = map.keySet().iterator().next();
        Game base = map.get(gameId);
        Game withPlatforms = new Game(
                base.id(), base.title(), base.releaseYear(),
                base.description(), base.genre(), platformMap.get(gameId));

        return Optional.of(withPlatforms);
    }

    private void setNullableInt(PreparedStatement ps, int index, Integer value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.INTEGER);
        } else {
            ps.setInt(index, value);
        }
    }

    public int getPageSize() {
        return PAGE_SIZE;
    }
}
