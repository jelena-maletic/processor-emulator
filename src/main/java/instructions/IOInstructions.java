package instructions;

import app.*;
import util.RegisterUtil;

import java.util.Scanner;

public class IOInstructions {

    public void in(String reg) {
        RegisterUtil.checkIfRegisterExists(reg);
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a character: ");
        char inputChar = scanner.next().charAt(0);
        Emulator.registers.put(reg, (long) (inputChar & 0xFF));  // takes the last 8 bits
    }

    public void out(String reg) {
        RegisterUtil.checkIfRegisterExists(reg);
        long regValue = Emulator.registers.get(reg);
        char outputChar = (char) (regValue & 0xFF); // takes the last 8 bits
        System.out.print(outputChar);
    }
}
