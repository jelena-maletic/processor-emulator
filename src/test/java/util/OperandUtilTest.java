package util;

import app.Emulator;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class OperandUtilTest {

    @BeforeEach
    public void setUp() {
        Emulator.registers.clear();
        Emulator.registers.put("R1", 10L);
        Emulator.registers.put("R2", 255L);
    }

    // --- TESTS FOR parseOperand ---

    @Test
    public void testParseOperand_Register() {
        assertEquals(10L, OperandUtil.parseOperand("R1"));
    }

    @Test
    public void testParseOperand_RegisterTrimmed() {
        assertEquals(255L, OperandUtil.parseOperand("   R2   "));
    }

    @Test
    public void testParseOperand_DecimalNumber() {
        assertEquals(123L, OperandUtil.parseOperand("123"));
    }

    @Test
    public void testParseOperand_HexWithPrefix() {
        assertEquals(255L, OperandUtil.parseOperand("0xFF"));
    }

    @Test
    public void testParseOperand_HexWithSuffix() {
        assertEquals(255L, OperandUtil.parseOperand("FFh"));
    }

    @Test
    public void testParseOperand_HexWithSpaces() {
        assertEquals(4096L, OperandUtil.parseOperand("  0x1000  "));
    }

    @Test
    public void testParseOperand_InvalidNumber_Letters() {
        assertThrows(IllegalArgumentException.class, () -> {
            OperandUtil.parseOperand("xyz");
        });
    }

    @Test
    public void testParseOperand_InvalidNumber_Brackets() {
        assertThrows(IllegalArgumentException.class, () -> {
            OperandUtil.parseOperand("[0xFF]");
        });
    }

    @Test
    public void testParseOperand_NullString() {
        assertThrows(NullPointerException.class, () -> {
            OperandUtil.parseOperand(null);
        });
    }

    // --- TESTS FOR convertHexAddressToLong ---

    @Test
    public void testConvertHexAddress_Prefix() {
        assertEquals(4096L, OperandUtil.convertHexAddressToLong("0x1000"));
    }

    @Test
    public void testConvertHexAddress_Suffix() {
        assertEquals(4096L, OperandUtil.convertHexAddressToLong("1000h"));
    }

    @Test
    public void testConvertHexAddress_WithSpaces() {
        assertEquals(65535L, OperandUtil.convertHexAddressToLong("  0xFFFF  "));
    }

    @Test
    public void testConvertHexAddress_InvalidLetters() {
        assertThrows(NumberFormatException.class, () -> {
            OperandUtil.convertHexAddressToLong("XYZ");
        });
    }

    // --- TESTS FOR resolveAddress ---

    @Test
    public void testResolveAddress_Register() {
        assertEquals(255L, OperandUtil.resolveAddress("R2"));
    }

    @Test
    public void testResolveAddress_HexWithPrefix() {
        assertEquals(8192L, OperandUtil.resolveAddress("0x2000"));
    }

    @Test
    public void testResolveAddress_HexWithSuffix() {
        assertEquals(65535L, OperandUtil.resolveAddress("FFFFh"));
    }

    @Test
    public void testResolveAddress_NonExistingRegister() {
        assertThrows(IllegalArgumentException.class, () -> OperandUtil.resolveAddress("R5")); // not a register, treated as hex
    }

    @Test
    public void testResolveAddress_InvalidFormat() {
        assertThrows(NumberFormatException.class, () -> {
            OperandUtil.resolveAddress("@#");
        });
    }

    @Test
    public void testResolveAddress_WithSpaces() {
        assertEquals(1L, OperandUtil.resolveAddress("   01h  "));
    }

    @Test
    public void testResolveAddress_HexOverflow_TooBigHex() {
        assertThrows(NumberFormatException.class, () -> OperandUtil.convertHexAddressToLong("10000000000000000h")); // 2^64
    }

    @Test
    public void testResolveAddress_MaxSignedLong() {
        assertEquals(Long.MAX_VALUE, OperandUtil.convertHexAddressToLong("7FFFFFFFFFFFFFFFh"));
    }

    @Test
    public void testResolveAddress_NegativeAddress() {
        assertThrows(NumberFormatException.class, () -> OperandUtil.convertHexAddressToLong("-1"));
    }

}