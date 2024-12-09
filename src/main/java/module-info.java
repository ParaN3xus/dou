module pkg.paran3xus.dou {
    requires transitive org.java_websocket;
    requires transitive javafx.graphics;
    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires com.google.gson;

    exports pkg.paran3xus.dou;
    exports pkg.paran3xus.dou.controls;
    exports pkg.paran3xus.dou.Game;
    exports pkg.paran3xus.dou.Room;
    exports pkg.paran3xus.dou.Room.Player;
    exports pkg.paran3xus.dou.Room.Network.Message;

    opens pkg.paran3xus.dou to javafx.fxml;
    opens pkg.paran3xus.dou.controls to javafx.fxml;
    opens pkg.paran3xus.dou.Room.Network.Message to com.google.gson;
    opens pkg.paran3xus.dou.Game to com.google.gson;
    opens pkg.paran3xus.dou.Room.Player to com.google.gson;
}