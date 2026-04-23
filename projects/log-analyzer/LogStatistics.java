import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LogStatistics implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String fileName;
    private long totalLines;
    private Map<String, Long> levelCounts;
    private long processingTimeMs;
    private long memoryUsedBytes;
    private String approach;
    
    public LogStatistics(String fileName, String approach) {
        this.fileName = fileName;
        this.approach = approach;
        this.levelCounts = new HashMap<>();
        this.levelCounts.put("DEBUG", 0L);
        this.levelCounts.put("INFO", 0L);
        this.levelCounts.put("WARN", 0L);
        this.levelCounts.put("ERROR", 0L);
    }
    
    public void incrementLevel(String level) {
        levelCounts.put(level, levelCounts.getOrDefault(level, 0L) + 1);
    }
    
    public void setTotalLines(long totalLines) {
        this.totalLines = totalLines;
    }
    
    public void setProcessingTimeMs(long processingTimeMs) {
        this.processingTimeMs = processingTimeMs;
    }
    
    public void setMemoryUsedBytes(long memoryUsedBytes) {
        this.memoryUsedBytes = memoryUsedBytes;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public long getTotalLines() {
        return totalLines;
    }
    
    public Map<String, Long> getLevelCounts() {
        return levelCounts;
    }
    
    public long getProcessingTimeMs() {
        return processingTimeMs;
    }
    
    public long getMemoryUsedBytes() {
        return memoryUsedBytes;
    }
    
    public String getApproach() {
        return approach;
    }
    
    public void saveToFile(String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(this);
            System.out.println("✓ Estatísticas salvas em: " + filePath);
        } catch (IOException e) {
            System.err.println("Erro ao salvar estatísticas: " + e.getMessage());
        }
    }
    
    public static LogStatistics loadFromFile(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (LogStatistics) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar estatísticas: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Estatísticas [").append(approach).append("] ===\n");
        sb.append("Arquivo: ").append(fileName).append("\n");
        sb.append("Total de linhas: ").append(totalLines).append("\n");
        sb.append("DEBUG: ").append(levelCounts.get("DEBUG")).append(" | ");
        sb.append("INFO: ").append(levelCounts.get("INFO")).append(" | ");
        sb.append("WARN: ").append(levelCounts.get("WARN")).append(" | ");
        sb.append("ERROR: ").append(levelCounts.get("ERROR")).append("\n");
        sb.append("Tempo: ").append(processingTimeMs).append("ms | ");
        sb.append("Memória: ").append(String.format("%.2f", memoryUsedBytes / 1024.0 / 1024.0)).append("MB\n");
        return sb.toString();
    }
}
