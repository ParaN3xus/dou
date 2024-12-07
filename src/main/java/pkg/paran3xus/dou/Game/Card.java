package pkg.paran3xus.dou.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

class Card implements Comparable<Card> {
    enum Suit {
        SPADE, HEART, CLUB, DIAMOND, JOKER
    }

    enum Value {
        THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10),
        JACK(11), QUEEN(12), KING(13), ACE(14), TWO(15),
        SMALL_JOKER(16), BIG_JOKER(17);

        private final int value;

        Value(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private final Suit suit;
    private final Value value;

    public Card(Suit suit, Value value) {
        if (suit == Suit.JOKER && value != Value.SMALL_JOKER && value != Value.BIG_JOKER) {
            throw new IllegalArgumentException("Joker suit can only be used with joker values");
        }
        if (suit != Suit.JOKER && (value == Value.SMALL_JOKER || value == Value.BIG_JOKER)) {
            throw new IllegalArgumentException("Joker values can only be used with joker suit");
        }
        this.suit = suit;
        this.value = value;
    }

    public static Card smallJoker() {
        return new Card(Suit.JOKER, Value.SMALL_JOKER);
    }

    public static Card bigJoker() {
        return new Card(Suit.JOKER, Value.BIG_JOKER);
    }

    public Suit getSuit() {
        return suit;
    }

    public Value getValue() {
        return value;
    }

    public int getNumericValue() {
        return value.getValue();
    }

    @Override
    public int compareTo(Card other) {
        return this.value.getValue() - other.value.getValue();
    }

    @Override
    public String toString() {
        if (suit == Suit.JOKER) {
            return value == Value.SMALL_JOKER ? "Small Joker" : "Big Joker";
        }
        return suit + "-" + value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Card card = (Card) o;
        return suit == card.suit && value == card.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(suit, value);
    }

    public static Card[][] createAndDealCards() {
        List<Card> deck = new ArrayList<>();

        for (Suit suit : new Suit[] { Suit.SPADE, Suit.HEART, Suit.CLUB, Suit.DIAMOND }) {
            for (Value value : Value.values()) {
                if (value != Value.SMALL_JOKER && value != Value.BIG_JOKER) {
                    deck.add(new Card(suit, value));
                }
            }
        }

        deck.add(smallJoker());
        deck.add(bigJoker());

        Collections.shuffle(deck);

        Card[][] result = new Card[4][];
        result[0] = new Card[17];
        result[1] = new Card[17];
        result[2] = new Card[17];
        result[3] = new Card[3];

        for (int i = 0; i < 51; i++) {
            result[i % 3][i / 3] = deck.get(i);
        }

        for (int i = 0; i < 3; i++) {
            result[3][i] = deck.get(51 + i);
        }

        for (int i = 0; i < 4; i++) {
            Arrays.sort(result[i]);
        }

        return result;
    }
}
