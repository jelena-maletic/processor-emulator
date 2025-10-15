package instructions;

import app.Emulator;
import org.junit.jupiter.api.*;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class InstructionExecutorTest {

    @BeforeEach
    void setUp() {
        Emulator.registers.clear();
        Emulator.programCounter = 0;
        InstructionExecutor.halted = false;
        Emulator.registers.put("R1", 0L);
        Emulator.registers.put("R2", 0L);
        Emulator.registers.put("R3", 0L);
        Emulator.registers.put("R4", 0L);
    }

    // --- TESTS FOR arithmetic operations ---

    @Test
    void testExecuteInstruction_Add() {
        Emulator.registers.put("R1", 10L);
        InstructionExecutor.executeInstruction("ADD R1, 5");
        assertEquals(15L, Emulator.registers.get("R1"));
    }

    @Test
    void testExecuteInstruction_Sub() {
        Emulator.registers.put("R2", 20L);
        InstructionExecutor.executeInstruction("SUB R2, 5");
        assertEquals(15L, Emulator.registers.get("R2"));
    }

    @Test
    void testExecuteInstruction_Mul() {
        Emulator.registers.put("R3", 7L);
        InstructionExecutor.executeInstruction("MUL R3, 3");
        assertEquals(21L, Emulator.registers.get("R3"));
    }

    @Test
    void testExecuteInstruction_Div() {
        Emulator.registers.put("R4", 20L);
        InstructionExecutor.executeInstruction("DIV R4, 4");
        assertEquals(5L, Emulator.registers.get("R4"));
    }

    // --- TESTS FOR bitwise operations ---

    @Test
    void testExecuteInstruction_And() {
        Emulator.registers.put("R2", 111L);
        InstructionExecutor.executeInstruction("AND R2, 100");
        assertEquals(100L, Emulator.registers.get("R2"));
    }

    @Test
    void testExecuteInstruction_Or() {
        Emulator.registers.put("R2", 101L);
        InstructionExecutor.executeInstruction("OR R2, 100");
        assertEquals(101L, Emulator.registers.get("R2"));
    }

    @Test
    void testExecuteInstruction_Xor() {
        Emulator.registers.put("R1", 111L);
        InstructionExecutor.executeInstruction("XOR R1, 101");
        assertEquals(10L, Emulator.registers.get("R1"));
    }

    @Test
    void testExecuteInstruction_Not() {
        Emulator.registers.put("R1", 10L);
        InstructionExecutor.executeInstruction("NOT R1");
        assertEquals(~10L, Emulator.registers.get("R1"));
    }

    // --- TESTS FOR mov ---

    @Test
    void testExecuteInstruction_Mov_RegisterToRegister() {
        Emulator.registers.put("R1", 123L);
        InstructionExecutor.executeInstruction("MOV R2, R1");
        assertEquals(123L, Emulator.registers.get("R2"));
    }

    @Test
    void testExecuteInstruction_Mov_ImmediateToRegister() {
        InstructionExecutor.executeInstruction("MOV R3, 456");
        assertEquals(456L, Emulator.registers.get("R3"));
    }

    // --- TESTS FOR jumps ---

    @Test
    void testExecuteInstruction_Cmp() {
        Emulator.registers.put("R1", 10L);
        InstructionExecutor.executeInstruction("CMP R1, 10");
        assertEquals(0, JumpInstructions.cmpFlag);
    }

    @Test
    void testExecuteInstruction_Jmp() {
        InstructionExecutor.executeInstruction("JMP 1000");
        assertEquals(1000L, Emulator.programCounter);
    }

    @Test
    void testExecuteInstruction_Je_Equal() {
        JumpInstructions.cmpFlag = 0;
        InstructionExecutor.executeInstruction("JE 2000");
        assertEquals(2000L, Emulator.programCounter);
    }

    @Test
    void testExecuteInstruction_Je_NotEqual() {
        JumpInstructions.cmpFlag = 1;
        Emulator.programCounter = 0;
        InstructionExecutor.executeInstruction("JE 2000");
        assertEquals(0L, Emulator.programCounter);
    }

    @Test
    void testExecuteInstruction_Jne_NotEqual() {
        JumpInstructions.cmpFlag = -1;
        InstructionExecutor.executeInstruction("JNE 3000");
        assertEquals(3000L, Emulator.programCounter);
    }

    @Test
    void testExecuteInstruction_Jne_Equal() {
        JumpInstructions.cmpFlag = 0;
        Emulator.programCounter = 0;
        InstructionExecutor.executeInstruction("JNE 3000");
        assertEquals(0L, Emulator.programCounter);
    }

    @Test
    void testExecuteInstruction_Jge_Greater() {
        JumpInstructions.cmpFlag = 1;
        InstructionExecutor.executeInstruction("JGE 4000");
        assertEquals(4000L, Emulator.programCounter);
    }

    @Test
    void testExecuteInstruction_Jge_Equal() {
        JumpInstructions.cmpFlag = 0;
        InstructionExecutor.executeInstruction("JGE 4000");
        assertEquals(4000L, Emulator.programCounter);
    }

    @Test
    void testExecuteInstruction_Jge_Smaller() {
        JumpInstructions.cmpFlag = -1;
        Emulator.programCounter = 0;
        InstructionExecutor.executeInstruction("JGE 4000");
        assertEquals(0L, Emulator.programCounter);
    }

    @Test
    void testExecuteInstruction_Jl_Smaller() {
        JumpInstructions.cmpFlag = -1;
        InstructionExecutor.executeInstruction("JL 5000");
        assertEquals(5000L, Emulator.programCounter);
    }

    @Test
    void testExecuteInstruction_Jl_Equal() {
        JumpInstructions.cmpFlag = 0;
        Emulator.programCounter = 0;
        InstructionExecutor.executeInstruction("JL 5000");
        assertEquals(0L, Emulator.programCounter);
    }

    @Test
    void testExecuteInstruction_Jl_Greater() {
        JumpInstructions.cmpFlag = 4;
        Emulator.programCounter = 0;
        InstructionExecutor.executeInstruction("JL 5000");
        assertEquals(0L, Emulator.programCounter);
    }

    // --- TESTS FOR in i out ---

    @Test
    void testExecuteInstruction_In() {
        String simulatedInput = "A\n";
        InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        try {
            InstructionExecutor.executeInstruction("IN R1");
            assertEquals((long) 'A', Emulator.registers.get("R1"));
        }
        finally {
            System.setIn(originalIn);
        }
    }

    @Test
    void testExecuteInstruction_Out() {
        Emulator.registers.put("R1", (long) 'Z');
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        try {
            InstructionExecutor.executeInstruction("OUT R1");
            String output = outputStream.toString();
            assertTrue(output.contains("Z"));
        } finally {
            System.setOut(originalOut);
        }
    }

    // --- TESTS FOR halt ---

    @Test
    void testExecuteInstruction_Halt() {
        InstructionExecutor.executeInstruction("HALT");
        assertTrue(InstructionExecutor.halted);
    }

    // --- TESTS FOR missing arguments and invalid instruction input---

    @Test
    void testExecuteInstruction_InstructionWithMissingArgs() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            InstructionExecutor.executeInstruction("ADD R1");
        });
    }

    @Test
    void testExecuteInstruction_InstructionWithExtraSpaces() {
        Emulator.registers.put("R1", 10L);
        assertDoesNotThrow(() -> {
            InstructionExecutor.executeInstruction(" ADD   R1 , 5 ");
        });
        assertEquals(15L, Emulator.registers.get("R1"));
    }

    @Test
    void testExecuteInstruction_EmptyInstruction() {
        assertDoesNotThrow(() -> InstructionExecutor.executeInstruction(""));
    }

    @Test
    void testExecuteInstruction_UnknownInstruction() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            InstructionExecutor.executeInstruction("INSTRUCTION R1, R2");
        });
        assertTrue(ex.getMessage().contains("Unknown instruction"));
    }

}