import javafx.scene.control.TextArea;

public class OutputDevice {

    private TextArea textArea;

    public OutputDevice(TextArea textArea) {
        this.textArea = textArea;
    }

    /**
     * @param text to print on the printer
     */
    public void print(String text){
        textArea.setText(text);
    }

}
