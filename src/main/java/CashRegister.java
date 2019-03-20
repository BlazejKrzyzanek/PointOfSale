import javafx.scene.control.TextArea;
import org.h2.jdbcx.JdbcDataSource;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CashRegister {

    // I/O devices
    private final ProductScanner productScanner;
    private final LcdDisplay lcdDisplay;
    private final OutputDevice printer;

    private Statement statement;
    private Connection connection;
    private ResultSet resultSet;

    // Processed products since last exit()
    private List<Product> products;

    public CashRegister(ProductScanner productScanner, LcdDisplay lcdDisplay, OutputDevice outputDevice,
                 Statement statement, Connection connection, ResultSet resultSet) {
        this.productScanner = productScanner;
        this.lcdDisplay = lcdDisplay;
        this.printer = outputDevice;
        this.statement = statement;
        this.connection = connection;
        this.resultSet = resultSet;
        this.products = new ArrayList<>();
    }

    public CashRegister(TextArea lcdDisplay, TextArea printer) {

        this.productScanner = new ProductScanner();
        this.lcdDisplay = new LcdDisplay(lcdDisplay);
        this.printer = new OutputDevice(printer);
        this.products = new ArrayList<>();
    }

    /**
     * Processes scanned (random) product and displays it on lcd display
     */
    public void processProduct() {
        String barcode = productScanner.scanBarcode();
        processProduct(barcode);
    }

    /**
     * Processes given product and displays it on lcd display
     */
    public void processProduct(String barcode) {
        Product product;
        try {
            product = getProductFromDatabase(barcode);
        } catch (SQLDataException e) {
            lcdDisplay.print(e.getMessage());
            return;
        } catch (SQLException e) {
            // trying to reconnect when the connection is lost
            connectWithDatabase();
            return;
        }
        lcdDisplay.display(product);
        products.add(product);
    }

    /**
     * Prints receipt with outputDevice and displays total price on lcd display
     */
    public void exit() {

        StringBuilder receipt = new StringBuilder(String.format(
                "%15s\t|%10s\n---------------------------\n", "Product", "Price"));

        BigDecimal sum = BigDecimal.ZERO;
        for (Product p : products) {
            sum = sum.add(p.getPrice());
            receipt.append(String.format("%15s\t|%10s\n", p.getName(), p.getPrice()));
        }
        receipt.append(String.format("\nTOTAL:\t%s zl\n", sum));
        lcdDisplay.print(String.format("TOTAL:\t%s zl", sum.toString()));
        printer.print(receipt.toString());
        products.clear();
    }


    /**
     * Connects with in-memory h2 database
     */
    public void connectWithDatabase() {

        try {
            JdbcDataSource ds = new JdbcDataSource();
            ds.setURL("jdbc:h2:mem:");
            ds.setUser("sa");
            ds.setPassword("sa");
            connection = ds.getConnection();

            statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCTS (barcode VARCHAR(9), name VARCHAR(30), price VARCHAR(10))";
            statement.execute(sql);
            statement.execute(generateProductsDatabaseInsertionSql());
        } catch (SQLException e) {
            lcdDisplay.print(e.getMessage());
        }
    }

    /**
     * Used only to initialize in-memory database with some product entities
     * @return sql insert into statement as String
     */
    private String generateProductsDatabaseInsertionSql() {
        String[] names = new String[]{"milk", "cornflakes", "sugar", "beer", "coffee", "tea", "cheese", "soda", "water",
                "chocolate", "bread", "chicken", "salmon", "halibut", "Reduced-sodium lunchmeat", "roast beef", "brown rice",
                "tomato sauce", "mustard", "barbecue sauce", "salsa", "hot pepper", "broccoli", "spinach", "frozen pizza", "eggs"
        };
        StringBuilder sql = new StringBuilder("INSERT INTO PRODUCTS (barcode, name, price) VALUES ");
        Random r = new Random();
        for (int i = 0; i < 1000; i++) {
            sql.append(new StringBuilder("('")
                    .append(999_999_000 + i)
                    .append("', '")
                    .append(names[r.nextInt(names.length)])
                    .append("', '")
                    .append(r.nextInt(20))
                    .append(".")
                    .append(r.nextInt(100))
                    .append("') ")
            );
            sql.append(",");
        }
        // remove trailing comma
        sql.deleteCharAt(sql.length() - 1);
        //sql.append(";");
        return sql.toString();
    }

    /**
     * @param barcode - barcode of searched product
     * @return found product if exists in database
     * @throws SQLException exception when application is not connected with database
     */
    private Product getProductFromDatabase(String barcode) throws SQLException {

        if (statement == null)
            throw new SQLException("Not connected to database");

        if (barcode.isEmpty() || !barcode.matches("[0-9]{9}"))
            throw new SQLDataException("Invalid barcode");

        String query = "SELECT * FROM PRODUCTS WHERE barcode=" + barcode;
        resultSet = statement.executeQuery(query);
        if (!resultSet.next())
            throw new SQLDataException(String.format("Product %s not found", barcode));

        return new Product(resultSet.getString("barcode"),
                resultSet.getString("name"),
                resultSet.getString("price"));
    }

    public List<Product> getProducts() {
        return products;
    }
}
