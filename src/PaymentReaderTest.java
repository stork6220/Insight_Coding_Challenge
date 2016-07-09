import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class PaymentReaderTest {
    PaymentReader reader = new PaymentReader("venmo_input/venmo-testInput.txt");
    @Test
    public void testPaymentReader() {
        List<String> result = Arrays.asList("B", "A", "2016-03-28 23:23:12");
        List<String> next = reader.next();
        assertEquals(result, next);
        assertEquals(false, reader.hasNext());
    }
}