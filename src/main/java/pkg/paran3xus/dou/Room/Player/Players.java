package pkg.paran3xus.dou.Room.Player;

import java.util.stream.Stream;

import org.java_websocket.WebSocket;

public class Players {
    public class PlayerFullException extends Exception {
        public PlayerFullException() {
            super("Player full");
        }
    }

    private Player first, second, third;

    public Players() {
        first = second = third = null;
    }

    public int count() {
        return (int) Stream.of(first, second, third)
                .filter(p -> p != null)
                .count();
    }

    public void addPlayer(Player player) throws PlayerFullException {
        if (first == null) {
            first = player;
        } else if (second == null) {
            second = player;
        } else if (third == null) {
            third = player;
        } else {
            throw new PlayerFullException();
        }
    }

    public Player ofConnection(WebSocket conn) {
        return Stream.of(first, second, third)
                .filter(p -> p != null && p.getConnection() == conn)
                .findFirst()
                .orElse(null);
    }
}