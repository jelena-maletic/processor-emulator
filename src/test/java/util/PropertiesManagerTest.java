package util;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class PropertiesManagerTest {

    private PropertiesManager propertiesManager;

    @BeforeEach
    void setUp() {
        propertiesManager = new PropertiesManager("filePath.properties");
    }

    // --- TESTS FOR getProperty ---

    @Test
    void testGetProperty_ExistingKey() {
        assertEquals("src/main/resources/data/test1.asm", propertiesManager.getProperty("ASM_FILE"));
    }

    @Test
    void testGetProperty_NonExistingKey() {
        assertNull(propertiesManager.getProperty("nonexistent"));
    }

    // --- TEST FOR constructor ---

    @Test
    void testConstructor_FileNotFound() {
        assertThrows(NullPointerException.class, () -> new PropertiesManager("nonexistentFile.properties"));
    }

}