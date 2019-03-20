import javafx.scene.control.TextArea;

public class LcdDisplay extends OutputDevice {

    public LcdDisplay(TextArea textArea) {
        super(textArea);
    }

    /**
     * Converts product to readable string and prints it on lcd screen
     *
     * @param product to display on lcd display
     */
    public void display(Product product) {
        print(String.format("%30s\t|\t%5s zl", product.getName(), product.getPrice()));
    }
}
