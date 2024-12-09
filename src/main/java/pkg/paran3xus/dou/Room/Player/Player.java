package pkg.paran3xus.dou.Room.Player;

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

        avatar = joinData.getJFXAvatar();
        connection = conn;
    }

    public Player(PlayerInfo info) {
        nickname = info.getNickname();
        id = info.getId();
        isReady = info.getIsReady();
        avatar = info.getJFXAvatar();
        connection = null;
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

    public String getNickname() {
        return nickname;
    }

    public Image getAvatar() {
        return avatar;
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

    public void setId(String id) {
        this.id = id;
    }

    public PlayerInfo toPlayerInfo(int index) {
        return new PlayerInfo(nickname, id, avatar, isReady, index);
    }

    public void distHiddenCard(CardCollection col) {
        cards.addCards(col.getCards());
    }
}
