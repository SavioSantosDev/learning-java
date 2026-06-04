package aulas.projects.game_catalog;

import aulas.projects.game_catalog.migration.FlywayMigration;
import aulas.projects.game_catalog.ui.ConsoleUI;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Main {

    private Main() {}

    static void main() {
        try {
            FlywayMigration.run();
        } catch (Exception e) {
            log.error("Falha ao executar migrações Flyway: {}", e.getMessage(), e);
            System.err.println("Não foi possível inicializar o banco de dados. Verifique a conexão.");
            return;
        }

        new ConsoleUI().start();
    }
}
