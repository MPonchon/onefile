package org.yellowreindeer.merge;


import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class MergeFilesTest {

    static final String PROJECT_DIR = System.getProperty("user.dir");
    static final String CHEMIN = PROJECT_DIR+ "/src/main/java/";
    static final String CIBLE = PROJECT_DIR + "/dist";

    @Test
    void dependenciesAndSourceCode_excludeImport_Empty() throws IOException {

        MergeFiles mf = new MergeFiles("MergeFiles", CHEMIN, "", CIBLE, "");
        mf.loadFiles();
    }

    @Test
    void dependenciesAndSourceCode_with_excludeImport_not_empty() {
        //Given
        String excludes = "org.coding,org.yellow";
        MergeFiles mf = new MergeFiles("MergeFiles", CHEMIN, excludes, CIBLE, "");
        List<String> lines = List.of(
                "import org.coding.toto",
                "import org.yellow.toto",
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
        MergeFiles mf = new MergeFiles("MergeFiles", CHEMIN, ",", CIBLE, "");
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

    @Test
    void isImportKept_does_not_keep_exclude_import() {
        // given
        Set<String> excludes = Set.of("org.toto", "org.titi");
        String line = "import org.toto.voila";
        //Then
        assertFalse(MergeFiles.isImportKept(line, excludes));

        // given
        line = "import org.titi.voila";
        //Then
        assertFalse(MergeFiles.isImportKept(line, excludes));

        // given
        line = "import org.tata.voila";
        //Then
        assertTrue(MergeFiles.isImportKept(line, excludes));

    }

    @Test
    void isLineKept_does_not_keep_exclude_lines() {
        // given
        Set<String> excludes = Set.of("Logger logger", "logger = new Logger");
        String line = "Logger logger";
        //Then
        assertFalse(MergeFiles.isLineKept(line, excludes));

        // given
        line = "logger";
        //Then
        assertTrue(MergeFiles.isLineKept(line, excludes));

        // given
        line = "logger = new Logger";
        //Then
        assertFalse(MergeFiles.isLineKept(line, excludes));

    }
}