module pkg.paran3xus.dou {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.java_websocket;
    requires com.google.gson;
    requires javafx.graphics;
    requires java.desktop;
    requires javafx.swing;

    opens pkg.paran3xus.dou to javafx.fxml;
    opens pkg.paran3xus.dou.Room.Network.Message to com.google.gson;
    opens pkg.paran3xus.dou.Room.Player to com.google.gson;

    exports pkg.paran3xus.dou;
}