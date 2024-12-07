package pkg.paran3xus.dou.Room.Network.Message;

public class MessageWrapper<T> {
    private MessageType type;
    private T data;

    public MessageWrapper(MessageType type, T data) {
        this.type = type;
        this.data = data;
    }

    public MessageType getType() {
        return type;
    }

    public T getData() {
        return data;
    }
}
