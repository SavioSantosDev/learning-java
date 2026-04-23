package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class A05_IO {
    static void main() throws IOException {

        {
            System.out.println("\njava.io.File");

            File file = new File("file.txt");
            System.out.println("Created: " + file.createNewFile());
            System.out.println("Exists: " + file.exists());
            System.out.println("Is file: " + file.isFile());
            System.out.println("Is directory: " + file.isDirectory());
            System.out.println("Absolute path: " + file.getAbsolutePath());
            System.out.println("Length: " + file.length());
            System.out.println("Last modified: "
                    + LocalDateTime.ofEpochSecond(file.lastModified() / 1000, 0, java.time.ZoneOffset.UTC));
            System.out.println("Can read: " + file.canRead());
            System.out.println("Can write: " + file.canWrite());
            System.out.println("Can exec: " + file.canExecute());
            System.out.println("Is Hidden: " + file.isHidden());

            if (file.exists()) {
                System.out.println("Deleting file: " + file.delete());
            }
        }

        {
            System.out.println("\njava.io.FileWriter");

            File file = new File("file.txt");
            FileWriter writer = new FileWriter(file);
            writer.close();
        }

        System.out.println("\n=== COMPARAÇÃO DE PERFORMANCE ===");
        compararPerformance();
    }

    static void compararPerformance() throws IOException {
        System.out.println("Escrevendo 10.000 linhas em cada abordagem...\n");

        metodo1_SomenteFileWriter(1000000);
        metodo2_ComBufferedWriter(1000000);
        metodo3_ComFilesWrite(1000000);

        System.out.println("\n=== CONCLUSÃO ===");
        System.out.println("✓ BufferedWriter é MUITO mais rápido que FileWriter sozinho");
        System.out.println("✓ Files.write() é conveniente e performático");
        System.out.println("✓ NUNCA use FileWriter sem buffer em produção!");
    }

    static void metodo1_SomenteFileWriter(long quantidadeLinhas) throws IOException {
        System.out.println("\n1. Usando SOMENTE FileWriter (SEM buffer)");

        long inicio = System.currentTimeMillis();

        FileWriter writer = new FileWriter("teste-filewriter.txt");
        for (int i = 0; i < quantidadeLinhas; i++) {
            writer.write("Linha " + i + ": Esta é uma linha de teste para demonstrar performance\n");
        }
        writer.close();

        long fim = System.currentTimeMillis();
        long tempo = fim - inicio;

        System.out.println("   ⏱️  Tempo: " + tempo + "ms");
        new File("teste-filewriter.txt").delete();
    }

    static void metodo2_ComBufferedWriter(long quantidadeLinhas) throws IOException {
        System.out.println("\n2. Usando FileWriter + BufferedWriter (COM buffer)");

        long inicio = System.currentTimeMillis();

        BufferedWriter writer = new BufferedWriter(new FileWriter("teste-buffered.txt"));
        for (int i = 0; i < quantidadeLinhas; i++) {
            writer.write("Linha " + i + ": Esta é uma linha de teste para demonstrar performance\n");
        }
        writer.close();

        long fim = System.currentTimeMillis();
        long tempo = fim - inicio;

        System.out.println("   ⏱️  Tempo: " + tempo + "ms");
        new File("teste-buffered.txt").delete();
    }

    static void metodo3_ComFilesWrite(long quantidadeLinhas) throws IOException {
        System.out.println("\n3. Usando Files.write() do java.nio (Moderno)");

        long inicio = System.currentTimeMillis();

        List<String> linhas = new ArrayList<>();
        for (int i = 0; i < quantidadeLinhas; i++) {
            linhas.add("Linha " + i + ": Esta é uma linha de teste para demonstrar performance");
        }

        Path path = Paths.get("teste-nio.txt");
        Files.write(path, linhas, StandardCharsets.UTF_8);

        long fim = System.currentTimeMillis();
        long tempo = fim - inicio;

        System.out.println("   ⏱️  Tempo: " + tempo + "ms");
        Files.delete(path);
    }
}
