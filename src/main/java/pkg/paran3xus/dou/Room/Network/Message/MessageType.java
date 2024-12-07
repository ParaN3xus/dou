package pkg.paran3xus.dou.Room.Network.Message;

import pkg.paran3xus.dou.Room.Network.Message.GameMessage.*;

public enum MessageType {
    JOIN(JoinData.class),
    LEAVE(LeaveData.class),
    CHAT(ChatData.class),
    MOVE(MoveData.class),
    DIST(DistData.class),
    BID(BidData.class);

    private final Class<? extends GameMessage> dataClass;

    MessageType(Class<? extends GameMessage> dataClass) {
        this.dataClass = dataClass;
    }

    public Class<? extends GameMessage> getDataClass() {
        return dataClass;
    }
}
