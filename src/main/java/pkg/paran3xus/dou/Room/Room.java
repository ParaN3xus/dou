package pkg.paran3xus.dou.Room;

import java.util.List;
import java.util.Random;

import pkg.paran3xus.dou.Game.Card;
import pkg.paran3xus.dou.Game.CardCollection;
import pkg.paran3xus.dou.Room.Player.Player;
import pkg.paran3xus.dou.Room.Player.Players;
import pkg.paran3xus.dou.Room.Player.Players.PlayerFullException;

public class Room {
    public enum RoomState {
        JOINING, // waiting for joining
        READY, // waiting for getting ready
        INITING, // dist cards
        BIDDING, // bid
        PLAYING, // play
    }

    public enum PlayingState {
        WAITING_0(0),
        WAITING_1(1),
        WAITING_2(2);

        private int value;

        PlayingState(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void next() {
            value++;
            value %= 3;
        }

        public static PlayingState fromInt(int value) {
            switch (value) {
                case 0:
                    return WAITING_0;
                case 1:
                    return WAITING_1;
                default:
                    return WAITING_2;
            }
        }
    }

    interface RoomCallback {
        void onPlayerChanged(Players players);

        void onRoomStateChanged(RoomState state);

        void onError(String errorMessage);
    }

    RoomCallback callback;
    RoomState state;
    PlayingState playingState;
    Players players;
    CardCollection hiddenCards;
    Random random = new Random();

    public void playerJoin(Player player) throws PlayerFullException {
        players.addPlayer(player);
        if (players.count() == 3) {
            state = RoomState.READY;
        }
        callback.onPlayerChanged(players);
    }

    protected void playerLeave(Player player) {
        players.removePlayer(player);
        state = RoomState.JOINING;
        callback.onPlayerChanged(players);
    }

    protected void playerReady(Player player) {
        player.setReady(true);
        if (players.countReady() == 3) {
            state = RoomState.INITING;
        }
        callback.onPlayerChanged(players);
    }

    protected void playerBid(Player player) {

    }

    protected void init() {
        List<CardCollection> cards = Card.createAndDealCards();
        players.setCards(cards);
        hiddenCards = cards.getLast();

        state = RoomState.BIDDING;
        playingState = PlayingState.fromInt(random.nextInt(3));
    }

    public void setCallback(RoomCallback callback) {
        this.callback = callback;
    }

    public RoomCallback getCallback() {
        return callback;
    }
}
