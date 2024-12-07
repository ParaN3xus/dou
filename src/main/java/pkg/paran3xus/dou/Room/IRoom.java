package pkg.paran3xus.dou.Room;

import pkg.paran3xus.dou.GameEvent;
import pkg.paran3xus.dou.Player;

enum RoomState {
    WAITING, // waiting for joining
    READY, // waiting for getting ready
    PLAYING, // playing
}

public interface IRoom {
    interface RoomCallback {
        void onPlayerJoined(Player player);

        void onPlayerLeft(Player player);

        void onPlayerReady(Player player);

        void onRoomStateChanged(RoomState state);

        void onGameEvent(GameEvent event);

        void onError(String errorMessage);
    }
}
