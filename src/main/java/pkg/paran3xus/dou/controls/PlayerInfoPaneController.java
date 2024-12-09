package pkg.paran3xus.dou.controls;

import javafx.application.Platform;
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
    @FXML
    private Label playerIdentity;

    int count;

    public void setPlayerName(String name) {
        Platform.runLater(() -> {
            playerName.setText(name);
        });
    }

    public void setCardCount(int count) {
        Platform.runLater(() -> {
            cardCount.setText("Cards: " + count);
        });
        this.count = count;
    }

    public int getCardCount() {
        return count;
    }

    public void setStatus(String status) {
        Platform.runLater(() -> {
            statusMessage.setText(status);
        });
    }

    public void setAvatar(Image image) {
        Platform.runLater(() -> {
            avatarImage.setImage(image);
        });
    }

    public void setIdentity(boolean isLandlord) {
        Platform.runLater(() -> {
            playerIdentity.setText(isLandlord ? "Landlord" : "Farmer");
        });
    }
}
