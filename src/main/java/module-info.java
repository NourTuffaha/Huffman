module module.huffmancompression {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens Module to javafx.fxml;
    exports Module;
}