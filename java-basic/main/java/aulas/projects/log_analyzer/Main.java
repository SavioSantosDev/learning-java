package aulas.projects.log_analyzer;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=".repeat(60));
        System.out.println("    ANALISADOR DE LOGS - java.io vs java.nio");
        System.out.println("=".repeat(60));
        System.out.println();
        
        aulas.projects.log_analyzer.LogGenerator generator = new aulas.projects.log_analyzer.LogGenerator();
        generator.generate();
        
        String logFile = "logs/app-" + java.time.LocalDateTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".log";
        
        System.out.println("\n=== COMPARAÇÃO JUSTA COM THREADS ISOLADAS ===");
        System.out.println("Executando múltiplas rodadas para evitar cache do SO...\n");
        
        aulas.projects.log_analyzer.LogStatistics ioStats = executarComThread("java.io", logFile, true);
        aulas.projects.log_analyzer.LogStatistics nioStats = executarComThread("java.nio", logFile, false);
        
        aulas.projects.log_analyzer.PerformanceComparator comparator = new aulas.projects.log_analyzer.PerformanceComparator();
        comparator.compare(ioStats, nioStats);
        
        ioStats.saveToFile("logs/io-stats.ser");
        nioStats.saveToFile("logs/nio-stats.ser");
        
        System.out.println("\n=== Demonstrações Adicionais ===");
        
        aulas.projects.log_analyzer.IOLogReader ioReader = new aulas.projects.log_analyzer.IOLogReader();
        aulas.projects.log_analyzer.NIOLogReader nioReader = new aulas.projects.log_analyzer.NIOLogReader();
        
        ioReader.analyzeWithLineNumberReader(logFile);
        ioReader.showLastLines(logFile, 5);
        nioReader.analyzeWithReadAllLines(logFile);
        nioReader.analyzeMultipleFiles("logs");
        nioReader.parallelAnalysis(logFile);
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("✓ Análise completa! Verifique os arquivos em logs/");
        System.out.println("=".repeat(60));
    }
    
    private static aulas.projects.log_analyzer.LogStatistics executarComThread(String approach, String logFile, boolean isIO) throws InterruptedException {
        final int RODADAS = 5;
        final aulas.projects.log_analyzer.LogStatistics[] resultados = new aulas.projects.log_analyzer.LogStatistics[RODADAS];
        
        System.out.println("=== Testando " + approach + " ===");
        System.out.println("Executando " + RODADAS + " rodadas em thread isolada...");
        
        for (int i = 0; i < RODADAS; i++) {
            final int rodada = i;
            
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(100);
                    System.gc();
                    Thread.sleep(100);
                    
                    aulas.projects.log_analyzer.LogStatistics stats;
                    if (isIO) {
                        aulas.projects.log_analyzer.IOLogReader reader = new aulas.projects.log_analyzer.IOLogReader();
                        stats = reader.analyze(logFile);
                    } else {
                        aulas.projects.log_analyzer.NIOLogReader reader = new aulas.projects.log_analyzer.NIOLogReader();
                        stats = reader.analyze(logFile);
                    }
                    
                    resultados[rodada] = stats;
                    System.out.println("  Rodada " + (rodada + 1) + ": " + stats.getProcessingTimeMs() + "ms");
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            
            thread.start();
            thread.join();
            
            Thread.sleep(200);
        }
        
        long somaTempos = 0;
        long menorTempo = Long.MAX_VALUE;
        long maiorTempo = 0;
        
        for (aulas.projects.log_analyzer.LogStatistics stat : resultados) {
            long tempo = stat.getProcessingTimeMs();
            somaTempos += tempo;
            if (tempo < menorTempo) menorTempo = tempo;
            if (tempo > maiorTempo) maiorTempo = tempo;
        }
        
        long tempoMedio = somaTempos / RODADAS;
        
        System.out.println("  ⏱️  Tempo médio: " + tempoMedio + "ms");
        System.out.println("  📊 Menor: " + menorTempo + "ms | Maior: " + maiorTempo + "ms");
        System.out.println();
        
        aulas.projects.log_analyzer.LogStatistics melhorResultado = resultados[0];
        for (aulas.projects.log_analyzer.LogStatistics stat : resultados) {
            if (stat.getProcessingTimeMs() == menorTempo) {
                melhorResultado = stat;
                break;
            }
        }
        
        melhorResultado.setProcessingTimeMs(tempoMedio);
        
        return melhorResultado;
    }
}
