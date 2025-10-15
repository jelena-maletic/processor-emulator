package instructions;

import app.Emulator;
import memory.MemoryManager;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class DataTransferInstructionsTest {

    private DataTransferInstructions dataTransfer;
    private MemoryManager memoryManager;

    @BeforeEach
    void setUp() {
        Emulator.registers.clear();
        dataTransfer = new DataTransferInstructions();
        memoryManager = new MemoryManager();
        MemoryManager.dataMemory.clear();
        MemoryManager.instructionsMemory.clear();
        Emulator.registers.put("R1", 0L);
        Emulator.registers.put("R2", 0L);
        Emulator.registers.put("R3", 0L);
        Emulator.registers.put("R4", 0L);
    }

    @Test
    void testMov_RegisterToRegister() {
        Emulator.registers.put("R1", 42L);
        dataTransfer.mov("R2", "R1");
        assertEquals(42L, Emulator.registers.get("R2"));
    }

    @Test
    void testMov_ConstantToRegister() {
        dataTransfer.mov("R3", "100");
        assertEquals(100L, Emulator.registers.get("R3"));
    }

    @Test
    void testMov_MemoryToRegister() {
        long address = 0x1f4;
        long value = 123456789L;
        memoryManager.writeLong(address, value);
        dataTransfer.mov("R4", "[0x1f4]");
        assertEquals(value, Emulator.registers.get("R4"));
    }

    @Test
    void testMov_RegisterToMemory() {
        Emulator.registers.put("R3", 987654321L);
        long address = 0x1f;
        dataTransfer.mov("[0x1f]", "R3");
        long readBack = memoryManager.readLong(address);
        assertEquals(987654321L, readBack);
    }

    @Test
    void testMov_ConstantToMemory() {
        long address = 0x1f;
        dataTransfer.mov("[0x1f]", "5555");
        long readBack = memoryManager.readLong(address);
        assertEquals(5555L, readBack);
    }

    @Test
    void testMov_RegisterAddressToMemoryAndBack() {
        Emulator.registers.put("R1", 50L);
        Emulator.registers.put("R4", 111L);
        dataTransfer.mov("[R1]", "R4");
        assertEquals(111L, memoryManager.readLong(50L));
        dataTransfer.mov("R4", "[R1]");
        assertEquals(111L, Emulator.registers.get("R4"));
    }

    @Test
    void testMov_InvalidMemoryAddress() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            dataTransfer.mov("[invalidAddress]", "123");
        });
        assertTrue(ex.getMessage().contains("Error while executing MOV instruction"));
    }

    @Test
    void testMov_InvalidRegisterSource() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            dataTransfer.mov("R2", "R6");
        });
        assertTrue(ex.getMessage().contains("Error while executing MOV instruction"));
    }

    @Test
    void testMov_InvalidRegisterDestination() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            dataTransfer.mov("R7", "50");
        });
        assertTrue(ex.getMessage().contains("Error while executing MOV instruction"));
    }

    @Test
    void testMov_MemoryAddressWithInvalidRegister() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            dataTransfer.mov("[R5]", "100");
        });
        assertTrue(ex.getMessage().contains("Error while executing MOV instruction"));
    }

    @Test
    void testMov_SourceMemoryAddressWithInvalidRegister() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            dataTransfer.mov("R3", "[RX]");
        });
        assertTrue(ex.getMessage().contains("Error while executing MOV instruction"));
    }

    @Test
    void testMov_EmptySource() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            dataTransfer.mov("R1", "");
        });
        assertTrue(ex.getMessage().contains("Error while executing MOV instruction"));
    }

    @Test
    void testMov_EmptyDestination() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            dataTransfer.mov("", "100");
        });
        assertTrue(ex.getMessage().contains("Error while executing MOV instruction"));
    }

}