package org.codingame.merge;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LectureRepertoireTest {

    static String CHEMIN = "/home/marc/mesprogs/codingGame/one_file/onefilejava/onefile/src/main/java/";

    @Test
    void getFilePaths() {
        List<Path>  p = LectureRepertoire.getFilePaths(CHEMIN, "MergeFilesOneFile");
        System.out.println("p " + p);
    }
}