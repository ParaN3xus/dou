package pkg.paran3xus.dou.Room.Network;

import java.net.InetSocketAddress;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import pkg.paran3xus.dou.Game.CardCollection;
import pkg.paran3xus.dou.Room.Network.Message.*;
import pkg.paran3xus.dou.Room.Network.Message.GameMessage.*;
import pkg.paran3xus.dou.Room.Player.Player;
import pkg.paran3xus.dou.Room.Player.Players;
import pkg.paran3xus.dou.Room.Player.Players.PlayerFullException;

public class WSServer extends WebSocketServer {
    public interface Callback {
        void onServerMessage(GameMessage message, WebSocket conn) throws PlayerFullException;
    }

    public Callback callback;

    public WSServer(Callback callback) {
        super(new InetSocketAddress(17963));
        this.callback = callback;
        this.start();
    }

    private MessageProcessor msgProcessor = new MessageProcessor();

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("New connection: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Closed connection: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        GameMessage msg = msgProcessor.deserialize(message);
        try {
            callback.onServerMessage(msg, conn);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("Error occurred on connection " + conn.getRemoteSocketAddress());
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("WebSocket server started");
    }

    public void notifyReady(ReadyData data) {
        broadcast(msgProcessor.serialize(MessageType.READY, data));
    }

    public void notifyLeave(LeaveData data) {
        broadcast(msgProcessor.serialize(MessageType.LEAVE, data));
    }

    public void notifyChat(ChatData data) {
        broadcast(msgProcessor.serialize(MessageType.CHAT, data));
    }

    public void notifyAskBid(AskBidData data) {
        broadcast(msgProcessor.serialize(MessageType.ASK_BID, data));
    }

    public void notifyAskMove(AskMoveData data) {
        broadcast(msgProcessor.serialize(MessageType.ASK_MOVE, data));
    }

    public void notifyBid(BidData data) {
        broadcast(msgProcessor.serialize(MessageType.BID, data));
    }

    public void notifyMove(MoveData data) {
        broadcast(msgProcessor.serialize(MessageType.MOVE, data));
    }

    public void notifyEnd(EndData data) {
        broadcast(msgProcessor.serialize(MessageType.END, data));
    }

    public void notifyPlayers(Players players) {
        PlayersData data = new PlayersData(players);
        broadcast(msgProcessor.serialize(MessageType.PLAYERS, data));
    }

    public void notifyDistCards(Players players) {
        for (int i = 0; i < 3; i++) {
            Player p = players.ofIndex(i);
            WebSocket conn = p.getConnection();
            DistData data = new DistData(p.getId(), p.getCards().getCardsInfo());
            conn.send(msgProcessor.serialize(MessageType.DIST, data));
        }
    }

    public void notifyId(Player p) {
        p.getConnection().send(msgProcessor.serialize(MessageType.ID, new IdData(p.getId())));
    }

    public void notifyDistHiddenCards(Player player, CardCollection hiddenCards) {
        WebSocket conn = player.getConnection();
        DistData data = new DistData(player.getId(), hiddenCards.getCardsInfo());
        conn.send(msgProcessor.serialize(MessageType.DIST_HIDDEN, data));
    }
}
