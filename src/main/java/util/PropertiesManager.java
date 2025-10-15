package util;

import java.io.*;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesManager {
    private final Properties properties = new Properties();

    public PropertiesManager(String fileName) {
        InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        try {
            properties.load(input);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

}
