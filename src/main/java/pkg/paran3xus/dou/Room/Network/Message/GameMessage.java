package pkg.paran3xus.dou.Room.Network.Message;

import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.image.Image;
import pkg.paran3xus.dou.Game.Card;
import pkg.paran3xus.dou.Game.CardCollection;
import pkg.paran3xus.dou.Room.Player.PlayerInfo;
import pkg.paran3xus.dou.Room.Player.Players;

public interface GameMessage {
    public static class JoinData implements GameMessage {
        private PlayerInfo playerInfo;

        public JoinData(PlayerInfo info) {
            this.playerInfo = info;
        }

        public String getId() {
            return playerInfo.getId();
        }

        public String getNickname() {
            return playerInfo.getNickname();
        }

        public Image getJFXAvatar() {
            return playerInfo.getJFXAvatar();
        }
    }

    public static class PlayersData implements GameMessage {
        private List<PlayerInfo> players;

        public PlayersData(Players pls) {
            players = pls.getPlayers()
                    .stream()
                    .map(x -> {
                        if (x == null) {
                            return PlayerInfo.blank;
                        } else {
                            return x.toPlayerInfo(pls.indexOf(x));
                        }
                    })
                    .collect(Collectors.toList());
        }

        public List<PlayerInfo> getPlayers() {
            return players;
        }
    }

    public static class IdData implements GameMessage {
        private String id;

        public IdData(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    public static class LeaveData extends IdData {
        public LeaveData(String id) {
            super(id);
        }
    }

    public static class AskBidData extends IdData {
        public AskBidData(String id) {
            super(id);
        }
    }

    public static class AskMoveData extends IdData {
        public AskMoveData(String id) {
            super(id);
        }
    }

    public static class ReadyData extends IdData {
        public ReadyData(String id) {
            super(id);
        }
    }

    public static class ChatData extends IdData {
        private String msg;

        public ChatData(String id, String msg) {
            super(id);
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }
    }

    public static class EndData implements GameMessage {
        private Boolean isLandlordWin;

        public EndData(boolean isLandlordWin) {
            this.isLandlordWin = isLandlordWin;
        }

        public boolean getIsLandlordWin() {
            return isLandlordWin;
        }
    }

    public static class MoveData extends IdData {
        private List<Card.CardInfo> cards;

        public MoveData(String id, List<Card.CardInfo> cards) {
            super(id);
            this.cards = cards;
        }

        public CardCollection getCardCollection() {
            return new CardCollection(cards.stream().map(x -> x.toCard()).collect(Collectors.toList()));
        }

        public List<Card.CardInfo> getCardInfo() {
            return cards;
        }
    }

    public static class DistData extends MoveData {
        public DistData(String id, List<Card.CardInfo> cards) {
            super(id, cards);
        }
    }

    public static class DistHiddenData extends DistData {
        public DistHiddenData(String id, List<Card.CardInfo> cards) {
            super(id, cards);
        }
    }

    public static class BidData implements GameMessage {
        private Boolean bid;
        private String id;

        public BidData(String id, boolean bid) {
            this.bid = bid;
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public Boolean getBid() {
            return bid;
        }
    }
}
