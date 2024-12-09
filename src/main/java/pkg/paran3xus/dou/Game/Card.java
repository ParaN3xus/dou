package pkg.paran3xus.dou.Game;

import javafx.scene.paint.Color;

import java.util.*;
import java.util.stream.Collectors;

public class Card implements Comparable<Card> {
    public enum Suit {
        SPADE, HEART, CLUB, DIAMOND, JOKER
    }

    public enum Value {
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

        public static Value fromInt(int value) {
            for (Value v : values()) {
                if (v.getValue() == value) {
                    return v;
                }
            }
            throw new IllegalArgumentException("Invalid value: " + value);
        }
    }

    public class CardInfo {
        // 0: spade, 1: heart, 2: club, 3: diamond, 4: joker
        public int suit;

        public int value;

        CardInfo(int suit, int value) {
            this.suit = suit;
            this.value = value;
        }

        public Card toCard() {
            Suit suit;
            switch (this.suit) {
                case 0:
                    suit = Suit.SPADE;
                    break;
                case 1:
                    suit = Suit.HEART;
                    break;
                case 2:
                    suit = Suit.CLUB;
                    break;
                case 3:
                    suit = Suit.DIAMOND;
                    break;
                default:
                    suit = Suit.JOKER;
            }
            return new Card(suit, Value.fromInt(this.value));
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

    public CardInfo GetCardInfo() {
        int suitValue = -1;
        int value = ' ';

        switch (this.suit) {
            case SPADE:
                suitValue = 0;
                break;
            case HEART:
                suitValue = 1;
                break;
            case CLUB:
                suitValue = 2;
                break;
            case DIAMOND:
                suitValue = 3;
                break;
            case JOKER:
                suitValue = 4;
                break;
        }

        value = this.value.getValue();

        return new CardInfo(suitValue, value);
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

    public static List<CardCollection> createAndDealCards() {
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

        return Arrays.stream(result)
                .map(x -> new CardCollection(Arrays.asList(x)))
                .collect(Collectors.toList());
    }

    public String getValueName() {
        int v = value.getValue();
        if (v < 10) {
            return Integer.toString(v);
        }
        if (v == 10) {
            return "10";
        }
        if (v == 11) {
            return "J";
        }
        if (v == 12) {
            return "Q";
        }
        if (v == 13) {
            return "K";
        }
        if (v == 14) {
            return "A";
        }
        if (v == 15) {
            return "2";
        }
        if (v == 16) {
            return "Joker S";
        }
        if (v == 17) {
            return "Joker B";
        }
        return "U";
    }

    public String getSuitSymbol() {
        int s = suit.ordinal();
        if (s == 0) {
            return "♠";

        }
        if (s == 1) {
            return "♥";
        }
        if (s == 2) {
            return "♣";
        }
        if (s == 3) {
            return "♦";
        }
        return "\uD83E\uDD21";
    }

    public Color getColor() {
        if (suit == Suit.SPADE || suit == Suit.CLUB || value == Value.SMALL_JOKER) {
            return Color.BLACK;
        }
        return Color.RED;
    }
}
