package instructions;

import app.Emulator;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class BitwiseOperationsTest {

    private final BitwiseOperations bitwiseOps = new BitwiseOperations();

    @BeforeEach
    void setup() {
        Emulator.registers.clear();
        Emulator.registers.put("R1", 12L);
        Emulator.registers.put("R2", 10L);
    }

    // === TESTS FOR and ===

    @Test
    void testAnd_ValidResult() {
        bitwiseOps.and("R1", 0b1010L); // 1100 & 1010 = 1000 (8)
        assertEquals(0b1000L, Emulator.registers.get("R1"));
    }

    @Test
    void testAnd_NonExistingRegister() {
        assertThrows(IllegalArgumentException.class, () -> {
            bitwiseOps.and("R7", 0b1111L);
        });
    }

    // === TESTS FOR or ===

    @Test
    void testOr_ValidResult() {
        bitwiseOps.or("R1", 0b0011L); // 1100 | 0011 = 1111 (15)
        assertEquals(0b1111L, Emulator.registers.get("R1"));
    }

    @Test
    void testOr_NonExistingRegister() {
        assertThrows(IllegalArgumentException.class, () -> {
            bitwiseOps.or("R5", 0b0101L);
        });
    }

    // === TESTS FOR xor ===

    @Test
    void testXor_ValidResult() {
        bitwiseOps.xor("R1", 0b1010L); // 1100 ^ 1010 = 0110 (6)
        assertEquals(0b0110L, Emulator.registers.get("R1"));
    }

    @Test
    void testXor_NonExistingRegister() {
        assertThrows(IllegalArgumentException.class, () -> {
            bitwiseOps.xor("RX", 0b0001L);
        });
    }

    // === TESTS FOR not ===

    @Test
    void testNot_ValidResult() {
        bitwiseOps.not("R2"); // ~1010 = -11
        assertEquals(~0b1010L, Emulator.registers.get("R2"));
    }

    @Test
    void testNot_NonExistingRegister() {
        assertThrows(IllegalArgumentException.class, () -> {
            bitwiseOps.not("R8");
        });
    }

}

