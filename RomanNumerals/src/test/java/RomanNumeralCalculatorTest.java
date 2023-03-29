import static org.junit.jupiter.api.Assertions.*;

import converter.RomanNumeralConverter;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.ValueSource;

import java.security.InvalidParameterException;

class RomanNumeralConverterTest {

    RomanNumeralConverter converter;

    @BeforeEach
    void setup(){
        converter = new RomanNumeralConverter();
    }


    @ParameterizedTest
    @DisplayName("isDecimal equivalence testing")
    @ValueSource(strings = {"1", "50", "3999"})
    void isDecimalEquivalence(String num){
        assertTrue(converter.isValidDecimal(num));
    }

    @ParameterizedTest
    @DisplayName("isDecimal false testing")
    @ValueSource(strings = {"a", "XCI", "&*#)"})
    void isDecimalException(String num){
        assertThrows(NumberFormatException.class, ()->{converter.isValidDecimal(num);});
    }

    @Test
    @DisplayName("decimalToNumeral pass")
    void decimalToNumeralTest(){
        String test = converter.decimalToNumeral(1);
        assertEquals("I", test);

        String test2 = converter.decimalToNumeral(746);
        assertEquals("DCCXLVI", test2);

        String test3 = converter.decimalToNumeral(3999);
        assertEquals("MMMCMXCIX", test3);
    }

    @ParameterizedTest
    @DisplayName("numeralToDecimal fail")
    @ValueSource(strings = {"IIII", "Hello", "IVIV", "VV", "(*^^)", "MMMM"})
    void numeralToDecimalException(String num){
        assertThrows(InvalidParameterException.class, ()->{converter.numeralToDecimal(num);});
    }

    @Test
    @DisplayName("numeralToDecimal Pass")
    void numeralToDecimalPass(){
        String test = converter.numeralToDecimal("XCIV");
        assertEquals("94", test);

        test = converter.numeralToDecimal("I");
        assertEquals("1", test);

        test = converter.numeralToDecimal("III");
        assertEquals("3", test);

        test = converter.numeralToDecimal("MMMCMXCIX");
        assertEquals("3999", test);
    }
}