module pkg.paran3xus.dou {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.java_websocket;
    requires com.google.gson;
    requires javafx.graphics;
    requires java.desktop;
    requires javafx.swing;

    opens pkg.paran3xus.dou to javafx.fxml;

    exports pkg.paran3xus.dou;
}