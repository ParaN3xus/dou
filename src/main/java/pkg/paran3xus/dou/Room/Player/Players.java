package pkg.paran3xus.dou.Room.Player;

import java.util.List;
import java.util.stream.Stream;

import org.java_websocket.WebSocket;

import pkg.paran3xus.dou.Game.CardCollection;

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

    public int countReady() {
        return (int) Stream.of(first, second, third)
                .filter(p -> p.getReady())
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

    public Player ofId(String id) {
        return Stream.of(first, second, third)
                .filter(p -> p != null && p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Player ofIndex(int index) {
        switch (index) {
            case 0:
                return first;
            case 1:
                return second;
            default:
                return third;
        }
    }

    public int indexOf(Player player) {
        if (first == player) {
            return 0;
        } else if (second == player) {
            return 1;
        } else {
            return 2;
        }
    }

    public void removePlayer(Player player) {
        if (first == player) {
            first = null;
        } else if (second == player) {
            second = null;
        } else if (third == player) {
            third = null;
        }
    }

    public void setCards(List<CardCollection> cards) {
        first.setCards(cards.get(0));
        second.setCards(cards.get(1));
        third.setCards(cards.get(2));
    }

    public List<Player> getPlayers() {
        return Stream.of(first, second, third).toList();
    }
}