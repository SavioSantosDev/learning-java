package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

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
            System.out.println("Last modified: " + LocalDateTime.ofEpochSecond(file.lastModified() / 1000, 0, java.time.ZoneOffset.UTC));
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
        }


    }
}
