module pkg.paran3xus.dou {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.java_websocket;
    requires com.google.gson;

    opens pkg.paran3xus.dou to javafx.fxml;

    exports pkg.paran3xus.dou;
}