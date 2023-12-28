package org.yellowreindeer.merge;


import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MergeFilesTest {

    static String CHEMIN = "/home/marc/mesprogs/codingGame/one_file/onefilejava/onefile/src/main/java";
    static String CIBLE = "/home/marc/mesprogs/codingGame/one_file/onefilejava/onefile/dist";

    @Test
    void dependenciesAndSourceCode_excludeImport_Empty() throws IOException {

        MergeFiles mf = new MergeFiles("MergeFiles", CHEMIN, "", CIBLE);
        mf.loadFiles();
    }

    @Test
    void dependenciesAndSourceCode_with_excludeImport_not_empty() {
        //Given
        MergeFiles mf = new MergeFiles("MergeFiles", CHEMIN, "org.coding", CIBLE);
        List<String> lines = List.of(
                "import org.coding.toto",
                "package org.coding.mm",
                "class Merge {",
                "import java.util.*"
        );
        // When
        Map<Boolean, List<String>>  map = mf.dependenciesAndSourceCode(lines);
        //Then
        assertEquals(List.of("class Merge {"), map.get(false));
        assertEquals(List.of("import java.util.*"), map.get(true));
    }
    @Test
    void dependenciesAndSourceCode_with_excludeImport_Empty() {
        //Given
        MergeFiles mf = new MergeFiles("MergeFiles", CHEMIN, "", CIBLE);
        List<String> lines = List.of(
                "import org.coding.toto",
                "package org.coding.mm",
                "class Merge {",
                "import java.util.*"
        );
        // When
        Map<Boolean, List<String>>  map = mf.dependenciesAndSourceCode(lines);
        //Then
        assertEquals(List.of("class Merge {"), map.get(false));
        assertEquals(List.of("import org.coding.toto", "import java.util.*"), map.get(true));
    }
}