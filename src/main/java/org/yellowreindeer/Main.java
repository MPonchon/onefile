package org.yellowreindeer;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.yellowreindeer.launch.Launch;

import java.io.FileReader;
import java.io.IOException;


public class Main {

    private final static  String VERSION = "1.0.3";
    public static int NB_PARAMS = 5;

    public static void main(String[] args) throws IOException {
        Launch launch = new Launch();
        if (!launch.checkParams(args)) {
            usage();
            return;
        }
        launch.run();
    }

    public static void usage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Création d'un seul fichier pour tous les fichiers java.").append("\n")
                .append("Arguments obligatoires:").append("\n")
                .append("- nom de la mainclass: exemple MergeFiles").append("\n")
                .append("- chemin absolu du dossier a scanner").append("\n")
                .append("- les noms des imports a exclure exemple (org.yellowreindeer, ...)").append("\n")
                .append("- chemin absolu du dossier cible").append("\n")
                .append("- les parties de lignes a exclure exemple (\"logger.debug, logger = new Logger\")").append("\n");
        System.out.println(sb);
    }

    public static String getPomVersion()  {
        return VERSION;
//        String version = "inconnue";
//        try {
//            MavenXpp3Reader reader = new MavenXpp3Reader();
//            Model model = reader.read(new FileReader("pom.xml"));
////            System.out.println(model.getId());
////            System.out.println(model.getGroupId());
////            System.out.println(model.getArtifactId());
////            System.out.println(model.getVersion());
//            version = model.getVersion();
//        }
//        catch (Exception ex) {
//            System.err.println("Erreur getVersion :" + ex.getMessage());
//        }
//        return version;
    }
}

