package pkg.paran3xus.dou.controls;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import pkg.paran3xus.dou.Game.Card;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CardSelector extends Pane {
    private final List<CardView> cardViews = new ArrayList<>();
    private double startX;
    private boolean isDragging = false;
    private List<CardView> cardsInDragRange = new ArrayList<>();

    public CardSelector() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CardSelector.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        setOnMousePressed(this::handleMousePressed);
        setOnMouseDragged(this::handleMouseDragged);
        setOnMouseReleased(this::handleMouseReleased);
    }

    @FXML
    private void initialize() {
    }

    private class CardView extends Pane {
        private final Card card;
        private boolean selected = false;

        public CardView(Card card) {
            this.card = card;
            setPrefSize(80, 120);
            getStyleClass().add("card-view");

            javafx.scene.text.Text text = new javafx.scene.text.Text(card.getValueName());
            text.setX(10);
            text.setY(30);
            text.setStyle("-fx-font-size: 16px;");
            getChildren().add(text);

            updateCardDisplay();

            setOnMouseClicked(_ -> {
                if (!isDragging) {
                    toggleSelect();
                }
            });
        }

        private void updateCardDisplay() {
            setStyle("-fx-background-color: white; -fx-border-color: black;");

        }

        public void toggleSelect() {
            selected = !selected;
            setTranslateY(selected ? -20 : 0);
        }
    }

    public void updateCards(List<Card> cards) {
        getChildren().clear();
        cardViews.clear();

        for (int i = 0; i < cards.size(); i++) {
            CardView cardView = new CardView(cards.get(i));
            cardView.setLayoutX(i * 40);
            cardView.setLayoutY(20);
            cardViews.add(cardView);
            getChildren().add(cardView);
        }
    }

    public void addCards(List<Card> cards) {
        for (Card card : cards) {
            CardView cardView = new CardView(card);
            cardViews.add(cardView);
            getChildren().add(cardView);
        }

        cardViews.sort((cv1, cv2) -> cv2.card.compareTo(cv1.card));

        rearrangeCards();
    }

    public void rearrangeCards() {
        for (int i = 0; i < cardViews.size(); i++) {
            CardView cardView = cardViews.get(i);
            cardView.setLayoutX(i * 40);
            cardView.setLayoutY(20);
        }

        for (int i = cardViews.size() - 1; i >= 0; i--) {
            cardViews.get(i).toFront();
        }
    }

    public List<Card> getSelectedCards() {
        return cardViews.stream()
                .filter(cv -> cv.selected)
                .map(cv -> cv.card)
                .collect(Collectors.toList());
    }

    public void removeCards(List<Card> cardsToRemove) {
        List<Card> remainingCards = cardViews.stream()
                .map(cv -> cv.card)
                .filter(card -> !cardsToRemove.contains(card))
                .collect(Collectors.toList());
        updateCards(remainingCards);
    }

    private void handleMousePressed(MouseEvent event) {
        startX = event.getX();
        isDragging = false;
        cardsInDragRange.clear();
    }

    private void handleMouseDragged(MouseEvent event) {
        isDragging = true;
        double currentX = event.getX();
        double minX = Math.min(startX, currentX);
        double maxX = Math.max(startX, currentX);

        cardsInDragRange.clear();
        for (CardView cardView : cardViews) {
            if (cardView.getLayoutX() >= minX && cardView.getLayoutX() <= maxX) {
                cardsInDragRange.add(cardView);
                cardView.setStyle("-fx-border-color: blue;");
            } else {
                cardView.updateCardDisplay();
            }
        }
    }

    private void handleMouseReleased(MouseEvent event) {
        if (isDragging) {
            for (CardView cardView : cardsInDragRange) {
                cardView.selected = !cardView.selected;
                cardView.setTranslateY(cardView.selected ? -20 : 0);
                cardView.updateCardDisplay();
            }
        } else {
            double x = event.getX();
            ArrayList<CardView> reversedList = new ArrayList<>(cardViews);
            java.util.Collections.reverse(reversedList);
            reversedList.stream()
                    .filter(cv -> x >= cv.getLayoutX() && x <= cv.getLayoutX() + cv.getPrefWidth())
                    .findFirst()
                    .ifPresent(CardView::toggleSelect);
        }
        isDragging = false;
        cardsInDragRange.clear();
    }

}
