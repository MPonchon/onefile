package org.codingame.merge;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LectureRepertoire {

    public static String CHEMIN = "/home/marc/mesprogs/codingGame/one_file/onefilejava/onefile/src/main/java/";
    public static void main(String[] args) {
        List<Path> files = getFilePaths(CHEMIN, "MergeFilesOnefile");
        System.out.println(files);
    }

    public static List<Path> getFilePaths(String chemin, String mainClassOneFile) {
        List<Path> fileList = null;
        Path repertoirePath = Paths.get(chemin);
        try {
            fileList = Files.walk(repertoirePath, Integer.MAX_VALUE)
                    .filter(Files::isRegularFile)
                    .filter( s -> s.toString().endsWith(".java"))
                    .filter( s -> !s.toString().endsWith(MergeFiles.EXT + ".java"))
                    .toList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileList;
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
