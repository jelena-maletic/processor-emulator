package instructions;

import app.Emulator;
import memory.MemoryManager;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class JumpInstructionsTest {

    private JumpInstructions jumpInstructions;
    private MemoryManager memoryManager;

    @BeforeEach
    void setUp() {
        Emulator.programCounter = 0;
        Emulator.labels.clear();
        jumpInstructions = new JumpInstructions();
        memoryManager = new MemoryManager();
        MemoryManager.dataMemory.clear();
    }

    // --- TESTS FOR cmp ---

    @Test
    void testCmp_ImmediateValues_Equal() {
        JumpInstructions.cmpFlag = -1;
        jumpInstructions.cmp("100", "100");
        assertEquals(0, JumpInstructions.cmpFlag);
    }

    @Test
    void testCmp_ImmediateValues_FirstGreater() {
        JumpInstructions.cmpFlag = 0;
        jumpInstructions.cmp("200", "100");
        assertTrue(JumpInstructions.cmpFlag > 0);
    }

    @Test
    void testCmp_ImmediateValues_FirstSmaller() {
        JumpInstructions.cmpFlag = 0;
        jumpInstructions.cmp("100", "200");
        assertTrue(JumpInstructions.cmpFlag < 0);
    }

    @Test
    void testCmp_RegisterAndImmediate() {
        Emulator.registers.put("R1", 50L);
        JumpInstructions.cmpFlag = 0;
        jumpInstructions.cmp("R1", "50");
        assertEquals(0, JumpInstructions.cmpFlag);
    }

    @Test
    void testCmp_MemoryAndImmediate() {
        long address = 0x10;
        long value = 1234L;
        memoryManager.writeLong(address, value);
        jumpInstructions.cmp("[0x10]", "1234");
        assertEquals(0, JumpInstructions.cmpFlag);
    }

    @Test
    void testCmp_BothMemoryOperands() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            jumpInstructions.cmp("[100]", "[200]");
        });
        assertTrue(ex.getMessage().contains("CMP instruction does not allow comparison between two memory locations: "));
    }

    @Test
    void testCmp_RegisterDoesNotExists() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            jumpInstructions.cmp("R9", "50");
        });
        assertTrue(ex.getCause() instanceof IllegalArgumentException);
    }

    // --- TESTS FOR jmp ---

    @Test
    void testJmp_DirectNumber() {
        jumpInstructions.jmp("12345");
        assertEquals(12345L, Emulator.programCounter);
    }

    @Test
    void testJmp_LabelExists() {
        Emulator.labels.put("start", 500);
        jumpInstructions.jmp("start");
        assertEquals(500L, Emulator.programCounter);
    }

    @Test
    void testJmp_LabelDoesNotExist() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            jumpInstructions.jmp("nonexistentLabel");
        });
        assertTrue(ex.getMessage().contains("Unknown jump destination"));
    }

    @Test
    void testJmp_IndirectAddress() {
        long address = 0x100;
        long jumpAddress = 5555L;
        memoryManager.writeLong(address, jumpAddress);
        jumpInstructions.jmp("[0x100]");
        assertEquals(jumpAddress, Emulator.programCounter);
    }

    // --- TESTS FOR je (jump if equal) ---

    @Test
    void testJe_CmpFlagZero_Jumps() {
        JumpInstructions.cmpFlag = 0;
        jumpInstructions.je("1000");
        assertEquals(1000L, Emulator.programCounter);
    }

    @Test
    void testJe_CmpFlagNonZero() {
        JumpInstructions.cmpFlag = 1;
        Emulator.programCounter = 0;
        jumpInstructions.je("1000");
        assertEquals(0L, Emulator.programCounter);
    }

    // --- TESTS FOR jne (jump if not equal) ---

    @Test
    void testJne_CmpFlagNonZero() {
        JumpInstructions.cmpFlag = -1;
        jumpInstructions.jne("2000");
        assertEquals(2000L, Emulator.programCounter);
    }

    @Test
    void testJne_CmpFlagZero() {
        JumpInstructions.cmpFlag = 0;
        Emulator.programCounter = 0;
        jumpInstructions.jne("2000");
        assertEquals(0L, Emulator.programCounter);
    }

    // --- TESTS FOR jge (jump if greater or equal) ---

    @Test
    void testJge_CmpFlagPositive() {
        JumpInstructions.cmpFlag = 1;
        jumpInstructions.jge("3000");
        assertEquals(3000L, Emulator.programCounter);
    }

    @Test
    void testJge_CmpFlagZero() {
        JumpInstructions.cmpFlag = 0;
        jumpInstructions.jge("3000");
        assertEquals(3000L, Emulator.programCounter);
    }

    @Test
    void testJge_CmpFlagNegative() {
        JumpInstructions.cmpFlag = -1;
        Emulator.programCounter = 0;
        jumpInstructions.jge("3000");
        assertEquals(0L, Emulator.programCounter);
    }

    // --- TESTS FOR jl (jump if less) ---

    @Test
    void testJl_CmpFlagNegative() {
        JumpInstructions.cmpFlag = -1;
        jumpInstructions.jl("4000");
        assertEquals(4000L, Emulator.programCounter);
    }

    @Test
    void testJl_CmpFlagZero() {
        JumpInstructions.cmpFlag = 0;
        Emulator.programCounter = 0;
        jumpInstructions.jl("4000");
        assertEquals(0L, Emulator.programCounter);
    }

    @Test
    void testJl_CmpFlagPositive() {
        JumpInstructions.cmpFlag = 10;
        Emulator.programCounter = 0;
        jumpInstructions.jl("4000");
        assertEquals(0L, Emulator.programCounter);
    }

    // --- TESTS FOR resolveValue ---

    @Test
    void testResolveValue_Immediate() {
        assertEquals(42L, jumpInstructions.resolveValue("42"));
    }

    @Test
    void testResolveValue_Register() {
        Emulator.registers.put("R1", 100L);
        assertEquals(100L, jumpInstructions.resolveValue("R1"));
    }

    @Test
    void testResolveValue_Memory() {
        long addr = 0x30;
        memoryManager.writeLong(addr, 5555L);
        assertEquals(5555L, jumpInstructions.resolveValue("[0x30]"));
    }

    @Test
    void testResolveValue_InvalidRegister() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            jumpInstructions.resolveValue("R99");  // register that doesn't exist
        });
        assertTrue(ex.getMessage().contains("Invalid operand"));
    }

    @Test
    void testResolveValue_InvalidImmediate() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            jumpInstructions.resolveValue("12abc");
        });
        assertTrue(ex.getMessage().contains("Invalid operand"));
    }

    @Test
    void testResolveValue_InvalidMemoryAddress() {
        assertThrows(IllegalArgumentException.class, () -> {
            jumpInstructions.resolveValue("[XYZ]");  // XYZ nije validan broj ni registar
        });
    }

    @Test
    void testResolveValue_EmptyMemoryAddress() {
        long addr = 0x9999;
        long value = jumpInstructions.resolveValue("[0x9999]");
        assertEquals(0L, value);
    }

    // --- TESTS FOR resolveDestination ---

    @Test
    void testResolveDestination_DirectNumber() {
        assertEquals(1234L, jumpInstructions.resolveDestination("1234"));
    }

    @Test
    void testResolveDestination_Label() {
        Emulator.labels.put("loopStart", 9999);
        assertEquals(9999L, jumpInstructions.resolveDestination("loopStart"));
    }

    @Test
    void testResolveDestination_Address() {
        long addr = 0x40;
        memoryManager.writeLong(addr, 7777L);
        assertEquals(7777L, jumpInstructions.resolveDestination("[0x40]"));
    }

    @Test
    void testResolveDestination_UnknownLabel() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            jumpInstructions.resolveDestination("unknownLabel");
        });
        assertTrue(ex.getMessage().contains("Unknown jump destination"));
    }

}