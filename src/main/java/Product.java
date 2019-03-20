import java.math.BigDecimal;

public class Product {

    private String barcode;
    private String name;
    private BigDecimal price;

    public Product(String barcode, String name, String price) {
        if (barcode.isEmpty() || !barcode.matches("[0-9]{9}"))
            throw new IllegalArgumentException("Invalid barcode");
        this.barcode = barcode;

        if (name.isEmpty())
            throw new IllegalArgumentException("Invalid product name");
        this.name = name;

        this.price = new BigDecimal(price);
        if (this.price.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Price must be positive");

    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getBarcode() {
        return barcode;
    }

    @Override
    public String toString() {
        return "Product{" +
                "barcode='" + barcode + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
