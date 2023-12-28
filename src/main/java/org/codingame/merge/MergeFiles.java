package org.codingame.merge;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MergeFiles {
    public static String EXT = "Onefile";
    public String pathRoot;
    public String mainClass;
    public String pathToWrite;
    public String excludeImports;


    private List<Path> files;

    public MergeFiles(String mainClass, String path, String excludeImports, String cheminCible) {
        this.mainClass  = mainClass ;
        this.pathRoot = path;
        Path directory = Paths.get(pathRoot);
        pathToWrite = String.valueOf(Paths.get(cheminCible).resolve(this.mainClass + EXT +".java"));
        this.excludeImports = excludeImports;
    }

    public void loadFiles () throws IOException {
        files = LectureRepertoire.getFilePaths(pathRoot, mainClass+EXT);
        List<String> imports = new ArrayList<>();
        List<String> codeLines = new ArrayList<>();
        for(Path path: files) {
            List<String> lines  = LectureRepertoire.readFile(path);
            Map<Boolean, List<String>> depAndSrc = dependenciesAndSourceCode(lines);
            // imports
            // retirer les imports du package mainclass
            // charger le fichier mainclass, trouver tous les packages,
            imports.addAll(depAndSrc.get(true));

            // code
            String regex = "\\s*public\\s+class\\s+(\\w+)\\s+(.*)";
            Pattern patternPublicClass = Pattern.compile(regex);
            List<String> srcLines = depAndSrc.get(false).stream()
                    .filter(s -> !s.isEmpty())
                    .map( s -> {
                        Matcher matcherPublicClass = patternPublicClass.matcher(s);
                        if (matcherPublicClass.matches()) {
                            String className = matcherPublicClass.group(1);
                            String reste = matcherPublicClass.group(2);
                            return "class %s %s".formatted(className, reste);
                        } else {
                            return s;
                        }
                    })
                    .map(s -> s.replaceAll(mainClass,mainClass+EXT))
                    .toList();

            codeLines.addAll(srcLines);
        }
        Set<String> uniqueImport = new HashSet<>(imports);
        imports = new ArrayList<>(uniqueImport);
        Collections.sort(imports);

        // write
        Path path = Paths.get(pathToWrite);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            System.err.println("Erreur lors de la suppression du fichier : " + e.getMessage());
            return;
        }
        LectureRepertoire.writeFile(pathToWrite, imports, true);
        LectureRepertoire.writeFile(pathToWrite, codeLines, false);
    }

    public Map<Boolean, List<String>> dependenciesAndSourceCode(List<String> lines) {
        return lines.stream()
                .filter(s -> !s.contains("package"))
                .filter(s -> isLineKept(s , excludeImports))
                .collect(Collectors.partitioningBy(s -> s.startsWith("import")));
    }



    public static boolean isLineKept(String line, String excludeImports) {
//        System.out.println("line "+ line + " excludeImports " + excludeImports);
        if (excludeImports.isEmpty()) { return true; }
        return !line.contains(excludeImports);
    } 

    // designe la main class ok
    // charge le contenu tous les fichiers en memoire ok
    // enleve les imports ok
    // supprime les imports en doublons ok
    // ecrit les imports : ok
    // supprime l'attribut public de la main class: OK
    // ecris le contenu des fichiers dans les packages et sans les imports
    // fichier interface -> ecrit en 1er

}
