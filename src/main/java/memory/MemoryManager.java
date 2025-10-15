package memory;

import util.InstructionLoader;

import java.io.IOException;
import java.util.*;

public class MemoryManager {

    public static Map<Long, byte[]> dataMemory = new HashMap<>();
    public static List<String> instructionsMemory = new ArrayList<>();
    private static final int BLOCK_SIZE = 8;


    public static void writeInstructions(String filePath) throws IOException {
        instructionsMemory.clear();
        instructionsMemory.addAll(InstructionLoader.loadInstructions(filePath));
    }

    public static String readInstructionAt(int index) {
        return instructionsMemory.get(index);
    }

    public byte read(long address) {
        long blockNumber = address / BLOCK_SIZE;
        int offset = (int) (address % BLOCK_SIZE);
        byte[] block = dataMemory.get(blockNumber);
        if (block == null) {
            return 0;
        }
        return block[offset];
    }

    public void write(long address, byte value) {
        long blockNumber = address / BLOCK_SIZE;
        int offset = (int) (address % BLOCK_SIZE);
        byte[] block = dataMemory.computeIfAbsent(blockNumber, k -> new byte[BLOCK_SIZE]);
        block[offset] = value;
    }

    public long readLong(long address) {
        long value = 0;
        for (int i = 0; i < 8; i++) {
            value |= (((long) read(address + i) & 0xFF) << (8 * i));
        }
        return value;
    }

    public void writeLong(long address, long value) {
        for (int i = 0; i < 8; i++) {
            byte b = (byte) ((value >> (8 * i)) & 0xFF);
            write(address + i, b);
        }
    }
}
