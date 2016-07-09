import org.junit.Test;

import static org.junit.Assert.*;

public class CalculatorTest {
    Calculator calculator = new Calculator();
    @Test
    public void testCalculator() {
        calculator.add("a"); calculator.add("b");calculator.add("d");
        assertEquals(true, calculator.getMedian() == 1.00);
        calculator.add("b");
        assertEquals(true, calculator.getMedian() == 1.00);
        calculator.add("c"); calculator.add("c");calculator.add("c");
        assertEquals(true, calculator.getMedian() == 1.50);
        calculator.subtract("b");calculator.subtract("a");
        assertEquals(true, calculator.getMedian() == 1.00);
        calculator.add("e"); calculator.add("e");calculator.add("e"); calculator.add("e"); calculator.add("e");calculator.add("e");
        assertEquals(true, calculator.getMedian() == 2.00);
    }
}