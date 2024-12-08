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
import pkg.paran3xus.dou.Room.Player.Players;

public class MainController implements Initializable {
    @FXML
    private Parent root;

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
    private Button controlButton;

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
                break;

            default:
                break;
        }
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
                cardSelector.updateCards(col.getCards());
            }

            @Override
            public void onDistHidden(String id, CardCollection col) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'onDistHidden'");
            }

            @Override
            public void onPlayerBid(String id, boolean isBid) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'onPlayerBid'");
            }

            @Override
            public void onPlayerBidding(String id) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'onPlayerBidding'");
            }

            @Override
            public void onPlayerMoving(String id) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'onPlayerMoving'");
            }

            @Override
            public void onPlayerMove(String id, CardCollection col) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'onPlayerMove'");
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