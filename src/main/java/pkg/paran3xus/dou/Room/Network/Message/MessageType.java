package pkg.paran3xus.dou.Room.Network.Message;

import pkg.paran3xus.dou.Room.Network.Message.GameMessage.*;

public enum MessageType {
    JOIN(JoinData.class),
    ID(IdData.class),
    PLAYERS(PlayersData.class),
    READY(ReadyData.class),
    CHAT(ChatData.class),
    ASK_BID(AskBidData.class),
    BID(BidData.class),
    ASK_MOVE(AskMoveData.class),
    MOVE(MoveData.class),
    DIST(DistData.class),
    DIST_HIDDEN(DistData.class),
    END(EndData.class),
    LEAVE(LeaveData.class);

    private final Class<? extends GameMessage> dataClass;

    MessageType(Class<? extends GameMessage> dataClass) {
        this.dataClass = dataClass;
    }

    public Class<? extends GameMessage> getDataClass() {
        return dataClass;
    }
}
