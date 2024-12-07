package pkg.paran3xus.dou.Room;

import java.util.ArrayList;
import java.util.List;

import org.java_websocket.WebSocket;

import pkg.paran3xus.dou.Room.Network.RoomServer;
import pkg.paran3xus.dou.Room.Network.RoomServer.Callback;
import pkg.paran3xus.dou.Room.Player.Player;
import pkg.paran3xus.dou.Room.Player.Players;
import pkg.paran3xus.dou.Room.Player.Players.PlayerFullException;
import pkg.paran3xus.dou.Room.Network.Message.GameMessage;
import pkg.paran3xus.dou.Room.Network.Message.MessageProcessor;
import pkg.paran3xus.dou.Room.Network.Message.GameMessage.*;

public class LocalRoom extends Room {
    RoomServer server;

    public LocalRoom() {
        server = new RoomServer(new Callback() {
            @Override
            public void onServerEvent(GameMessage message, WebSocket conn) throws PlayerFullException {
                switch (message) {
                    case JoinData joinData -> handleJoinMessage(joinData, conn);
                    case ReadyData readyData -> handleReadyMessage(readyData, conn);
                    case LeaveData leaveData -> handleLeaveMessage(leaveData, conn);
                    case ChatData chatData -> handleChatMessage(chatData, conn);
                    case MoveData moveData -> handleMoveMessage(moveData, conn);
                    case DistData distData -> handleDistMessage(distData, conn);
                    case BidData bidData -> handleBidMessage(bidData, conn);
                    default -> {
                    }
                }
            }

        });
        state = RoomState.JOINING;
        players = new Players();
    }

    protected void handleJoinMessage(JoinData joinData, WebSocket conn) throws PlayerFullException {
        playerJoin(new Player(joinData, conn));
    }

    protected void handleReadyMessage(ReadyData readyData, WebSocket conn) {
        playerReady(players.ofConnection(conn));
    }

    protected void handleLeaveMessage(LeaveData leaveData, WebSocket conn) {
        playerLeave(players.ofConnection(conn));
    }

    protected void handleBidMessage(BidData bidData, WebSocket conn) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleBidMessage'");
    }

    protected void handleDistMessage(DistData distData, WebSocket conn) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleDistMessage'");
    }

    protected void handleMoveMessage(MoveData moveData, WebSocket conn) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleMoveMessage'");
    }

    protected void handleChatMessage(ChatData chatData, WebSocket conn) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleChatMessage'");
    }

}
