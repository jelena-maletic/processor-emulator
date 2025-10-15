package instructions;

import util.OperandUtil;

public class InstructionExecutor {

    static ArithmeticOperations arithmeticOps = new ArithmeticOperations();
    static BitwiseOperations bitwiseOps = new BitwiseOperations();
    static DataTransferInstructions dataTransfer = new DataTransferInstructions();
    static JumpInstructions jumpInstr = new JumpInstructions();
    static IOInstructions ioInstr = new IOInstructions();
    public static boolean halted = false;

    public static void executeInstruction(String instruction) {
        instruction = instruction.trim();
        if (instruction.isEmpty()) {
            return;
        }
        String[] parts = instruction.split("[ ,]+");
        String opcode = parts[0].toUpperCase();
        switch (opcode) {
            case "ADD":
                arithmeticOps.add(parts[1], OperandUtil.parseOperand(parts[2]));
                break;
            case "SUB":
                arithmeticOps.sub(parts[1], OperandUtil.parseOperand(parts[2]));
                break;
            case "MUL":
                arithmeticOps.mul(parts[1], OperandUtil.parseOperand(parts[2]));
                break;
            case "DIV":
                arithmeticOps.div(parts[1], OperandUtil.parseOperand(parts[2]));
                break;
            case "AND":
                bitwiseOps.and(parts[1], OperandUtil.parseOperand(parts[2]));
                break;
            case "OR":
                bitwiseOps.or(parts[1], OperandUtil.parseOperand(parts[2]));
                break;
            case "XOR":
                bitwiseOps.xor(parts[1], OperandUtil.parseOperand(parts[2]));
                break;
            case "NOT":
                bitwiseOps.not(parts[1]);
                break;
            case "MOV":
                dataTransfer.mov(parts[1], parts[2]);
                break;
            case "CMP":
                jumpInstr.cmp(parts[1], parts[2]);
                break;
            case "JMP":
                jumpInstr.jmp(parts[1]);
                return;
            case "JE":
                jumpInstr.je(parts[1]);
                return;
            case "JNE":
                jumpInstr.jne(parts[1]);
                return;
            case "JGE":
                jumpInstr.jge(parts[1]);
                return;
            case "JL":
                jumpInstr.jl(parts[1]);
                return;
            case "IN":
                ioInstr.in(parts[1]);
                break;
            case "OUT":
                ioInstr.out(parts[1]);
                break;
            case "HALT":
                halted = true; // Sets the flag indicating that the program has stopped
                System.out.println("\n\nThe End");
                return;
            default:
                throw new IllegalArgumentException("Unknown instruction: " + opcode);
        }
    }
}


