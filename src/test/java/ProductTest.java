import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


class ProductTest {

    private static final String PRODUCT_BARCODE = "123456789";
    private static final String PRODUCT_NAME = "Milk";
    private static final String PRODUCT_PRICE = "7.99";

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product(PRODUCT_BARCODE, PRODUCT_NAME, PRODUCT_PRICE);
    }

    @Test
    void shouldBeAbleToCreateProduct() {
        Product product1 = new Product(PRODUCT_BARCODE, PRODUCT_NAME, PRODUCT_PRICE);
        assertNotNull(product1);
    }

    @Test
    void shouldntAllowToCreateProductWithWrongPrice() {
        assertThrows(IllegalArgumentException.class, () -> new Product(PRODUCT_BARCODE, PRODUCT_NAME, "-1"));
        assertThrows(IllegalArgumentException.class, () -> new Product(PRODUCT_BARCODE, PRODUCT_NAME, "0"));
        assertThrows(NumberFormatException.class, () -> new Product(PRODUCT_BARCODE, PRODUCT_NAME, "abc"));
    }

    @Test
    void shouldntAllowToCreateProductWithWrongName() {
        assertThrows(IllegalArgumentException.class, () -> new Product(PRODUCT_BARCODE, "", PRODUCT_PRICE));
    }

    @Test
    void shouldGetValidName() {
        assertEquals(PRODUCT_NAME, product.getName());
    }

    @Test
    void shouldGetValidPrice() {
        assertEquals(new BigDecimal(PRODUCT_PRICE), product.getPrice());
    }

    @Test
    void shouldGetValidBarcode() {
        assertEquals(PRODUCT_BARCODE, product.getBarcode());
    }
}
