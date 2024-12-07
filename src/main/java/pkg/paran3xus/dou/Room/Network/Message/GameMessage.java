package pkg.paran3xus.dou.Room.Network.Message;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import pkg.paran3xus.dou.Game.Card;
import pkg.paran3xus.dou.Game.CardCollection;

public interface GameMessage {
    public static class JoinData implements GameMessage {
        private String id;
        private String nickname;
        private byte[] avatar;

        public JoinData(String id, String nickname, byte[] avatar) {
            this.id = id;
            this.nickname = nickname;
            this.avatar = avatar;
        }

        public String getId() {
            return id;
        }

        public String getNickname() {
            return nickname;
        }

        public byte[] getAvatar() {
            return avatar;
        }
    }

    public static class LeaveData implements GameMessage {
        private String id;

        public LeaveData(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    public static class AskBidData implements GameMessage {
        private String id;

        public AskBidData(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    public static class AskMoveData implements GameMessage {
        private String id;

        public AskMoveData(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    public static class ReadyData implements GameMessage {
        private String id;

        public ReadyData(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    public static class ChatData implements GameMessage {
        private String msg;
        private String id;

        public ChatData(String id, String msg) {
            this.msg = msg;
            this.id = id;
        }
    }

    public static class EndData implements GameMessage {
        private Boolean isLandlordWin;

        public EndData(boolean isLandlordWin) {
            this.isLandlordWin = isLandlordWin;
        }
    }

    public static class MoveData implements GameMessage {
        private List<Card.CardInfo> cards;
        private String id;

        public MoveData(String id, List<Card.CardInfo> cards) {
            this.cards = cards;
            this.id = id;
        }

        public CardCollection getCardCollection() {
            return new CardCollection(cards.stream().map(x -> x.toCard()).collect(Collectors.toList()));
        }

        public List<Card.CardInfo> getCardInfo() {
            return cards;
        }
    }

    public static class DistData implements GameMessage {
        private List<Card.CardInfo> cards;

        public DistData(List<Card.CardInfo> cards) {
            this.cards = cards;
        }
        // getters and setters
    }

    public static class DistHiddenData implements GameMessage {
        private List<Card.CardInfo> cards;

        public DistHiddenData(List<Card.CardInfo> cards) {
            this.cards = cards;
        }
        // getters and setters
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
