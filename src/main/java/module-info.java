module pkg.paran3xus.dou {
    requires javafx.controls;
    requires javafx.fxml;


    opens pkg.paran3xus.dou to javafx.fxml;
    exports pkg.paran3xus.dou;
}