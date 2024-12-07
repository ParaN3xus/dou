package pkg.paran3xus.dou;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import pkg.paran3xus.dou.Room.Room;

public class MainController {
    @FXML
    private Label welcomeText;

    private Room activeRoom;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}