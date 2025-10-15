package util;

import app.Emulator;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class RegisterUtilTest {

    @BeforeEach
    void setup() {
        Emulator.registers.clear();
        Emulator.registers.put("R1", 10L);
        Emulator.registers.put("R2", 20L);
    }

    @Test
    void checkIfRegisterExists_RegisterExists() {
        assertDoesNotThrow(() -> RegisterUtil.checkIfRegisterExists("R1"));
    }

    @Test
    void checkIfRegisterExists_RegisterDoesNotExist() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            RegisterUtil.checkIfRegisterExists("R5");
        });
        assertEquals("Register does not exist: R5", exception.getMessage());
    }

    @Test
    void checkIfRegisterExists_NullInput() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            RegisterUtil.checkIfRegisterExists(null);
        });
        assertEquals("Register does not exist: null", exception.getMessage());
    }

    @Test
    void checkIfRegisterExists_EmptyString() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            RegisterUtil.checkIfRegisterExists("");
        });
        assertEquals("Register does not exist: ", exception.getMessage());
    }

}