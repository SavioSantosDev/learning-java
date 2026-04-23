import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.stream.Stream;

public class NIOLogReader {
    
    public LogStatistics analyze(String filePath) {
        Path path = Paths.get(filePath);
        LogStatistics stats = new LogStatistics(path.getFileName().toString(), "java.nio");
        
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.currentTimeMillis();
        
        analyzeWithFilesLines(path, stats);
        
        long endTime = System.currentTimeMillis();
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        
        stats.setProcessingTimeMs(endTime - startTime);
        stats.setMemoryUsedBytes(memoryAfter - memoryBefore);
        
        return stats;
    }
    
    private void analyzeWithFilesLines(Path path, LogStatistics stats) {
        long lineCount = 0;
        
        try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
            lineCount = lines.peek(line -> extractLevel(line, stats)).count();
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo: " + e.getMessage());
        }
        
        stats.setTotalLines(lineCount);
    }
    
    public void analyzeWithReadAllLines(String filePath) {
        System.out.println("\n--- Demonstração Files.readAllLines() ---");
        
        try {
            Path path = Paths.get(filePath);
            var lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            System.out.println("Total de linhas carregadas em memória: " + lines.size());
            System.out.println("Primeiras 3 linhas:");
            for (int i = 0; i < Math.min(3, lines.size()); i++) {
                System.out.println("  " + lines.get(i));
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo: " + e.getMessage());
        }
    }
    
    public void analyzeMultipleFiles(String directoryPath) {
        System.out.println("\n--- Demonstração Files.walk() ---");
        
        try (Stream<Path> paths = Files.walk(Paths.get(directoryPath))) {
            long totalFiles = paths
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".log"))
                .peek(p -> System.out.println("  Encontrado: " + p.getFileName()))
                .count();
            
            System.out.println("Total de arquivos .log encontrados: " + totalFiles);
        } catch (IOException e) {
            System.err.println("Erro ao percorrer diretório: " + e.getMessage());
        }
    }
    
    public void parallelAnalysis(String filePath) {
        System.out.println("\n--- Demonstração Parallel Stream ---");
        
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            long startTime = System.currentTimeMillis();
            
            long errorCount = lines.parallel()
                .filter(line -> line.contains("ERROR"))
                .count();
            
            long endTime = System.currentTimeMillis();
            
            System.out.println("Linhas com ERROR: " + errorCount);
            System.out.println("Tempo (parallel): " + (endTime - startTime) + "ms");
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo: " + e.getMessage());
        }
    }
    
    private void extractLevel(String line, LogStatistics stats) {
        if (line.contains("DEBUG")) {
            stats.incrementLevel("DEBUG");
        } else if (line.contains("INFO")) {
            stats.incrementLevel("INFO");
        } else if (line.contains("WARN")) {
            stats.incrementLevel("WARN");
        } else if (line.contains("ERROR")) {
            stats.incrementLevel("ERROR");
        }
    }
}
