package org.yellowreindeer.merge;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class LectureRepertoire {

    public static String CHEMIN = "/home/marc/mesprogs/codingGame/java/onefile/src/main/java";
    public static void main(String[] args) {
        List<Path> files = getFilePaths(CHEMIN);
        System.out.println(files);
    }

    public static List<Path> getFilePaths(String chemin) {
        final String[] extentions = new String[]{"java"};
        File directory = new File(chemin);
        return FileUtils.listFiles(directory, extentions , true).stream()
                .filter(File::isFile)
                .map(File::toPath)
                .toList();
    }

    public static List<String> readFile(Path filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        try (Scanner scanner = new Scanner(filePath)) {
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
        }
        return lines;
    }

    public static void writeFile(String filePath, List<String> linesToWrite, boolean create) {
        try {
            Files.write(
                    Path.of(filePath),
                    linesToWrite,
                    create ? StandardOpenOption.CREATE : StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture dans le fichier : " + e.getMessage());
        }
    }
}
