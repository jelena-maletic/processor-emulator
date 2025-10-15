package instructions;

import app.Emulator;
import org.junit.jupiter.api.*;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class IOInstructionsTest {

    private IOInstructions ioInstructions;
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() {
        Emulator.registers.clear();
        Emulator.registers.put("R1", 0L);
        Emulator.registers.put("R2", 65L); // A
        ioInstructions = new IOInstructions();
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void cleanup() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    // --- TESTS FOR in ----

    @Test
    void testIn_ValidAsciiCharacter() {
        System.setIn(new ByteArrayInputStream("A\n".getBytes()));
        ioInstructions.in("R1");
        assertEquals(65L, Emulator.registers.get("R1"));
    }

    @Test
    void testIn_ValidNonAsciiCharacter() {
        System.setIn(new ByteArrayInputStream("Š\n".getBytes()));
        ioInstructions.in("R1");
        assertEquals(((long) 'Š') & 0xFF, Emulator.registers.get("R1"));  // only the last 8 bits
    }

    @Test
    void testIn_RegisterDoesNotExist() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            ioInstructions.in("R9");
        });
        assertEquals("Register does not exist: R9", ex.getMessage());
    }

    // --- TESTS FOR out ---

    @Test
    void testOut_ValidAsciiCharacter() {
        ioInstructions.out("R2");  // R2 = 65 (A)
        assertEquals("A", outContent.toString());
    }

    @Test
    void testOut_OnlyLowestByteIsPrinted() {
        Emulator.registers.put("R1", 0x12345678L); // only 0x78 should be printed
        ioInstructions.out("R1");
        assertEquals(Character.toString((char) 0x78), outContent.toString());
    }

    @Test
    void testOut_RegisterDoesNotExist() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            ioInstructions.out("R9");
        });
        assertEquals("Register does not exist: R9", ex.getMessage());
    }

}