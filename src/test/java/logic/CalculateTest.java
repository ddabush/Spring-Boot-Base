package logic;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CalculateTest {
    Calculate c;
    @BeforeAll
    public void setup() {
        c = new Calculate();
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void calculatePow() {
        assertEquals(c.calculatePow(2), 4);
    }
}