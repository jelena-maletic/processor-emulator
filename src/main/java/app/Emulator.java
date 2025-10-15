package app;

import java.io.IOException;
import java.util.*;
import memory.*;
import util.*;
import instructions.InstructionExecutor;

public class Emulator {
    public static Map<String, Long> registers = new HashMap<>();
    public static long programCounter = 0;
    public static Map<String, Integer> labels = new HashMap<>();
    public static PropertiesManager propertiesManager=new PropertiesManager("filePath.properties");

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Emulator emulator = new Emulator();
            initializeRegisters();

            String asmFilePath;

            System.out.println("Select one of the available options: ");
            System.out.println("[1] Use the default ASM file");
            System.out.println("[2] Manually enter the absolute path to the ASM file");
            String choice = scanner.nextLine().trim().toLowerCase();
            if (choice.equals("2")) {
                System.out.print("Enter the absolute path to the ASM file: ");
                asmFilePath = scanner.nextLine().trim();
                if (asmFilePath.isEmpty()) {
                    System.err.println("No path entered.");
                    return;
                }
            }
            else {
                asmFilePath = propertiesManager.getProperty("ASM_FILE");
                if (asmFilePath == null || asmFilePath.isEmpty()) {
                    System.err.println("Path to the ASM file is not defined in the properties file.");
                    return;
                }
            }

            MemoryManager.writeInstructions(asmFilePath);
            for (int i = 0; i < MemoryManager.instructionsMemory.size(); i++) {
                String line = MemoryManager.instructionsMemory.get(i).trim();
                if (line.endsWith(":")) {
                    String label = line.substring(0, line.length() - 1).trim();
                    labels.put(label, i);
                }
            }
            emulator.executeProgram();
        }
        catch (IOException e) {
            System.err.println("Error while loading instructions: " + e.getMessage());
            e.printStackTrace();
        }
        catch (RuntimeException e) {
            System.err.println("Error while executing program: " + e.getMessage());
            e.printStackTrace();
        }
        catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void initializeRegisters() {
        registers.put("R1", 0L);
        registers.put("R2", 0L);
        registers.put("R3", 0L);
        registers.put("R4", 0L);
    }


    public void executeProgram() {
        while (!InstructionExecutor.halted && programCounter < MemoryManager.instructionsMemory.size()) {
            String instruction = MemoryManager.readInstructionAt((int) programCounter);
            // Skips labels since they are already added to the label map
            if (instruction.endsWith(":")) {
                programCounter++;
                continue;
            }
            try {
                InstructionExecutor.executeInstruction(instruction);
            } catch (Exception e) {
                System.err.println("Error in instruction at line " + programCounter + ": " + instruction);
                e.printStackTrace();
                break;
            }
            programCounter++;
        }

        System.out.println("Program finished. Register states:");
        registers.forEach((k, v) -> System.out.println(k + " = " + v));

    }

}
