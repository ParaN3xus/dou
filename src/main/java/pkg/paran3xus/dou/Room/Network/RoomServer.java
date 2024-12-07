package pkg.paran3xus.dou.Room.Network;

import java.net.InetSocketAddress;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import pkg.paran3xus.dou.Room.Network.Message.GameMessage;
import pkg.paran3xus.dou.Room.Network.Message.MessageProcessor;
import pkg.paran3xus.dou.Room.Network.Message.MessageType;
import pkg.paran3xus.dou.Room.Network.Message.GameMessage.JoinData;
import pkg.paran3xus.dou.Room.Player.Players.PlayerFullException;

public class RoomServer extends WebSocketServer {
    public interface Callback {
        void onServerEvent(GameMessage message, WebSocket conn) throws PlayerFullException;
    }

    public Callback callback;

    public RoomServer(Callback callback) {
        super(new InetSocketAddress(17963));
        this.callback = callback;
        this.start();
    }

    private static Set<WebSocket> connections = new CopyOnWriteArraySet<>();

    private MessageProcessor msgProcessor = new MessageProcessor();

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        connections.add(conn);
        System.out.println("New connection: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        connections.remove(conn);
        System.out.println("Closed connection: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        GameMessage msg = msgProcessor.deserialize(message);
        try {
            callback.onServerEvent(msg, conn);
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

    public void notifyJoin(JoinData data) {
        broadcast(msgProcessor.serialize(MessageType.JOIN, data));
    }
}
