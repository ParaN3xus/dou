package pkg.paran3xus.dou;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pkg.paran3xus.dou.Game.Card;
import pkg.paran3xus.dou.Game.CardCollection;
import pkg.paran3xus.dou.Game.CardCollection.Type;
import pkg.paran3xus.dou.Room.Client;
import pkg.paran3xus.dou.Room.Client.ClientCallback;
import pkg.paran3xus.dou.Room.Server;
import pkg.paran3xus.dou.Room.Server.ServerCallback;
import pkg.paran3xus.dou.Room.Utils.RoomState;
import pkg.paran3xus.dou.controls.*;
import pkg.paran3xus.dou.Room.Network.RoomScanner;
import pkg.paran3xus.dou.Room.Network.RoomScanner.ScanCallback;
import pkg.paran3xus.dou.Room.Player.*;

public class MainController implements Initializable {
    @FXML
    private Parent root;

    @FXML
    private Label hiddenCardsLabel, chatMessagesLabel;

    @FXML
    private VBox controlPanel;

    @FXML
    private TextField nicknameField, serverField, chatInputField;

    @FXML
    private ImageView avatarImage;

    @FXML
    private ListView<String> roomList;

    @FXML
    private PlayerInfoPane leftPlayerInfo, rightPlayerInfo, bottomPlayerInfo;

    @FXML
    private CardSelector cardSelector;

    @FXML
    private ScrollPane chatMessageScrollPane;

    @FXML
    private Button controlButton, controlNotButton;

    private Client client;
    private Server server;
    RoomScanner scanner;

    RoomState state = RoomState.READY;

    boolean skippable;
    CardCollection lastCardCollection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        server = new Server(new ServerCallback() {
            @Override
            public void onServerStart() {
                connectServer("ws://127.0.0.1:17963");
            }
        });
        scanner = new RoomScanner(32, 100);

        roomList.getSelectionModel().selectedItemProperty().addListener(
                (newValue) -> {
                    if (newValue != null) {
                        serverField.setText(roomList.getSelectionModel().getSelectedItem());
                    }
                });

        Platform.runLater(() -> {
            avatarImage.setImage(new Image(MainApplication.class.getResourceAsStream("images/dou.png")));

            Stage stage = (Stage) root.getScene().getWindow();
            stage.setOnCloseRequest(_ -> {
                try {
                    server.stop();
                } catch (Exception e) {

                }
            });
        });

