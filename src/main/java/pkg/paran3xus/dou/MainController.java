package pkg.paran3xus.dou;

import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pkg.paran3xus.dou.Game.CardCollection;
import pkg.paran3xus.dou.Room.Client;
import pkg.paran3xus.dou.Room.Client.ClientCallback;
import pkg.paran3xus.dou.Room.Server;
import pkg.paran3xus.dou.Room.Server.ServerCallback;
import pkg.paran3xus.dou.Room.Utils.RoomState;
import pkg.paran3xus.dou.controls.CardSelector;
import pkg.paran3xus.dou.controls.PlayerInfoPane;
import pkg.paran3xus.dou.Room.Network.RoomScanner;
import pkg.paran3xus.dou.Room.Network.RoomScanner.ScanCallback;
import pkg.paran3xus.dou.Room.Player.Player;
import pkg.paran3xus.dou.Room.Player.PlayerInfo;
import pkg.paran3xus.dou.Room.Player.Players;

public class MainController implements Initializable {
    @FXML
    private Parent root;

    @FXML
    private Label hiddenCardsLabel;

    @FXML
    private VBox controlPanel;

    @FXML
    private TextField nicknameField;

    @FXML
    private ImageView avatarImage;

    @FXML
    private ListView<String> roomList;

    @FXML
    private PlayerInfoPane leftPlayerInfo, rightPlayerInfo, bottomPlayerInfo;

    @FXML
    private CardSelector cardSelector;

    @FXML
    private Button controlButton, controlNotButton;

    private Client client;
    private Server server;
    RoomScanner scanner;

    RoomState state = RoomState.READY;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        server = new Server(new ServerCallback() {
            @Override
            public void onServerStart() {
                connectServer("ws://127.0.0.1:17963");
            }
        });
        scanner = new RoomScanner(32, 100);

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
    }

    @FXML
    protected void onCreateRoomButtonClicked() {
        controlPanel.setVisible(false);
        server.run();
    }

    @FXML
    protected void onJoinRoomButtonClicked() {
        controlPanel.setVisible(false);
        connectServer(roomList.getSelectionModel().getSelectedItem());
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
                client.sendMove(cardSelector.getSelectedCards());
                cardSelector.removeCards(cardSelector.getSelectedCards());
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
                    pane.addCardCount(3);

                    hiddenCardsLabel.setText(col.toString());

                    if (id.equals(client.getMyId())) {
                        cardSelector.addCards(col.getCards());
                    }
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
            public void onPlayerMoving(String id) {
                Platform.runLater(() -> {
                    playerInfoPaneOfId(id).setStatus("Moving");

                    state = RoomState.PLAYING;
                    controlButton.setText("Move");
                    controlNotButton.setText("pass");

                    boolean neq = !id.equals(client.getMyId());
                    controlButton.setDisable(neq);
                    controlNotButton.setDisable(neq);
                });
            }

            @Override
            public void onPlayerMove(String id, CardCollection col) {
                PlayerInfoPane pane = playerInfoPaneOfId(id);
                pane.setStatus(col.toString());
                pane.substractCardCount(col.getCardsCount());
            }

            @Override
            public void onEnd(Boolean isLandlordWin) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'onEnd'");
            }

            @Override
            public void onChat(Player p, String msg) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'onChat'");
            }

            @Override
            public void onError(String errorMessage) {
                System.out.println("client error: " + errorMessage);
            }
        });
        client.connect();
    }
}