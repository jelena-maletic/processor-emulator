package instructions;

import app.Emulator;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class ArithmeticOperationsTest {

    ArithmeticOperations arithmeticOps;

    @BeforeEach
    void setUp() {
        Emulator.registers.clear();
        arithmeticOps = new ArithmeticOperations();
    }

    // --- TESTS FOR add ---

    @Test
    void testAdd_PositiveValue() {
        Emulator.registers.put("R1", 10L);
        arithmeticOps.add("R1", 5L);
        assertEquals(15L, Emulator.registers.get("R1"));
    }

    @Test
    void testAdd_RegisterDoesNotExist() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            arithmeticOps.add("R5", 5L);
        });
        assertTrue(ex.getMessage().contains("Register does not exist"));
    }

    @Test
    void testAdd_NegativeValue() {
        Emulator.registers.put("R2", 10L);
        arithmeticOps.add("R2", -3L);
        assertEquals(7L, Emulator.registers.get("R2"));
    }

    // --- TESTS FOR sub ---

    @Test
    void testSub_PositiveValue() {
        Emulator.registers.put("R3", 20L);
        arithmeticOps.sub("R3", 5L);
        assertEquals(15L, Emulator.registers.get("R3"));
    }

    @Test
    void testSub_RegisterDoesNotExist() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            arithmeticOps.sub("R6", 5L);
        });
        assertTrue(ex.getMessage().contains("Register does not exist"));
    }

    @Test
    void testSub_NegativeValue() {
        Emulator.registers.put("R4", 20L);
        arithmeticOps.sub("R4", -5L);
        assertEquals(25L, Emulator.registers.get("R4"));
    }

    // --- TESTS FOR mul ---

    @Test
    void testMul_PositiveValue() {
        Emulator.registers.put("R3", 4L);
        arithmeticOps.mul("R3", 3L);
        assertEquals(12L, Emulator.registers.get("R3"));
    }

    @Test
    void testMul_RegisterDoesNotExist() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            arithmeticOps.mul("R7", 2L);
        });
        assertTrue(ex.getMessage().contains("Register does not exist"));
    }

    @Test
    void testMul_MultiplyByZero() {
        Emulator.registers.put("R2", 5L);
        arithmeticOps.mul("R2", 0L);
        assertEquals(0L, Emulator.registers.get("R2"));
    }

    @Test
    void testMul_MultiplyByNegative() {
        Emulator.registers.put("R1", 5L);
        arithmeticOps.mul("R1", -2L);
        assertEquals(-10L, Emulator.registers.get("R1"));
    }

    // --- TESTS FOR div ---

    @Test
    void testDiv_PositiveValueWithoutRemainder() {
        Emulator.registers.put("R1", 20L);
        arithmeticOps.div("R1", 5L);
        assertEquals(4L, Emulator.registers.get("R1"));
    }

    @Test
    void testDiv_RegisterDoesNotExist() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            arithmeticOps.div("R10", 3L);
        });
        assertTrue(ex.getMessage().contains("Register does not exist"));
    }

    @Test
    void testDiv_DivisionByZero() {
        Emulator.registers.put("R2", 10L);
        var errContent = new java.io.ByteArrayOutputStream();
        System.setErr(new java.io.PrintStream(errContent));
        arithmeticOps.div("R2", 0L);
        String errOutput = errContent.toString();
        assertTrue(errOutput.contains("Error: Division by zero"));
        assertEquals(10L, Emulator.registers.get("R2"));
        System.setErr(System.err);
    }

    @Test
    void testDiv_DivisionResultIsRoundedDown() {
        Emulator.registers.put("R3", 7L);
        arithmeticOps.div("R3", 2L);
        assertEquals(3L, Emulator.registers.get("R3"));
    }

}