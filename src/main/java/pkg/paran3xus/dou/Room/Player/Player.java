package pkg.paran3xus.dou.Room.Player;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.WebSocket;

import javafx.scene.image.Image;
import pkg.paran3xus.dou.Room.Network.Message.GameMessage.JoinData;

public class Player {
    private String playerId;
    private Image avatar;
    private boolean isReady;
    private WebSocket connection;

    public Player(JoinData joinData, WebSocket conn) {
        playerId = joinData.getId();

        ByteArrayInputStream bis = new ByteArrayInputStream(joinData.getAvatar());
        avatar = new Image(bis);
        connection = conn;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public boolean getReady() {
        return isReady;
    }

    public WebSocket getConnection() {
        return connection;
    }
}
