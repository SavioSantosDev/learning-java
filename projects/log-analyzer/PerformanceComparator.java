import java.io.*;

public class PerformanceComparator {
    
    public void compare(LogStatistics ioStats, LogStatistics nioStats) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("           COMPARAÇÃO DE PERFORMANCE");
        System.out.println("=".repeat(60));
        
        System.out.println("\n" + ioStats);
        System.out.println(nioStats);
        
        compareMetrics(ioStats, nioStats);
        
        saveReport(ioStats, nioStats);
    }
    
    private void compareMetrics(LogStatistics ioStats, LogStatistics nioStats) {
        System.out.println("--- Comparação Detalhada ---");
        
        long timeDiff = nioStats.getProcessingTimeMs() - ioStats.getProcessingTimeMs();
        double timePercent = ((double) timeDiff / ioStats.getProcessingTimeMs()) * 100;
        
        if (timeDiff < 0) {
            System.out.printf("⚡ NIO foi %.1f%% mais rápido (%dms vs %dms)\n",
                Math.abs(timePercent), nioStats.getProcessingTimeMs(), ioStats.getProcessingTimeMs());
        } else {
            System.out.printf("⚡ IO foi %.1f%% mais rápido (%dms vs %dms)\n",
                Math.abs(timePercent), ioStats.getProcessingTimeMs(), nioStats.getProcessingTimeMs());
        }
        
        long memDiff = nioStats.getMemoryUsedBytes() - ioStats.getMemoryUsedBytes();
        double memPercent = ioStats.getMemoryUsedBytes() != 0 
            ? ((double) memDiff / ioStats.getMemoryUsedBytes()) * 100 
            : 0;
        
        if (memDiff < 0) {
            System.out.printf("💾 NIO usou %.1f%% menos memória (%.2fMB vs %.2fMB)\n",
                Math.abs(memPercent),
                nioStats.getMemoryUsedBytes() / 1024.0 / 1024.0,
                ioStats.getMemoryUsedBytes() / 1024.0 / 1024.0);
        } else {
            System.out.printf("💾 IO usou %.1f%% menos memória (%.2fMB vs %.2fMB)\n",
                Math.abs(memPercent),
                ioStats.getMemoryUsedBytes() / 1024.0 / 1024.0,
                nioStats.getMemoryUsedBytes() / 1024.0 / 1024.0);
        }
        
        long ioThroughput = ioStats.getProcessingTimeMs() > 0 
            ? ioStats.getTotalLines() * 1000 / ioStats.getProcessingTimeMs() 
            : 0;
        long nioThroughput = nioStats.getProcessingTimeMs() > 0 
            ? nioStats.getTotalLines() * 1000 / nioStats.getProcessingTimeMs() 
            : 0;
        
        System.out.printf("📊 Throughput IO: %d linhas/segundo\n", ioThroughput);
        System.out.printf("📊 Throughput NIO: %d linhas/segundo\n", nioThroughput);
        System.out.println();
    }
    
    private void saveReport(LogStatistics ioStats, LogStatistics nioStats) {
        String reportPath = "logs/performance-report.txt";
        
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(reportPath)))) {
            writer.println("=== RELATÓRIO DE PERFORMANCE ===");
            writer.println("Data: " + java.time.LocalDateTime.now());
            writer.println();
            writer.println(ioStats);
            writer.println(nioStats);
            writer.println("--- Conclusão ---");
            writer.println("Ambas as abordagens (java.io e java.nio) foram testadas.");
            writer.println("NIO oferece API mais moderna e integração com Stream API.");
            writer.println("IO oferece controle mais granular e menor overhead em alguns casos.");
            
            System.out.println("✓ Relatório salvo em: " + reportPath);
        } catch (IOException e) {
            System.err.println("Erro ao salvar relatório: " + e.getMessage());
        }
    }
}
