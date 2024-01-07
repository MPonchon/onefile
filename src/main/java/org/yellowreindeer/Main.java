package org.yellowreindeer;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.yellowreindeer.merge.MergeFiles;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;


public class Main {
    String mainClass;
    String cheminDuDossier;
    String excludeImports;
    String cheminCible;

    public static void main(String[] args) throws IOException, XmlPullParserException {
        Main main = new Main();
        if (! main.checkParams(args)) {
            usage();
            return;
        }
        main.run();

    }
    public boolean checkParams(String[] args) {
        if (args.length != 4 ) {
            return false;
        }
        mainClass = args[0];
        cheminDuDossier = args[1];
        excludeImports = args[2];
        cheminCible = args[3];
        Path path = FileSystems.getDefault().getPath(cheminDuDossier);
        if (! Files.isDirectory(path)) {
            System.err.println("Erreur: le chemin  source n'est pas un dossier: " + cheminDuDossier);
            return false;
        }
        path = FileSystems.getDefault().getPath(cheminCible);
        if (! Files.isDirectory(path)) {
            System.err.println("Erreur: le chemin cible n'est pas un dossier: " + cheminCible);
            return false;
        }
        return true;
    }

    public void run() throws IOException, XmlPullParserException {
        System.out.println("Onefile " + getPomVersion());
        showParams();
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

    public void showParams() {
        System.out.println("mainClass " + mainClass);
        System.out.println("cheminDuDossier " + cheminDuDossier);
        System.out.println("excludeImports " + excludeImports);
        System.out.println("cheminCible " + cheminCible);
    }

    public static String getPomVersion() throws IOException, XmlPullParserException {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new FileReader("pom.xml"));
//        System.out.println(model.getId());
//        System.out.println(model.getGroupId());
//        System.out.println(model.getArtifactId());
//        System.out.println(model.getVersion());
        return model.getVersion();
    }
}