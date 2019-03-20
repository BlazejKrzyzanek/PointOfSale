import java.util.Random;

public class ProductScanner {

    private final Random r;

    public ProductScanner() {
        r = new Random();
    }

    /**
     * Generates pseudo random barcode from a known range of barcodes
     * @return generated barcode
     */
    public String scanBarcode(){
        return String.valueOf(r.nextInt(1000) + 999_999_000);
}
}
