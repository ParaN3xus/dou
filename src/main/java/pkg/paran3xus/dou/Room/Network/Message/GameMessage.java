package pkg.paran3xus.dou.Room.Network.Message;

public interface GameMessage {
    public static class JoinData implements GameMessage {
        private String id;
        private byte[] avatar;

        public JoinData(String nickname, byte[] avatar) {
            this.id = nickname;
            this.avatar = avatar;
        }

        public String getId() {
            return id;
        }

        public byte[] getAvatar() {
            return avatar;
        }
    }

    public static class LeaveData implements GameMessage {
    }

    public static class ReadyData implements GameMessage {
    }

    public static class ChatData implements GameMessage {
        private String msg;

        public ChatData(String msg) {
            this.msg = msg;
        }
    }

    public static class MoveData implements GameMessage {
        private String cards;

        public MoveData(String cards) {
            this.cards = cards;
        }
        // getters and setters
    }

    public static class DistData implements GameMessage {
        private String cards;

        public DistData(String cards) {
            this.cards = cards;
        }
        // getters and setters
    }

    public static class BidData implements GameMessage {
        private boolean bid;

        public BidData(boolean bid) {
            this.bid = bid;
        }
        // getters and setters
    }
}
