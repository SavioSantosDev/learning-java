package aulas.projects.game_catalog.domain;

import java.util.List;

public record Game(
        int id,
        String title,
        Integer releaseYear,
        String description,
        Genre genre,
        List<Platform> platforms
) {
}
