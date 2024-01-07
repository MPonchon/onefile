package org.yellowreindeer.merge;

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

//    public static String REGEX_CLASS = "\\s*public\\s+(class|enum)\\s+(\\w+<*\\w+>*)\\s+(.*)";
    public static String REGEX_CLASS = " *public +(class|enum) +(\\w+<?\\w+,* *\\w*>?)( +.*)";


    private List<Path> files;

    public MergeFiles(String mainClass, String path, String excludeImports, String cheminCible) {
        this.mainClass  = mainClass ;
        this.pathRoot = path;
        Path directory = Paths.get(pathRoot);
        pathToWrite = String.valueOf(Paths.get(cheminCible).resolve(this.mainClass + EXT +".java"));
        this.excludeImports = excludeImports;
    }

    public static String removePublic(String line, Pattern patternPublicClass) {
        Matcher matcherPublicClass = patternPublicClass.matcher(line);
        if (matcherPublicClass.matches()) {
            String classOrEnum = matcherPublicClass.group(1);
            String className = matcherPublicClass.group(2);
            String reste = matcherPublicClass.group(3);
            return "%s %s%s".formatted(classOrEnum, className, reste);
        } else {
            return line;
        }
    }

    public void loadFiles () throws IOException {
        files = LectureRepertoire.getFilePaths(pathRoot, mainClass+EXT);
        List<String> imports = new ArrayList<>();
        List<String> codeLines = new ArrayList<>();
        for(Path path: files) {
            processFile(path, imports, codeLines);
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

    public void processFile(Path path, List<String> imports, List<String> codeLines) throws IOException {
        List<String> lines  = LectureRepertoire.readFile(path);
        Map<Boolean, List<String>> depAndSrc = dependenciesAndSourceCode(lines);
        // imports
        // retirer les imports du package mainclass
        // charger le fichier mainclass, trouver tous les packages,
        imports.addAll(depAndSrc.get(true));
        Pattern patternPublicClass = Pattern.compile(REGEX_CLASS);
        List<String> srcLines = depAndSrc.get(false).stream()
                .filter(s -> !s.isEmpty())
                .map(s -> removePublic(s, patternPublicClass))
                .map(s -> s.replaceAll(mainClass,mainClass+EXT))
                .toList();
        codeLines.addAll(srcLines);
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
