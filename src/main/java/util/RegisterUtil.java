package util;

import app.Emulator;

public class RegisterUtil {

    public static void checkIfRegisterExists(String reg) {
        if (!Emulator.registers.containsKey(reg)) {
            throw new IllegalArgumentException("Register does not exist: " + reg);
        }
    }
}
