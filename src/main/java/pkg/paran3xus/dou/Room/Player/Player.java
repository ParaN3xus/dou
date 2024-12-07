package pkg.paran3xus.dou.Room.Player;

import java.io.ByteArrayInputStream;

import org.java_websocket.WebSocket;

import javafx.scene.image.Image;
import pkg.paran3xus.dou.Game.CardCollection;
import pkg.paran3xus.dou.Room.Network.Message.GameMessage.JoinData;

public class Player {
    private String nickname;
    private String id;
    private Image avatar;
    private boolean isReady;
    private WebSocket connection;
    private CardCollection cards;

    public Player(JoinData joinData, WebSocket conn) {
        nickname = joinData.getNickname();
        id = joinData.getId();

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

    public String getId() {
        return id;
    }

    public void setCards(CardCollection cards) {
        this.cards = cards;
    }

    public CardCollection getCards() {
        return cards;
    }

    public void move(CardCollection col) {
        cards.subtract(col);
    }
}
