package pkg.paran3xus.dou.controls;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import pkg.paran3xus.dou.Room.Utils.RoomState;
import pkg.paran3xus.dou.Room.Player.Player;

public class PlayerInfoPane extends GridPane {
    private PlayerInfoPaneController controller;

    public PlayerInfoPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PlayerInfoPane.fxml"));

            GridPane root = loader.load();
            controller = loader.getController();

            getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPlayerName(String name) {
        controller.setPlayerName(name);
    }

    public void setCardCount(int count) {
        controller.setCardCount(count);
    }

    public void setStatus(String status) {
        controller.setStatus(status);
    }

    public void setAvatar(Image image) {
        controller.setAvatar(image);
    }

    public void setPlayer(Player p, RoomState state) {
        if (p == null) {
            return;
        }

        setPlayerName(p.getNickname());
        setAvatar(p.getAvatar());
        switch (state) {
            case RoomState.READY:
                setStatus(p.getReady() ? "Ready" : "not Ready");
                break;

            default:
                break;
        }
    }
}
