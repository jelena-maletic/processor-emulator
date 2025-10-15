package instructions;

import app.Emulator;
import memory.MemoryManager;
import util.*;

public class DataTransferInstructions {

    private final MemoryManager memoryManager = new MemoryManager();

    public void mov(String destination, String source) {
        try {
            if (destination.startsWith("[") && destination.endsWith("]")) {
                // Destination is a memory location: MOV [addr], reg or MOV [r], reg or MOV [r], const
                String addressStr = destination.substring(1, destination.length() - 1);
                long address = OperandUtil.resolveAddress(addressStr);
                long srcValue;
                if (Emulator.registers.containsKey(source)) {
                    // Source is a register
                    srcValue = Emulator.registers.get(source);
                }
                else {
                    // Source is a constant
                    srcValue = OperandUtil.parseOperand(source);
                }
                memoryManager.writeLong(address, srcValue);
            }
            else if (source.startsWith("[") && source.endsWith("]")) {
                // Source is a memory location: MOV reg, [addr] or MOV reg, [r]
                String addressStr = source.substring(1, source.length() - 1);
                long address = OperandUtil.resolveAddress(addressStr);
                RegisterUtil.checkIfRegisterExists(destination);
                long value = memoryManager.readLong(address); // reads a 64-bit value from memory starting at the given address
                Emulator.registers.put(destination, value);
            }
            else {
                // Direct transfer between registers or constant to register: MOV r1, r2 or MOV r1, const
                RegisterUtil.checkIfRegisterExists(destination);
                long value = OperandUtil.parseOperand(source);
                Emulator.registers.put(destination, value);
            }
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException("Error while executing MOV instruction: " + e.getMessage(), e);
        }
    }



}
