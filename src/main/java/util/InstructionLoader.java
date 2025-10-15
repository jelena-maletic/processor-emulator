package util;

import java.util.*;
import java.io.*;

public class InstructionLoader {

    public static List<String> loadInstructions(String filePath) throws IOException {
        List<String> instructions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String cleanLine = removeComment(line).trim();
                if (!cleanLine.isEmpty()) {
                    instructions.add(cleanLine);
                }
            }
        }
        return instructions;
    }

    public static String removeComment(String line) {
        int commentIndex = line.indexOf(';');
        return (commentIndex >= 0) ? line.substring(0, commentIndex) : line;
    }
}
