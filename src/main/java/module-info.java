module pkg.paran3xus.dou {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.java_websocket;
    requires com.google.gson;
    requires javafx.graphics;
    requires java.desktop;
    requires javafx.swing;

    exports pkg.paran3xus.dou;
    exports pkg.paran3xus.dou.controls;

    opens pkg.paran3xus.dou to javafx.fxml;
    opens pkg.paran3xus.dou.controls to javafx.fxml;
    opens pkg.paran3xus.dou.Room.Network.Message to com.google.gson;
    opens pkg.paran3xus.dou.Game to com.google.gson;
    opens pkg.paran3xus.dou.Room.Player to com.google.gson;
}