package aulas.projects.game_catalog.migration;

import aulas.projects.game_catalog.config.DatabaseConfig;
import lombok.extern.log4j.Log4j2;
import org.flywaydb.core.Flyway;

import java.util.Properties;

@Log4j2
public class FlywayMigration {

    private FlywayMigration() {
    }

    public static void run() {
        Properties props = DatabaseConfig.getProperties();

        Flyway flyway = Flyway.configure()
                .dataSource(
                        props.getProperty("db.url"),
                        props.getProperty("db.user"),
                        props.getProperty("db.password"))
                .locations("classpath:db/migration")
                .load();

        int applied = flyway.migrate().migrationsExecuted;
        if (applied > 0) {
            log.info("Flyway aplicou {} migração(ões).", applied);
        } else {
            log.info("Flyway: banco já está atualizado.");
        }
    }
}
