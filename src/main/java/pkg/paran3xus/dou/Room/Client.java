package pkg.paran3xus.dou.Room;

import java.net.URI;

import javafx.scene.image.Image;
import pkg.paran3xus.dou.Game.CardCollection;
import pkg.paran3xus.dou.Room.Network.WSClient;
import pkg.paran3xus.dou.Room.Network.WSClient.Callback;
import pkg.paran3xus.dou.Room.Network.Message.GameMessage;
import pkg.paran3xus.dou.Room.Network.Message.GameMessage.*;
import pkg.paran3xus.dou.Room.Player.Player;
import pkg.paran3xus.dou.Room.Player.Players;
import pkg.paran3xus.dou.Room.Player.Players.PlayerFullException;

public class Client {

    interface ClientCallback {
        void onPlayerChanged(Players players);

        void onDist(CardCollection col);

        void onPlayerBid(String id, boolean isBid);

        void onPlayerBidding(String id);

        void onPlayerMoving(String id);

        void onPlayerMove(String id, CardCollection col);

        void onEnd(Boolean isLandlordWin);

        void onChat(Player p, String msg);

        void onError(String errorMessage);
    }

    ClientCallback callback;
    Players players = null;
    Player me = null;
    String tmpId = null;
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
                    case DistData distData -> handleDistMessage(distData);
                    case MoveData moveData -> handleMoveMessage(moveData);
                    case BidData bidData -> handleBidMessage(bidData);
                    case AskMoveData askMoveData -> handleAskMoveMessage(askMoveData);
                    case AskBidData askBidData -> handleAskBidMessage(askBidData);
                    case EndData endData -> handleEndMessage(endData);
                    case IdData idData -> handleIdMessage(idData);
                    default -> {
                    }
                }
            }
        });
        this.callback = callback;
    }

    protected void handleIdMessage(IdData idData) {
        tmpId = idData.getId();
        initMe();
    }

    protected void handlePlayersMessage(PlayersData playersData) {
        players = new Players(playersData);
        initMe();
    }

    protected void initMe() {
        if (players != null && tmpId != null) {
            me = players.ofId(tmpId);
            callback.onPlayerChanged(players);
        }
    }

    protected void handleReadyMessage(ReadyData readyData) {
        players.ofId(readyData.getId()).setReady(true);
        callback.onPlayerChanged(players);
    }

    protected void handleAskBidMessage(AskBidData askBidData) {
        callback.onPlayerBidding(askBidData.getId());
    }

    protected void handleBidMessage(BidData bidData) {
        callback.onPlayerBid(bidData.getId(), bidData.getBid());
    }

    protected void handleAskMoveMessage(AskMoveData askMoveData) {
        callback.onPlayerMoving(askMoveData.getId());
    }

    protected void handleMoveMessage(MoveData moveData) {
        callback.onPlayerMove(moveData.getId(), moveData.getCardCollection());
    }

    protected void handleDistMessage(DistData distData) {
        callback.onDist(distData.getCardCollection());
    }

    protected void handleChatMessage(ChatData chatData) {
        callback.onChat(players.ofId(chatData.getId()), tmpId);
    }

    protected void handleLeaveMessage(LeaveData leaveData) {
        players.removePlayer(players.ofId(leaveData.getId()));
        callback.onPlayerChanged(players);
    }

    protected void handleEndMessage(EndData endData) {
        callback.onEnd(endData.getIsLandlordWin());
    }
}
