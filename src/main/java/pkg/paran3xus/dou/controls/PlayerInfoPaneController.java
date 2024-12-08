package pkg.paran3xus.dou.controls;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class PlayerInfoPaneController {
    @FXML
    private GridPane playerInfoPane;
    @FXML
    private ImageView avatarImage;
    @FXML
    private Label playerName;
    @FXML
    private Label cardCount;
    @FXML
    private Label statusMessage;

    public void setPlayerName(String name) {
        playerName.setText(name);
    }

    public void setCardCount(int count) {
        cardCount.setText("Cards: " + count);
    }

    public void setStatus(String status) {
        statusMessage.setText(status);
    }

    public void setAvatar(Image image) {
        avatarImage.setImage(image);
    }
}
