package util;

import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InstructionLoaderTest {
    private static Path tempFile;

    @BeforeAll
    static void setup() throws IOException {
        tempFile = Files.createTempFile("testInstructionLoaderClass", ".asm");
    }

    @AfterAll
    static void cleanup() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    // --- TESTS FOR testLoadInstructions ---

    @Test
    void testLoadInstructions_NormalLinesWithComments() throws IOException {
        String content = """
            MOV R1, 5        ; Set R1 to 5
            ADD R1, 2        ; Add 2
            ; This line is a comment
            SUB R1, 1

            JMP start        ; Jump to start
            start:
            HALT
            """;
        Files.writeString(tempFile, content);

        List<String> instructions = InstructionLoader.loadInstructions(tempFile.toString());

        assertEquals(6, instructions.size());
        assertEquals("MOV R1, 5", instructions.get(0));
        assertEquals("ADD R1, 2", instructions.get(1));
        assertEquals("SUB R1, 1", instructions.get(2));
        assertEquals("JMP start", instructions.get(3));
        assertEquals("start:", instructions.get(4));
        assertEquals("HALT", instructions.get(5));
    }

    @Test
    void testLoadInstructions_EmptyFile() throws IOException {
        Files.writeString(tempFile, "");
        List<String> instructions = InstructionLoader.loadInstructions(tempFile.toString());
        assertTrue(instructions.isEmpty());
    }

    @Test
    void testLoadInstructions_OnlyCommentsAndSpaces() throws IOException {
        String content = """
            ; This is a comment
            ; Second comment

            ; Third comment with  spaces and     tabs
            
            """;
        Files.writeString(tempFile, content);
        List<String> instructions = InstructionLoader.loadInstructions(tempFile.toString());
        assertTrue(instructions.isEmpty());
    }

    @Test
    void testLoadInstructions_WhitespaceHandling() throws IOException {
        String content = "   MOV R1, 5   ; comment\n\tADD R1, 2\n\n  HALT  ";
        Files.writeString(tempFile, content);
        List<String> instructions = InstructionLoader.loadInstructions(tempFile.toString());

        assertEquals(3, instructions.size());
        assertEquals("MOV R1, 5", instructions.get(0));
        assertEquals("ADD R1, 2", instructions.get(1));
        assertEquals("HALT", instructions.get(2));
    }

    @Test
    void testLoadInstructions_FileDoesNotExist() {
        assertThrows(IOException.class, () -> InstructionLoader.loadInstructions("nonexistentFile.asm"));
    }

    @Test
    void testLoadInstructions_FileWithOnlyBlankLines() throws IOException {
        String content = "\n\n   \n\t\n";
        Files.writeString(tempFile, content);
        List<String> instructions = InstructionLoader.loadInstructions(tempFile.toString());
        assertTrue(instructions.isEmpty());
    }

    // --- TESTS FOR removeComment ---

    @Test
    void testRemoveComment_NoComment() {
        String line = "MOV R1, 10";
        String result = InstructionLoader.removeComment(line);
        assertEquals("MOV R1, 10", result);
    }

    @Test
    void testRemoveComment_WithComment() {
        String line = "MOV R1, 10 ; This is comment";
        String result = InstructionLoader.removeComment(line);
        assertEquals("MOV R1, 10 ", result);
    }

    @Test
    void testRemoveComment_CommentOnly() {
        String line = "; This is comment";
        String result = InstructionLoader.removeComment(line);
        assertEquals("", result);
    }


}