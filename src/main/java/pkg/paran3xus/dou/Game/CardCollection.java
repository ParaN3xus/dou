package pkg.paran3xus.dou.Game;

import java.util.*;
import java.util.stream.Collectors;

class CardCollection implements Comparable<CardCollection> {
    enum Type {
        INVALID, // 无效牌型
        SINGLE, // 单牌
        PAIR, // 对子
        THREE, // 三张
        THREE_WITH_ONE, // 三带一
        THREE_WITH_PAIR, // 三带二
        STRAIGHT, // 顺子
        STRAIGHT_PAIR, // 连对
        AIRPLANE, // 飞机
        AIRPLANE_WITH_ONES, // 飞机带翅膀
        AIRPLANE_WITH_PAIRS, // 飞机带翅膀
        FOUR_WITH_TWO, // 四带二
        FOUR_WITH_TWO_PAIRS, // 四带两对
        BOMB, // 炸弹
        ROCKET // 王炸
    }

    private final List<Card> cards;
    private final Type type;
    private final int baseValue; // for comparing

    public CardCollection(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
        Collections.sort(this.cards);
        this.type = determineType();
        this.baseValue = calculateBaseValue();
    }

    private Type determineType() {
        if (cards.isEmpty())
            return Type.INVALID;

        int size = cards.size();

        Map<Card.Value, Integer> valueCounts = new HashMap<>();
        for (Card card : cards) {
            valueCounts.merge(card.getValue(), 1, Integer::sum);
        }

        // rocket
        if (size == 2 &&
                cards.get(0).getValue() == Card.Value.SMALL_JOKER &&
                cards.get(1).getValue() == Card.Value.BIG_JOKER) {
            return Type.ROCKET;
        }

        // single
        if (size == 1) {
            return Type.SINGLE;
        }

        // pair
        if (size == 2 && valueCounts.size() == 1) {
            return Type.PAIR;
        }

        // three
        if (size == 3 && valueCounts.size() == 1) {
            return Type.THREE;
        }

        // bomb
        if (size == 4 && valueCounts.size() == 1) {
            return Type.BOMB;
        }

        // three with one
        if (size == 4 && valueCounts.containsValue(3)) {
            return Type.THREE_WITH_ONE;
        }

        // three with pair
        if (size == 5 && valueCounts.containsValue(3) && valueCounts.containsValue(2)) {
            return Type.THREE_WITH_PAIR;
        }

        // four with two
        if (size == 6 && valueCounts.containsValue(4) && valueCounts.size() == 3) {
            return Type.FOUR_WITH_TWO;
        }

        // four with two pairs
        if (size == 8 && valueCounts.containsValue(4) &&
                valueCounts.values().stream().filter(count -> count == 2).count() == 2) {
            return Type.FOUR_WITH_TWO_PAIRS;
        }

        // straight
        if (size >= 5 && valueCounts.values().stream().allMatch(count -> count == 1)) {
            List<Integer> values = cards.stream()
                    .map(Card::getNumericValue)
                    .sorted()
                    .collect(Collectors.toList());

            boolean isConsecutive = true;
            for (int i = 1; i < values.size(); i++) {
                if (values.get(i) - values.get(i - 1) != 1 || values.get(i) >= Card.Value.TWO.getValue()) {
                    isConsecutive = false;
                    break;
                }
            }
            if (isConsecutive)
                return Type.STRAIGHT;
        }

        // straight pair
        if (size >= 6 && size % 2 == 0 && valueCounts.values().stream().allMatch(count -> count == 2)) {
            List<Integer> values = new ArrayList<>(valueCounts.keySet().stream()
                    .map(Card.Value::getValue)
                    .sorted()
                    .collect(Collectors.toList()));

            boolean isConsecutive = true;
            for (int i = 1; i < values.size(); i++) {
                if (values.get(i) - values.get(i - 1) != 1 || values.get(i) >= Card.Value.TWO.getValue()) {
                    isConsecutive = false;
                    break;
                }
            }
            if (isConsecutive)
                return Type.STRAIGHT_PAIR;
        }

        // airplane
        if (size >= 6) {
            List<Card.Value> threes = valueCounts.entrySet().stream()
                    .filter(e -> e.getValue() == 3)
                    .map(Map.Entry::getKey)
                    .sorted()
                    .collect(Collectors.toList());

            if (threes.size() >= 2) {
                boolean isConsecutive = true;
                for (int i = 1; i < threes.size(); i++) {
                    if (threes.get(i).getValue() - threes.get(i - 1).getValue() != 1 ||
                            threes.get(i).getValue() >= Card.Value.TWO.getValue()) {
                        isConsecutive = false;
                        break;
                    }
                }
                if (isConsecutive && size == threes.size() * 3)
                    return Type.AIRPLANE;
                if (isConsecutive && size == threes.size() * 4)
                    return Type.AIRPLANE_WITH_ONES;
                if (isConsecutive && size == threes.size() * 5)
                    return Type.AIRPLANE_WITH_PAIRS;
            }
        }

        return Type.INVALID;
    }

    private int calculateBaseValue() {
        if (type == Type.INVALID)
            return -1;
        if (type == Type.ROCKET)
            return Integer.MAX_VALUE;
        if (type == Type.BOMB)
            return cards.get(0).getNumericValue();

        Map<Card.Value, Integer> valueCounts = new HashMap<>();
        for (Card card : cards) {
            valueCounts.merge(card.getValue(), 1, Integer::sum);
        }

        switch (type) {
            case SINGLE:
                return cards.get(0).getNumericValue();
            case PAIR:
                return cards.get(0).getNumericValue();
            case THREE:
            case THREE_WITH_ONE:
            case THREE_WITH_PAIR:
                return valueCounts.entrySet().stream()
                        .filter(e -> e.getValue() == 3)
                        .map(e -> e.getKey().getValue())
                        .findFirst()
                        .orElse(-1);
            case STRAIGHT:
            case STRAIGHT_PAIR:
                return cards.get(cards.size() - 1).getNumericValue();
            case AIRPLANE:
            case AIRPLANE_WITH_ONES:
            case AIRPLANE_WITH_PAIRS:
                return valueCounts.entrySet().stream()
                        .filter(e -> e.getValue() == 3)
                        .map(e -> e.getKey().getValue())
                        .max(Integer::compareTo)
                        .orElse(-1);
            case FOUR_WITH_TWO:
            case FOUR_WITH_TWO_PAIRS:
                return valueCounts.entrySet().stream()
                        .filter(e -> e.getValue() == 4)
                        .map(e -> e.getKey().getValue())
                        .findFirst()
                        .orElse(-1);
            default:
                return -1;
        }
    }

    @Override
    public int compareTo(CardCollection other) {
        // 王炸最大
        if (this.type == Type.ROCKET)
            return 1;
        if (other.type == Type.ROCKET)
            return -1;

        // 炸弹比普通牌型大
        if (this.type == Type.BOMB && other.type != Type.BOMB)
            return 1;
        if (other.type == Type.BOMB && this.type != Type.BOMB)
            return -1;

        // 牌型不同无法比较
        if (this.type != other.type)
            return 0;

        // 同类型比较基础值
        return this.baseValue - other.baseValue;
    }

    public boolean canBeat(CardCollection other) {
        int comparison = this.compareTo(other);
        return comparison > 0;
    }

    public Type getType() {
        return type;
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

    @Override
    public String toString() {
        return "CardCollection{" +
                "type=" + type +
                ", cards=" + cards +
                '}';
    }
}
