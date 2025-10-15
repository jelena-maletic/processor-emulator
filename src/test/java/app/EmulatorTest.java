package app;

import instructions.InstructionExecutor;
import memory.MemoryManager;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class EmulatorTest {

    @BeforeEach
    void cleanup() {
        Emulator.registers.clear();
        Emulator.programCounter = 0;
        Emulator.labels.clear();
    }

    @Test
    void testInitializeRegisters() {
        Emulator.initializeRegisters();
        assertEquals(4, Emulator.registers.size());
        assertTrue(Emulator.registers.containsKey("R1"));
        assertEquals(0L, Emulator.registers.get("R1"));
        assertEquals(0L, Emulator.registers.get("R2"));
        assertEquals(0L, Emulator.registers.get("R3"));
        assertEquals(0L, Emulator.registers.get("R4"));
    }

    @Test
    void testExecuteProgram_MovAndHalt() {
        Emulator.initializeRegisters();
        InstructionExecutor.halted = false;

        MemoryManager.instructionsMemory.clear();
        MemoryManager.instructionsMemory.add("MOV R1, 123");
        MemoryManager.instructionsMemory.add("HALT");
        MemoryManager.instructionsMemory.add("MOV R2, 456");

        Emulator emulator = new Emulator();
        emulator.executeProgram();

        assertEquals(123L, Emulator.registers.get("R1"));
        assertEquals(0L, Emulator.registers.get("R2"));
        assertTrue(InstructionExecutor.halted);
    }

}