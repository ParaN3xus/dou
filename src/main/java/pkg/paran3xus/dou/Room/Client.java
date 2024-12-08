package pkg.paran3xus.dou.Room;

import java.net.URI;

import javafx.scene.image.Image;
import pkg.paran3xus.dou.Game.CardCollection;
import pkg.paran3xus.dou.Room.Network.WSClient;
import pkg.paran3xus.dou.Room.Network.WSClient.Callback;
import pkg.paran3xus.dou.Room.Network.Message.GameMessage;
import pkg.paran3xus.dou.Room.Network.Message.GameMessage.*;
import pkg.paran3xus.dou.Room.Player.Players;
import pkg.paran3xus.dou.Room.Player.Players.PlayerFullException;

public class Client {

    interface ClientCallback {
        void onPlayerChanged(Players players);

        void onGameStart();

        void onPlayerBid(int index, boolean isBid);

        void onError(String errorMessage);
    }

    ClientCallback callback;
    Players players;
    CardCollection hiddenCards;
    WSClient client;

    public Client(URI uri, String nickname, Image avatar, ClientCallback callback) {
        client = new WSClient(uri, nickname, avatar, new Callback() {
            @Override
            public void onClientMessage(GameMessage message) throws PlayerFullException {
                switch (message) {
                    case PlayersData playersData -> handlePlayersMessage(playersData);
                    case ReadyData readyData -> handleReadyMessage(readyData);
                    case LeaveData leaveData -> handleLeaveMessage(leaveData);
                    case ChatData chatData -> handleChatMessage(chatData);
                    case MoveData moveData -> handleMoveMessage(moveData);
                    case DistData distData -> handleDistMessage(distData);
                    case BidData bidData -> handleBidMessage(bidData);
                    case AskMoveData askMoveData -> handleAskMoveMessage(askMoveData);
                    case AskBidData askBidData -> handleAskBidMessage(askBidData);
                    case EndData endData -> handleEndMessage(endData);
                    default -> {
                    }
                }
            }
        });
        this.callback = callback;
    }

    protected Object handlePlayersMessage(PlayersData playersData) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handlePlayersMessage'");
    }

    protected void handleEndMessage(EndData endData) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleEndMessage'");
    }

    protected void handleAskBidMessage(AskBidData askBidData) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleAskBidMessage'");
    }

    protected void handleAskMoveMessage(AskMoveData askMoveData) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleAskMoveMessage'");
    }

    protected void handleBidMessage(BidData bidData) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleBidMessage'");
    }

    protected void handleDistMessage(DistData distData) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleDistMessage'");
    }

    protected void handleMoveMessage(MoveData moveData) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleMoveMessage'");
    }

    protected void handleChatMessage(ChatData chatData) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleChatMessage'");
    }

    protected void handleLeaveMessage(LeaveData leaveData) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleLeaveMessage'");
    }

    protected void handleReadyMessage(ReadyData readyData) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleReadyMessage'");
    }

    protected void handleJoinMessage(JoinData joinData) {
    }
}
