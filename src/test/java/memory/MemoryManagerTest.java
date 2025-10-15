package memory;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MemoryManagerTest {

    private MemoryManager memory;

    @BeforeEach
    void setUp() {
        memory = new MemoryManager();
        MemoryManager.dataMemory.clear();
        MemoryManager.instructionsMemory.clear();
    }

    // --- TESTS FOR read ---

    @Test
    void testRead_UninitializedAddress() {
        assertEquals(0, memory.read(123L));
    }

    @Test
    void testRead_AfterWrite() {
        memory.write(500L, (byte) 77);
        assertEquals(77, memory.read(500L));
    }

    // --- TESTS FOR write ---

    @Test
    void testWrite_NewAddress() {
        memory.write(300L, (byte) 99);
        assertEquals(99, memory.read(300L));
    }

    @Test
    void testWrite_OverwriteValue() {
        memory.write(100L, (byte) 5);
        memory.write(100L, (byte) 9);
        assertEquals(9, memory.read(100L));
    }

    // --- TESTS FOR readLong ---

    @Test
    void testReadLong_UninitializedAddress() {
        assertEquals(0L, memory.readLong(2025));
    }

    @Test
    void testReadLong_AfterWriteLong_SameBlock() {
        long value = 0x1122334455667788L;
        memory.writeLong(1024, value);
        assertEquals(value, memory.readLong(1024));
    }

    @Test
    void testReadLong_MultipleBlocks() {
        long value = 0x0102030405060708L;
        memory.writeLong(252L, value); // 4 bytes in one block, 4 in the next
        assertEquals(value, memory.readLong(252L));
    }

    // --- TESTS FOR writeLong ---

    @Test
    void testWriteLong_StoresAllBytesCorrectly() {
        long value = 0xAABBCCDDEEFF0011L;
        memory.writeLong(100L, value);
        assertEquals((byte) 0x11, memory.read(100L)); // least significant byte
        assertEquals((byte) 0x00, memory.read(101L));
        assertEquals((byte) 0xFF, memory.read(102L));
        assertEquals((byte) 0xEE, memory.read(103L));
        assertEquals((byte) 0xDD, memory.read(104L));
        assertEquals((byte) 0xCC, memory.read(105L));
        assertEquals((byte) 0xBB, memory.read(106L));
        assertEquals((byte) 0xAA, memory.read(107L));
    }

    @Test
    void testWriteLong_NegativeValue_StoredAndReadCorrectly() {
        long value = -123456789101112L;
        memory.writeLong(4000, value);
        assertEquals(value, memory.readLong(4000));
    }

    // --- TESTS FOR readInstructionAt ---

    @Test
    void testReadInstructionAt_ValidIndex() {
        MemoryManager.instructionsMemory.add("MOV R1, 5");
        assertEquals("MOV R1, 5", MemoryManager.readInstructionAt(0));
    }

    @Test
    void testReadInstructionAt_InvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            MemoryManager.readInstructionAt(999);
        });
    }

    // --- TESTS FOR writeInstructions ---

    @Test
    void testWriteInstructions_ValidFile() {
        try {
            String filePath = Files.createTempFile("testMemoryManagerClass", ".asm").toString();
            Files.write(Paths.get(filePath), List.of("MOV R1, 1", "HALT"));

            MemoryManager.writeInstructions(filePath);

            assertEquals(2, MemoryManager.instructionsMemory.size());
            assertEquals("MOV R1, 1", MemoryManager.instructionsMemory.get(0));
            assertEquals("HALT", MemoryManager.instructionsMemory.get(1));
        }
        catch (IOException e) {
            fail("IOException should not be thrown: " + e.getMessage());
        }
    }

    @Test
    void testWriteInstructions_FileDoesNotExist() {
        assertThrows(IOException.class, () -> {
            MemoryManager.writeInstructions("nonexistentFileMemoryManagerTest.asm");
        });
    }




}