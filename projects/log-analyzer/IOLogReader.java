import java.io.*;

public class IOLogReader {
    
    public LogStatistics analyze(String filePath) {
        File file = new File(filePath);
        LogStatistics stats = new LogStatistics(file.getName(), "java.io");
        
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.currentTimeMillis();
        
        analyzeWithBufferedReader(file, stats);
        
        long endTime = System.currentTimeMillis();
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        
        stats.setProcessingTimeMs(endTime - startTime);
        stats.setMemoryUsedBytes(memoryAfter - memoryBefore);
        
        return stats;
    }
    
    private void analyzeWithBufferedReader(File file, LogStatistics stats) {
        long lineCount = 0;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineCount++;
                extractLevel(line, stats);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo: " + e.getMessage());
        }
        
        stats.setTotalLines(lineCount);
    }
    
    public void analyzeWithLineNumberReader(String filePath) {
        System.out.println("\n--- Demonstração LineNumberReader ---");
        
        try (LineNumberReader reader = new LineNumberReader(new FileReader(filePath))) {
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null && count < 5) {
                System.out.println("Linha " + reader.getLineNumber() + ": " + line);
                count++;
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo: " + e.getMessage());
        }
    }
    
    public void showLastLines(String filePath, int n) {
        System.out.println("\n--- Últimas " + n + " linhas (RandomAccessFile) ---");
        
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "r")) {
            long fileLength = raf.length();
            long position = fileLength - 1;
            int linesFound = 0;
            StringBuilder sb = new StringBuilder();
            
            while (position >= 0 && linesFound < n) {
                raf.seek(position);
                int c = raf.read();
                
                if (c == '\n' || position == 0) {
                    if (position > 0) {
                        raf.seek(position + 1);
                    } else {
                        raf.seek(0);
                    }
                    
                    String line = raf.readLine();
                    if (line != null && !line.isEmpty()) {
                        sb.insert(0, line + "\n");
                        linesFound++;
                    }
                }
                position--;
            }
            
            System.out.println(sb.toString());
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
