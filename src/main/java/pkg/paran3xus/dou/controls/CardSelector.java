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

            updateCardDisplay();

            setOnMouseClicked(event -> {
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
    }

    private void handleMouseDragged(MouseEvent event) {
        isDragging = true;
        double currentX = event.getX();
        double minX = Math.min(startX, currentX);
        double maxX = Math.max(startX, currentX);

        for (CardView cardView : cardViews) {
            if (cardView.getLayoutX() >= minX && cardView.getLayoutX() <= maxX) {
                cardView.selected = !cardView.selected;
                cardView.setTranslateY(cardView.selected ? -20 : 0);
            }
        }
    }

    private void handleMouseReleased(MouseEvent event) {
        if (!isDragging) {
            double x = event.getX();
            cardViews.stream()
                    .filter(cv -> x >= cv.getLayoutX() &&
                            x <= cv.getLayoutX() + cv.getPrefWidth())
                    .findFirst()
                    .ifPresent(CardView::toggleSelect);
        }
        isDragging = false;
    }
}
