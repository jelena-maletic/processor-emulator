package util;

import app.Emulator;

public class OperandUtil {

    public static long parseOperand(String operandStr) throws IllegalArgumentException{
        operandStr = operandStr.trim();
        if (Emulator.registers.containsKey(operandStr)) {
            return Emulator.registers.get(operandStr);
        }
        try {
            if (operandStr.toLowerCase().startsWith("0x") || operandStr.toLowerCase().endsWith("h")) {
                return convertHexAddressToLong(operandStr);
            }
            else {
                return Long.parseLong(operandStr);
            }
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid operand: " + operandStr, e);
        }
    }

    public static long convertHexAddressToLong(String addressStr) {
        addressStr = addressStr.replace(" ", "");
        if (addressStr.toLowerCase().startsWith("0x")) {
            addressStr = addressStr.substring(2);
        }
        else if (addressStr.toLowerCase().endsWith("h")) {
            addressStr = addressStr.substring(0, addressStr.length() - 1);
        }
        return Long.parseUnsignedLong(addressStr, 16);
    }



    public static long resolveAddress(String addressStr) {
        if (Emulator.registers.containsKey(addressStr)) {
            return Emulator.registers.get(addressStr);  // Register indirect addressing
        }
        else {
            return convertHexAddressToLong(addressStr);  // Direct addressing with hex address
        }
    }
}
