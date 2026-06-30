package aulas.projects.game_catalog.domain;

import java.util.List;

public record Game(
        int id,
        String title,
        Integer releaseYear,
        String description,
        aulas.projects.game_catalog.domain.Genre genre,
        List<aulas.projects.game_catalog.domain.Platform> platforms
) {
}
