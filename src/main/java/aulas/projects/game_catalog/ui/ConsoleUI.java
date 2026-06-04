package aulas.projects.game_catalog.ui;

import aulas.projects.game_catalog.domain.Game;
import aulas.projects.game_catalog.repository.GameRepository;
import aulas.projects.game_catalog.repository.GenreRepository;
import aulas.projects.game_catalog.repository.PlatformRepository;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Log4j2
public class ConsoleUI {

    private final Scanner scanner = new Scanner(System.in);
    private final GameRepository gameRepository = new GameRepository();
    private final GenreRepository genreRepository = new GenreRepository();
    private final PlatformRepository platformRepository = new PlatformRepository();

    private final GameListView listView = new GameListView();
    private final GameDetailView detailView = new GameDetailView();
    private final GameFormView formView = new GameFormView(scanner, genreRepository, platformRepository);

    private int currentPage = 1;

    public void start() {
        System.out.println("\n  Bem-vindo à Biblioteca de Jogos!");
        showList();
    }

    private void showList() {
        while (true) {
            int total = gameRepository.countAll();
            int totalPages = Math.max(1, (int) Math.ceil((double) total / gameRepository.getPageSize()));

            if (currentPage > totalPages) currentPage = totalPages;
            if (currentPage < 1) currentPage = 1;

            List<Game> games = gameRepository.findAll(currentPage);
            listView.render(games, currentPage, totalPages);

            String input = scanner.nextLine().trim().toUpperCase();

            switch (input) {
                case "0" -> {
                    System.out.println("\n  Até logo!");
                    return;
                }
                case "N" -> {
                    if (currentPage < totalPages) currentPage++;
                    else System.out.println("  Já está na última página.");
                }
                case "P" -> {
                    if (currentPage > 1) currentPage--;
                    else System.out.println("  Já está na primeira página.");
                }
                case "A" -> {
                    handleAdd();
                    currentPage = 1;
                }
                default -> handleNumericInput(input, games);
            }
        }
    }

    private void handleNumericInput(String input, List<Game> games) {
        try {
            int idx = Integer.parseInt(input);
            if (idx >= 1 && idx <= games.size()) {
                handleSelect(games.get(idx - 1).id());
            } else {
                System.out.println("  Opção inválida.");
            }
        } catch (NumberFormatException e) {
            System.out.println("  Opção inválida.");
        }
    }

    private void handleAdd() {
        Game game = formView.showForm(null);
        if (game == null) return;

        Optional<Game> saved = gameRepository.save(game);
        if (saved.isPresent()) {
            System.out.println("\n  ✔ Jogo cadastrado com sucesso!");
        } else {
            System.out.println("\n  ✘ Erro ao salvar o jogo.");
        }
    }

    private void handleSelect(int gameId) {
        Optional<Game> found = gameRepository.findById(gameId);
        if (found.isEmpty()) {
            System.out.println("  Jogo não encontrado.");
            return;
        }

        Game game = found.get();
        showDetail(game);
    }

    private void showDetail(Game game) {
        while (true) {
            detailView.render(game);
            String input = scanner.nextLine().trim().toUpperCase();

            switch (input) {
                case "V" -> { return; }
                case "E" -> {
                    handleEdit(game);
                    Optional<Game> refreshed = gameRepository.findById(game.id());
                    if (refreshed.isPresent()) {
                        game = refreshed.get();
                    } else {
                        return;
                    }
                }
                case "D" -> {
                    if (handleDelete(game)) return;
                }
                default -> System.out.println("  Opção inválida.");
            }
        }
    }

    private void handleEdit(Game existing) {
        Game updated = formView.showForm(existing);
        if (updated == null) return;

        Optional<Game> result = gameRepository.update(updated);
        if (result.isPresent()) {
            System.out.println("\n  ✔ Jogo atualizado com sucesso!");
        } else {
            System.out.println("\n  ✘ Erro ao atualizar o jogo.");
        }
    }

    private boolean handleDelete(Game game) {
        System.out.printf("%n  Tem certeza que deseja deletar \"%s\"? (S/N): ", game.title());
        String confirm = scanner.nextLine().trim();
        if (!confirm.equalsIgnoreCase("S")) {
            System.out.println("  Operação cancelada.");
            return false;
        }

        boolean deleted = gameRepository.delete(game.id());
        if (deleted) {
            System.out.println("\n  ✔ Jogo removido com sucesso!");
            return true;
        } else {
            System.out.println("\n  ✘ Erro ao remover o jogo.");
            return false;
        }
    }
}
