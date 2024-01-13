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
    public String pathRoot;
    public String mainClass;
    public String pathToWrite;
    public Set<String> excludeImports;
    public Set<String> excludeLines;

    public static String REGEX_CLASS = " *public +(class|enum|interface) +(\\w+<?\\w+,* *\\w*>?)( +.*)";


    private List<Path> files;

    public MergeFiles(String mainClass, String path, String excludeImportsIn, String cheminCible, String excludeLinesIn) {
        this.mainClass  = mainClass ;
        this.pathRoot = path;
        pathToWrite = String.valueOf(Paths.get(cheminCible).resolve(this.mainClass  +".java"));
        this.excludeImports = new HashSet<>();
        String[] excludes = excludeImportsIn.split(", *");
        Collections.addAll(this.excludeImports, excludes);
        this.excludeLines = new HashSet<>();
        excludes = excludeLinesIn.split(", *");
        Collections.addAll(this.excludeLines, excludes);

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
        files = LectureRepertoire.getFilePaths(pathRoot);
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
        System.out.println("traitement terminé: " + pathToWrite);
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
                .toList();
        codeLines.addAll(srcLines);
    }

    public Map<Boolean, List<String>> dependenciesAndSourceCode(List<String> lines) {
        return lines.stream()
                .filter(s -> !s.contains("package"))
                .filter(s -> isImportKept(s , excludeImports))
                .filter(s -> isLineKept(s , excludeLines))
                .collect(Collectors.partitioningBy(s -> s.startsWith("import")));
    }

    public static boolean isImportKept(String line, Set<String> excludeImports) {
        if (excludeImports.isEmpty() || !line.startsWith("import")) { return true; }
        for (String excludeImport: excludeImports) {
            if (!excludeImport.isEmpty() && line.contains(excludeImport)) {
                return false;
            }
        }
        return true;
    }
    public static boolean isLineKept(String line, Set<String> excludeLines) {
        if (excludeLines.isEmpty() || line.startsWith("import")) { return true; }
        for (String excludeLine: excludeLines) {
            if (!excludeLine.isEmpty() && line.contains(excludeLine)) {
                return false;
            }
        }
        return true;
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
