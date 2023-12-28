package org.yellowreindeer;

import org.yellowreindeer.merge.MergeFiles;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;


public class Main {
    public static String version = "v1.0.0";
    public static void main(String[] args) throws IOException {
        System.out.printf("Onefile " + version);
        System.out.printf("args.length " + args.length);
        if (args.length != 4 ) {
            usage();
            return;
        }
        String mainClass = args[0];
        String cheminDuDossier = args[1];
        String excludeImports = args[2];
        String cheminCible = args[3];
        Path path = FileSystems.getDefault().getPath(cheminDuDossier);
        if (! Files.isDirectory(path)) {
            System.err.println("Erreur: le chemin  sourcen'est pas un dossier: " + cheminDuDossier);
        }
        path = FileSystems.getDefault().getPath(cheminCible);
        if (! Files.isDirectory(path)) {
            System.err.println("Erreur: le chemin cible n'est pas un dossier: " + cheminCible);
        }

        System.out.println("onefile ");
        System.out.println("mainClass " + mainClass);
        System.out.println("cheminDuDossier " + cheminDuDossier);
        System.out.println("excludeImports " + excludeImports);
        System.out.println("cheminCible " + cheminCible);
//        String mainClass = "MergeFiles";
//        String cheminDuDossier = LectureRepertoire.CHEMIN;
//        String mainClass = "Main";
//        String cheminDuDossier = "/home/marc/mesprogs/codingGame/one_file/onefilejava/Fall2023-2/";
        MergeFiles mf = new MergeFiles(mainClass, cheminDuDossier, excludeImports, cheminCible);
        mf.loadFiles();

    }

    public static void usage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Création d'un seul fichier pour tous les fichiers java.");
        sb.append("Arguments obligatoires:");
        sb.append("- nom de la mainclass: exemple MergeFiles");
        sb.append("- chemin absolu du dossier a scanner");
        sb.append("- le nom de l'import a exclure exemple (org.yellowreindeer)");
        sb.append("- chemin absolu du dossier cible");
        System.out.println(sb);
    }
}