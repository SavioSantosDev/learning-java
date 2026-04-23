import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class LogGenerator {
    private static final String[] MESSAGES = {
            "User login successful",
            "Database connection established",
            "Cache updated",
            "Request processed",
            "Session created",
            "File uploaded",
            "Email sent",
            "Payment processed",
            "Database connection failed",
            "Timeout occurred",
            "Invalid credentials",
            "High memory usage detected",
            "Disk space low",
            "Network error",
            "Service unavailable",
            "Configuration error"
    };

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final Random random = new Random();

    public void generate() {
        System.out.println("=== Gerando logs de teste ===");

        // Cria o diretório logs se não existir
        File logsDir = new File("logs");
        if (!logsDir.exists()) {
            logsDir.mkdir();
            System.out.println("✓ Diretório 'logs/' criado");
        }

        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        generateLogFile("logs/app-" + date + ".log", 2000000);
        generateLogFile("logs/error-" + date + ".log", 1500000);
        generateLogFile("logs/access-" + date + ".log", 1500000);

        System.out.println("✓ Geração de logs concluída: 5000 linhas totais\n");
    }

    private void generateLogFile(String fileName, int lineCount) {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)))) {
            LocalDateTime timestamp = LocalDateTime.now().minusHours(24);

            for (int i = 0; i < lineCount; i++) {
                String level = getRandomLevel();
                String message = getRandomMessage();
                String logLine = String.format("[%s] %s %s - user%d",
                        timestamp.format(FORMATTER),
                        level,
                        message,
                        random.nextInt(1000));

                writer.println(logLine);
                timestamp = timestamp.plusSeconds(random.nextInt(10) + 1);
            }

            System.out.println("  ✓ " + fileName + " - " + lineCount + " linhas");
        } catch (IOException e) {
            System.err.println("Erro ao gerar arquivo " + fileName + ": " + e.getMessage());
        }
    }

    private String getRandomLevel() {
        int rand = random.nextInt(100);
        if (rand < 25)
            return "DEBUG";
        if (rand < 65)
            return "INFO";
        if (rand < 85)
            return "WARN";
        return "ERROR";
    }

    private String getRandomMessage() {
        return MESSAGES[random.nextInt(MESSAGES.length)];
    }
}
