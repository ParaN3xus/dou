package pkg.paran3xus.dou.Room.Network.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

public class MessageProcessor {
    private final Gson gson;

    public MessageProcessor() {
        gson = new GsonBuilder()
                .serializeNulls()
                .create();
    }

    public String serialize(MessageType type, GameMessage data) {
        MessageWrapper<GameMessage> wrapper = new MessageWrapper<>(type, data);
        return gson.toJson(wrapper);
    }

    public GameMessage deserialize(String json) {
        MessageWrapper<?> wrapper = gson.fromJson(json, MessageWrapper.class);
        JsonElement dataElement = gson.toJsonTree(wrapper.getData());
        return gson.fromJson(dataElement, wrapper.getType().getDataClass());
    }
}
