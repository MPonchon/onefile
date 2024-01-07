package org.yellowreindeer.merge;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

class LectureRepertoireTest {

    static final String PROJECT_DIR = System.getProperty("user.dir");
    static String CHEMIN = PROJECT_DIR+ "/src/main/java/";

    @Test
    void getFilePaths() {
        List<Path>  p = LectureRepertoire.getFilePaths(CHEMIN, "MergeFiles");
        System.out.println("p " + p);
    }
}