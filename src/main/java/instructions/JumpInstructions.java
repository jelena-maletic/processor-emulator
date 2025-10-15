package instructions;

import app.Emulator;
import memory.MemoryManager;
import util.OperandUtil;

public class JumpInstructions {

    private final MemoryManager memoryManager = new MemoryManager();
    // 0 if operands are equal, >0 if the first is greater, <0 if the first is smaller
    public static int cmpFlag = 0;

    // CMP: Compares two operands and sets cmpFlag
    public void cmp(String op1, String op2) {
        boolean isOp1Mem = op1.startsWith("[") && op1.endsWith("]");
        boolean isOp2Mem = op2.startsWith("[") && op2.endsWith("]");

        if (isOp1Mem && isOp2Mem) {
            throw new IllegalArgumentException("CMP instruction does not allow comparison between two memory locations: " + op1 + ", " + op2);
        }

        long value1 = resolveValue(op1);
        long value2 = resolveValue(op2);
        cmpFlag = Long.compare(value1, value2);
    }

    //JMP: Unconditional jump - sets the program counter to the destination.
    public void jmp(String dest) {
        Emulator.programCounter = resolveDestination(dest);
    }

    //JE: Jump if operands are equal.
    public void je(String dest) {
        if (cmpFlag == 0) {
            jmp(dest);
        }
    }

    //JNE: Jump if operands are not equal.
    public void jne(String dest) {
        if (cmpFlag != 0) {
            jmp(dest);
        }
    }

    // JGE: Jump if the first operand is greater or equal.
    public void jge(String dest) {
        if (cmpFlag >= 0) {
            jmp(dest);
        }
    }

    //JL: Jump if the first operand is smaller.
    public void jl(String dest) {
        if (cmpFlag < 0) {
            jmp(dest);
        }
    }


    public long resolveDestination(String dest) {
        dest = dest.trim();
        if (dest.startsWith("[") && dest.endsWith("]")) {
            // If the operand has square brackets, that is indirect addressing.
            String inner = dest.substring(1, dest.length() - 1).trim();
            long address = OperandUtil.resolveAddress(inner);
            return memoryManager.readLong(address);
        } else {
            try {
                // If not in square brackets, try parsing it directly as a number.
                return Long.parseLong(dest);
            }
            catch (NumberFormatException e) {
                // If parsing fails, assume the operand is a label.
                if (Emulator.labels.containsKey(dest)) {
                    return Emulator.labels.get(dest);
                }
                else {
                    throw new IllegalArgumentException("Unknown jump destination: " + dest);
                }
            }
        }
    }

    public long resolveValue(String operand) {
        operand = operand.trim();
        if (operand.startsWith("[") && operand.endsWith("]")) {
            String inner = operand.substring(1, operand.length() - 1).trim();
            long address = OperandUtil.resolveAddress(inner);
            return memoryManager.readLong(address);
        } else {
            return OperandUtil.parseOperand(operand); // register or number
        }
    }
}
