package test;

import main.helper.Kategorie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class KategorieTest {

    @Test
    void get1() {
        Kategorie expectedValue = Kategorie.GEMUESE;
        Kategorie actualValue = Kategorie.get("Gemüse");
        Assertions.assertEquals(expectedValue, actualValue);
    }

    @Test
    void get2() {
        Kategorie expectedValue = Kategorie.FLEISCH;
        Kategorie actualValue = Kategorie.get("Meat");
        Assertions.assertEquals(expectedValue, actualValue);
    }

    @Test
    void get3() {
        Kategorie expectedValue = Kategorie.KEINE;
        Kategorie actualValue = Kategorie.get(0);
        Assertions.assertEquals(expectedValue, actualValue);
    }

    @Test
    void getAsStringArray() {
        String[] actualValue = Kategorie.getAlsStringArray(0);
        String[] expectedValue = new String[]{"Keine", "Obst", "Gemüse", "Fleisch", "Snacks", "Getränke"};
        Assertions.assertArrayEquals(actualValue, expectedValue);
    }

    @Test
    void testToString() {
        String expectedValue = "Gemüse";
        String actualValue = Kategorie.GEMUESE.toString(0);
        Assertions.assertEquals(expectedValue, actualValue);
    }
}