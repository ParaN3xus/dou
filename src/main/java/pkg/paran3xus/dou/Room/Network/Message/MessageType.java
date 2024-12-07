package pkg.paran3xus.dou.Room.Network.Message;

import pkg.paran3xus.dou.Room.Network.Message.GameMessage.*;

public enum MessageType {
    JOIN(JoinData.class),
    READY(ReadyData.class),
    LEAVE(LeaveData.class),
    CHAT(ChatData.class),
    MOVE(MoveData.class),
    ASK_MOVE(AskMoveData.class),
    DIST(DistData.class),
    DIST_HIDDEN(DistData.class),
    BID(BidData.class),
    ASK_BID(AskBidData.class),
    END(EndData.class);

    private final Class<? extends GameMessage> dataClass;

    MessageType(Class<? extends GameMessage> dataClass) {
        this.dataClass = dataClass;
    }

    public Class<? extends GameMessage> getDataClass() {
        return dataClass;
    }
}