        chatMessagesLabel.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                chatMessageScrollPane.setVvalue(chatMessageScrollPane.getVmax());
            }
        });
    }

    @FXML
    protected void onCreateRoomButtonClicked() {
        controlPanel.setVisible(false);
        server.run();
    }

    @FXML
    protected void onJoinRoomButtonClicked() {
        controlPanel.setVisible(false);
        controlButton.setText("Ready");
        controlButton.setDisable(false);
        controlNotButton.setText("no");
        controlNotButton.setDisable(true);
        state = RoomState.READY;

        connectServer(serverField.getText());
    }

    @FXML
    protected void onRefreshRoomButtonClicked() {
        scanner.scanLocalNetwork(17963, new ScanCallback() {
            @Override
            public void onServerFound(String url) {
                System.out.println("found " + url);
                roomList.getItems().add(url);
            }

            @Override
            public void onScanProgress(int scanned, int total) {
            }

            @Override
            public void onScanComplete() {
            }

            @Override
            public void onError(String url, Exception e) {
            }
        });
    }

    protected void alert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alert");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    protected void onAvatarImageClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose avatar");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image file", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"));

        File selectedFile = fileChooser.showOpenDialog(avatarImage.getScene().getWindow());

        if (selectedFile != null) {
            try {
                String imageUrl = selectedFile.toURI().toURL().toString();
                Image image = new Image(imageUrl);
                avatarImage.setImage(image);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void onControlButtonClicked() {
        switch (state) {
            case RoomState.READY:
                client.sendReady();
                controlButton.setDisable(true);
                break;
            case RoomState.BIDDING:
                client.sendBid(true);
                break;
            case RoomState.PLAYING:
                List<Card> cards = cardSelector.getSelectedCards();
                CardCollection col = new CardCollection(cards);

                if (col.getType() == Type.INVALID || col.getType() == Type.EMPTY) {
                    alert("invalid move!");
                    return;
                }

                if (skippable && !col.canBeat(lastCardCollection)) {
                    alert("can't beat!");
                    return;
                }

                client.sendMove(cards);
                cardSelector.removeCards(cards);
                break;
            default:
                break;
        }
    }

    @FXML
    protected void onControlNotButtonClicked() {
        switch (state) {
            case RoomState.READY:
                break;
            case RoomState.BIDDING:
                client.sendBid(false);
                break;
            case RoomState.PLAYING:
                client.sendMove(null);
                break;
            default:
                break;
        }
    }

    @FXML
    protected void onChatInputAction() {
        client.sendChat(chatInputField.getText());
        chatInputField.setText("");
    }

    protected PlayerInfoPane playerInfoPaneOfId(String id) {
        Players players = client.getPlayers();
        List<Player> po = players.orderedBy(client.getMyId());
        Player p = players.ofId(id);

        for (int i = 0; i < 3; i++) {
            if (p == po.get(i)) {
                switch (i) {
                    case 0:
                        return bottomPlayerInfo;
                    case 1:
                        return rightPlayerInfo;
                    default:
                        return leftPlayerInfo;
                }
            }
        }
        return null;
    }

    protected void connectServer(String uri) {
        System.out.println("connecting to " + uri);
        client = new Client(URI.create(uri), nicknameField.getText(), avatarImage.getImage(), new ClientCallback() {
            @Override
            public void onPlayerChanged(Players players) {
                if (!client.isMeInited()) {
                    System.out.println("client f: player changed but me uninited");
                    return;
                }
                System.out.println("client f: player changed");

                List<Player> po = players.orderedBy(client.getMyId());

                bottomPlayerInfo.setPlayer(po.get(0), state);
                rightPlayerInfo.setPlayer(po.get(1), state);
                leftPlayerInfo.setPlayer(po.get(2), state);
            }

            @Override
            public void onDist(CardCollection col) {
                Platform.runLater(() -> {
                    cardSelector.updateCards(col.getCards());
                });
            }

            @Override
            public void onDistHidden(String id, CardCollection col) {
                Platform.runLater(() -> {
                    PlayerInfoPane pane = playerInfoPaneOfId(id);
                    pane.setStatus("Landlord!");
                    pane.setIdentity(true);
                    pane.addCardCount(3);

                    hiddenCardsLabel.setText(col.toString());

                    if (id.equals(client.getMyId())) {
                        cardSelector.addCards(col.getCards());
                    }

                    for (PlayerInfoPane p : Stream.of(leftPlayerInfo, rightPlayerInfo, bottomPlayerInfo)
                            .collect(Collectors.toList())) {
                        if (p != pane) {
                            p.setIdentity(false);
                        }
                    }
                    ;
                });
            }

            @Override
            public void onPlayerBid(String id, boolean isBid) {
                playerInfoPaneOfId(id).setStatus(isBid ? "Bid!" : "not bid");
            }

            @Override
            public void onPlayerBidding(String id) {
                System.out.println("clientf: bidding " + id);
                Platform.runLater(() -> {
                    playerInfoPaneOfId(id).setStatus("Bidding");
                    state = RoomState.BIDDING;
                    controlButton.setText("Bid");
                    controlNotButton.setText("not Bid");

                    boolean neq = !id.equals(client.getMyId());
                    controlButton.setDisable(neq);
                    controlNotButton.setDisable(neq);
                });
            }

            @Override
            public void onPlayerMoving(String id, boolean skippable) {
                Platform.runLater(() -> {
                    setSkippable(skippable);
                    playerInfoPaneOfId(id).setStatus("Moving");

                    state = RoomState.PLAYING;
                    controlButton.setText("Move");
                    controlNotButton.setText("pass");

                    boolean neq = !id.equals(client.getMyId());
                    controlButton.setDisable(neq);
                    controlNotButton.setDisable(neq || !skippable);
                });
            }

            @Override
            public void onPlayerMove(String id, CardCollection col) {
                PlayerInfoPane pane = playerInfoPaneOfId(id);
                pane.setStatus(col.toString());
                pane.substractCardCount(col.getCardsCount());

                if (!col.isEmpty()) {
                    lastCardCollection = col;
                }
            }

            @Override
            public void onEnd(Boolean isLandlordWin) {
                System.out
                        .println("clientf: " + "Game ended. Winner: " + (isLandlordWin ? "Landlord" : "Farmers") + "!");
                Platform.runLater(() -> {
                    alert("Game ended. Winner: " + (isLandlordWin ? "Landlord" : "Farmers") + "!");

                    controlPanel.setVisible(true);
                    client.stop();
                });
            }

            @Override
            public void onChat(Player p, String msg) {
                Platform.runLater(() -> {
                    chatMessagesLabel.setText(chatMessagesLabel.getText() + p.getNickname() + ": " + msg + "\n");
                });
            }

            @Override
            public void onError(String errorMessage) {
                System.out.println("client error: " + errorMessage);
            }
        });
        client.connect();
    }

    public void setSkippable(boolean skippable) {
        this.skippable = skippable;
    }
}