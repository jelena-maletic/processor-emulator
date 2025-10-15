package instructions;

import app.*;
import util.RegisterUtil;

public class ArithmeticOperations {


    public void add(String reg, long value) {
        RegisterUtil.checkIfRegisterExists(reg);
        long regValue = Emulator.registers.get(reg);
        long result = regValue + value;
        Emulator.registers.put(reg, result);
    }

    public void sub(String reg, long value) {
        RegisterUtil.checkIfRegisterExists(reg);
        long regValue = Emulator.registers.get(reg);
        long result = regValue - value;
        Emulator.registers.put(reg, result);
    }

    public void div(String reg, long value) {
        RegisterUtil.checkIfRegisterExists(reg);
        long regValue = Emulator.registers.get(reg);
        if (value == 0) {
            System.err.println("Error: Division by zero! " + reg);
        }
        else {
            long result = regValue / value;
            Emulator.registers.put(reg, result);
        }
    }

    public void mul(String reg, long value) {
        RegisterUtil.checkIfRegisterExists(reg);
        long regValue = Emulator.registers.get(reg);
        long result = regValue * value;
        Emulator.registers.put(reg, result);
    }

}
