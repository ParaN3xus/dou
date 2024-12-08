package pkg.paran3xus.dou.Controls;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

public class PlayerInfoPane extends GridPane {
    private PlayerInfoPaneController controller;

    public PlayerInfoPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("controls/PlayerInfoPane.fxml"));
            GridPane root = loader.load();
            controller = loader.getController();

            // 将加载的内容添加到这个组件中
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
}
