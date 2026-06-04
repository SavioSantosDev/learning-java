package aulas.projects.game_catalog.ui;

import aulas.projects.game_catalog.domain.Game;
import aulas.projects.game_catalog.domain.Genre;
import aulas.projects.game_catalog.domain.Platform;
import aulas.projects.game_catalog.repository.GenreRepository;
import aulas.projects.game_catalog.repository.PlatformRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameFormView {

    private static final String SEPARATOR = "─".repeat(70);

    private final Scanner scanner;
    private final GenreRepository genreRepository;
    private final PlatformRepository platformRepository;

    public GameFormView(Scanner scanner, GenreRepository genreRepository, PlatformRepository platformRepository) {
        this.scanner = scanner;
        this.genreRepository = genreRepository;
        this.platformRepository = platformRepository;
    }

    public Game showForm(Game existing) {
        boolean isEdit = existing != null;
        System.out.println("\n" + SEPARATOR);
        System.out.println(isEdit ? "  EDITAR JOGO" : "  CADASTRAR JOGO");
        System.out.println(SEPARATOR);

        String title = readString(
                "  Título" + hint(isEdit, existing != null ? existing.title() : null) + ": ",
                isEdit ? existing.title() : null,
                false);

        Integer releaseYear = readOptionalInt(
                "  Ano de lançamento" + hint(isEdit, existing != null && existing.releaseYear() != null ? String.valueOf(existing.releaseYear()) : null) + " (deixe em branco para pular): ",
                isEdit ? existing.releaseYear() : null);

        String description = readString(
                "  Descrição" + hint(isEdit, existing != null ? existing.description() : null) + " (deixe em branco para pular): ",
                isEdit ? existing.description() : null,
                true);

        Genre genre = selectGenre(isEdit ? existing.genre() : null);
        if (genre == null) {
            System.out.println("  Operação cancelada.");
            return null;
        }

        List<Platform> platforms = selectPlatforms(isEdit ? existing.platforms() : List.of());
        if (platforms == null) {
            System.out.println("  Operação cancelada.");
            return null;
        }

        System.out.println("\n" + SEPARATOR);
        System.out.println("  Resumo:");
        System.out.printf("    Título      : %s%n", title);
        System.out.printf("    Ano         : %s%n", releaseYear != null ? releaseYear : "—");
        System.out.printf("    Gênero      : %s%n", genre.name());
        System.out.printf("    Plataformas : %s%n", platforms.isEmpty() ? "—" :
                platforms.stream().map(Platform::name).reduce((a, b) -> a + ", " + b).orElse(""));
        System.out.printf("    Descrição   : %s%n", description != null && !description.isBlank() ? description : "—");
        System.out.println(SEPARATOR);
        System.out.print("  Confirmar? (S/N): ");

        String confirm = scanner.nextLine().trim();
        if (!confirm.equalsIgnoreCase("S")) {
            System.out.println("  Operação cancelada.");
            return null;
        }

        int id = isEdit ? existing.id() : 0;
        return new Game(id, title, releaseYear, description, genre, platforms);
    }

    private Genre selectGenre(Genre current) {
        List<Genre> genres = genreRepository.findAll();
        if (genres.isEmpty()) {
            System.out.println("  ERRO: nenhum gênero disponível.");
            return null;
        }

        System.out.println("\n  Escolha o gênero" + (current != null ? " [atual: " + current.name() + "]" : "") + ":");
        for (int i = 0; i < genres.size(); i++) {
            System.out.printf("    [%d] %s%n", i + 1, genres.get(i).name());
        }
        System.out.print("  Opção: ");

        int choice = readInt(1, genres.size());
        if (choice == -1) return null;
        return genres.get(choice - 1);
    }

    private List<Platform> selectPlatforms(List<Platform> current) {
        List<Platform> allPlatforms = platformRepository.findAll();
        if (allPlatforms.isEmpty()) {
            System.out.println("  ERRO: nenhuma plataforma disponível.");
            return null;
        }

        String currentNames = current == null || current.isEmpty() ? "nenhuma" :
                current.stream().map(Platform::name).reduce((a, b) -> a + ", " + b).orElse("");

        System.out.println("\n  Escolha as plataformas [atual: " + currentNames + "]");
        System.out.println("  (informe os números separados por vírgula, ex: 1,3):");
        for (int i = 0; i < allPlatforms.size(); i++) {
            System.out.printf("    [%d] %s%n", i + 1, allPlatforms.get(i).name());
        }
        System.out.print("  Opção: ");

        String input = scanner.nextLine().trim();
        if (input.isBlank()) return List.of();

        List<Platform> selected = new ArrayList<>();
        for (String part : input.split(",")) {
            try {
                int idx = Integer.parseInt(part.trim());
                if (idx >= 1 && idx <= allPlatforms.size()) {
                    selected.add(allPlatforms.get(idx - 1));
                }
            } catch (NumberFormatException ignored) {
            }
        }
        return selected;
    }

    private String readString(String prompt, String defaultValue, boolean allowBlank) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isBlank() && defaultValue != null) return defaultValue;
        if (input.isBlank() && allowBlank) return null;
        while (input.isBlank() && !allowBlank) {
            System.out.print("  Campo obrigatório. Tente novamente: ");
            input = scanner.nextLine().trim();
        }
        return input;
    }

    private Integer readOptionalInt(String prompt, Integer defaultValue) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isBlank()) return defaultValue;
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("  Valor inválido, ignorando.");
            return defaultValue;
        }
    }

    private int readInt(int min, int max) {
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) return value;
                System.out.printf("  Informe um número entre %d e %d: ", min, max);
            } catch (NumberFormatException e) {
                System.out.printf("  Número inválido. Informe entre %d e %d: ", min, max);
            }
        }
    }

    private String hint(boolean isEdit, String value) {
        if (!isEdit || value == null || value.isBlank()) return "";
        return " [" + truncate(value, 25) + "]";
    }

    private String truncate(String value, int max) {
        return value.length() <= max ? value : value.substring(0, max - 1) + "…";
    }
}
