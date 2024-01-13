package org.yellowreindeer.launch;

import org.yellowreindeer.Main;
import org.yellowreindeer.merge.MergeFiles;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class Launch {
    String mainClass;
    String cheminDuDossier;
    String excludeImports;
    String cheminCible;
    String excludeLines;

    public void run() throws IOException {
        String version = "unknown";
        try {
            version = Main.getPomVersion();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Onefile version: %s".formatted(version));
        showParams();
        MergeFiles mf = new MergeFiles(mainClass, cheminDuDossier, excludeImports, cheminCible, excludeLines);
        mf.loadFiles();
    }

    public boolean checkParams(String[] args) {
        if (args.length != Main.NB_PARAMS) {
            System.err.println("Erreur: il n'y a que "+ args.length + " parametres sur " + Main.NB_PARAMS);
            for(String param: args) {
                System.err.println("- " + param);
            }
            return false;
        }
        mainClass = args[0];
        cheminDuDossier = args[1];
        excludeImports = args[2];
        cheminCible = args[3];
        excludeLines = args[4];
        Path path = FileSystems.getDefault().getPath(cheminDuDossier);
        if (!Files.isDirectory(path)) {
            System.err.println("Erreur: le chemin  source n'est pas un dossier: " + cheminDuDossier);
            return false;
        }
        path = FileSystems.getDefault().getPath(cheminCible);
        if (!Files.isDirectory(path)) {
            System.err.println("Erreur: le chemin cible n'est pas un dossier: " + cheminCible);
            return false;
        }
        return true;
    }

    public void showParams() {
        System.out.println("Params:");
        System.out.println("-----------------------");
        System.out.println("mainClass: " + mainClass);
        System.out.println("cheminDuDossier: " + cheminDuDossier);
        System.out.println("excludeImports: " + excludeImports);
        System.out.println("cheminCible: " + cheminCible);
        System.out.println("excludeLines: " + excludeLines);
        System.out.println("-----------------------");
    }

}
