package org.yellowreindeer.merge;


import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MergeFilesTest {

    static final String PROJECT_DIR = System.getProperty("user.dir");
    static final String CHEMIN = PROJECT_DIR+ "/src/main/java/";
    static final String CIBLE = PROJECT_DIR + "/dist";

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


    @Test
    void removePublic_enum() {
        //Given
        String regex = MergeFiles.REGEX_CLASS;
        Pattern patternPublicClass = Pattern.compile(regex);
        String line = "public enum Hello {";
        //When
        String result = MergeFiles.removePublic(line, patternPublicClass);
        //Then
        assertEquals("enum Hello {", result);

        //When
        line ="public class Toto<T , R>{";
        result = MergeFiles.removePublic(line, patternPublicClass);
        //Then
        assertEquals("class Toto<T , R>{", result);
    }
}