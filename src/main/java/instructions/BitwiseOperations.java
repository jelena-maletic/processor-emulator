package instructions;

import app.*;
import util.RegisterUtil;

public class BitwiseOperations {

    public void and(String reg, long value) {
        RegisterUtil.checkIfRegisterExists(reg);
        long regValue = Emulator.registers.get(reg);
        long result = regValue & value;
        Emulator.registers.put(reg, result);
    }

    public void or(String reg, long value) {
        RegisterUtil.checkIfRegisterExists(reg);
        long regValue = Emulator.registers.get(reg);
        long result = regValue | value;
        Emulator.registers.put(reg, result);
    }

    public void xor(String reg, long value) {
        RegisterUtil.checkIfRegisterExists(reg);
        long regValue = Emulator.registers.get(reg);
        long result = regValue ^ value;
        Emulator.registers.put(reg, result);
    }

    public void not(String reg) {
        RegisterUtil.checkIfRegisterExists(reg);
        long regValue = Emulator.registers.get(reg);
        long result = ~regValue;
        Emulator.registers.put(reg, result);
    }

}
