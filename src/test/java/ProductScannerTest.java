import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


class ProductScannerTest {

    private ProductScanner scanner;

    @BeforeEach
    void setUp() {
        scanner = new ProductScanner();
    }

    @Test
    void shouldScanProductBarcode() {
        assertTrue(scanner.scanBarcode().matches("[0-9]{9}"));
    }

}
