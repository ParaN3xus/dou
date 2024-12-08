package pkg.paran3xus.dou.Room.Network;

import java.net.URI;
import java.util.UUID;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import javafx.scene.image.Image;
import pkg.paran3xus.dou.Room.Network.Message.GameMessage.BidData;
import pkg.paran3xus.dou.Room.Network.Message.GameMessage.JoinData;
import pkg.paran3xus.dou.Room.Network.Message.GameMessage.ReadyData;
import pkg.paran3xus.dou.Room.Network.Message.*;
import pkg.paran3xus.dou.Room.Player.PlayerInfo;
import pkg.paran3xus.dou.Room.Player.Players.PlayerFullException;

public class WSClient extends WebSocketClient {
    public interface Callback {
        void onClientMessage(GameMessage message) throws PlayerFullException;
    }

    public Callback callback;

    private MessageProcessor msgProcessor = new MessageProcessor();

    String nickname;
    Image avatar;

    public WSClient(URI uri, String nickname, Image avatar, Callback callback) {
        super(uri);
        this.avatar = avatar;
        this.nickname = nickname;
        this.callback = callback;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        String id = UUID.randomUUID().toString();

        send(msgProcessor.serialize(MessageType.JOIN, new JoinData(new PlayerInfo(nickname, id, avatar, false, -1))));
    }

    @Override
    public void onMessage(String message) {
        GameMessage msg = msgProcessor.deserialize(message);
        try {
            callback.onClientMessage(msg);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("closed: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("error: " + ex);
    }

    public void sendReady(String id) {
        send(msgProcessor.serialize(MessageType.READY, new ReadyData(id)));
    }

    public void sendBid(String id, boolean bid) {
        send(msgProcessor.serialize(MessageType.BID, new BidData(id, bid)));
    }
}
