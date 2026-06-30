package aulas.projects.game_catalog.ui;

import aulas.projects.game_catalog.domain.Game;
import aulas.projects.game_catalog.domain.Platform;

import java.util.List;
import java.util.stream.Collectors;

public class GameListView {

    private static final String SEPARATOR = "─".repeat(70);

    public void render(List<Game> games, int currentPage, int totalPages) {
        System.out.println("\n" + SEPARATOR);
        System.out.printf("  BIBLIOTECA DE JOGOS  —  Página %d de %d%n", currentPage, totalPages);
        System.out.println(SEPARATOR);

        if (games.isEmpty()) {
            System.out.println("  Nenhum jogo cadastrado.");
        } else {
            System.out.printf("  %-4s  %-35s  %-12s  %s%n", "Nº", "Título", "Gênero", "Plataformas");
            System.out.println("  " + "─".repeat(66));
            for (int i = 0; i < games.size(); i++) {
                Game g = games.get(i);
                String platforms = g.platforms().stream()
                        .map(Platform::name)
                        .collect(Collectors.joining(", "));
                if (platforms.isBlank()) platforms = "—";
                System.out.printf("  [%-2d] %-35s  %-12s  %s%n",
                        i + 1,
                        truncate(g.title(), 35),
                        truncate(g.genre().name(), 12),
                        truncate(platforms, 30));
            }
        }

        System.out.println(SEPARATOR);
        if (currentPage > 1)          System.out.println("  [P] Página anterior");
        if (currentPage < totalPages) System.out.println("  [N] Próxima página");
        System.out.println("  [A] Adicionar jogo");
        System.out.println("  [número] Selecionar jogo");
        System.out.println("  [0] Sair");
        System.out.println(SEPARATOR);
        System.out.print("  Opção: ");
    }

    private String truncate(String value, int max) {
        if (value == null) return "";
        return value.length() <= max ? value : value.substring(0, max - 1) + "…";
    }
}
