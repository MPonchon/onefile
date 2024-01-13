package org.yellowreindeer;

//import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void teste_main() throws  IOException {
        String[] args = new String[5];

        String mainClass = "Main";
        String cheminDuDossier = "/home/marc/mesprogs/codingGame/java/onefile/src/main/java";
        String excludeImports = "org.yellowreindeer";
        String cheminCible =  "/home/marc/mesprogs/codingGame/java/onefile/dist";
        String excludeLines = "";
        args[0] = mainClass;
        args[1] = cheminDuDossier;
        args[2] = excludeImports;
        args[3] = cheminCible;
        args[4] = excludeLines;

        Main.main(args);

    }

    @Test
    void getPomVersion_should_return_verion_in_pom() {
        String expected = "1.0.3";
        assertEquals(expected, Main.getPomVersion());
    }
}