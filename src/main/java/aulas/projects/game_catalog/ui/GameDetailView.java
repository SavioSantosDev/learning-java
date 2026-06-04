package aulas.projects.game_catalog.ui;

import aulas.projects.game_catalog.domain.Game;
import aulas.projects.game_catalog.domain.Platform;

import java.util.stream.Collectors;

public class GameDetailView {

    private static final String SEPARATOR = "─".repeat(70);

    public void render(Game game) {
        System.out.println("\n" + SEPARATOR);
        System.out.println("  DETALHES DO JOGO");
        System.out.println(SEPARATOR);
        System.out.printf("  Título       : %s%n", game.title());
        System.out.printf("  Ano          : %s%n", game.releaseYear() != null ? game.releaseYear() : "—");
        System.out.printf("  Gênero       : %s%n", game.genre().name());

        String platforms = game.platforms().stream()
                .map(Platform::name)
                .collect(Collectors.joining(", "));
        System.out.printf("  Plataformas  : %s%n", platforms.isBlank() ? "—" : platforms);

        System.out.printf("  Descrição    : %s%n",
                game.description() != null && !game.description().isBlank() ? game.description() : "—");
        System.out.println(SEPARATOR);
        System.out.println("  [E] Editar");
        System.out.println("  [D] Deletar");
        System.out.println("  [V] Voltar");
        System.out.println(SEPARATOR);
        System.out.print("  Opção: ");
    }
}
