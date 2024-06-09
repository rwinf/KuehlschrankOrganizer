package test;

import main.KuehlschrankOrganizer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class KuehlschrankOrganizerTest {
    KuehlschrankOrganizer kuehlschrankOrganizer;

    @BeforeEach
    void setUp() {
        kuehlschrankOrganizer = new KuehlschrankOrganizer();
    }

    @AfterEach
    void tearDown() {
        kuehlschrankOrganizer.dispose();
    }

    @Test
    void eingabeIstFalsch1() {
        boolean expectedValue = true;
        boolean actualValue = kuehlschrankOrganizer.eingabeIstFalsch("<Name>", "01.02.2022");
        Assertions.assertEquals(expectedValue, actualValue);
    }

    @Test
    void eingabeIstFalsch2() {
        boolean expectedValue = true;
        boolean actualValue = kuehlschrankOrganizer.eingabeIstFalsch("", "01.02.2022");
        Assertions.assertEquals(expectedValue, actualValue);
    }

    @Test
    void eingabeIstFalsch3() {
        boolean expectedValue = false;
        boolean actualValue = kuehlschrankOrganizer.eingabeIstFalsch("Richtig", "01.02.2022");
        Assertions.assertEquals(expectedValue, actualValue);
    }
}