package pkg.paran3xus.dou;

import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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

    private Client client;
    private Server server;
    RoomScanner scanner;

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

    protected void connectServer(String uri) {
        System.out.println("connecting to " + uri);
        client = new Client(URI.create(uri), nicknameField.getText(), avatarImage.getImage(), new ClientCallback() {
            @Override
            public void onPlayerChanged(Players players) {
                System.out.println("player changed");
            }

            @Override
            public void onDist(CardCollection col) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'onDist'");
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
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'onError'");
            }
        });
        client.connect();
    }
}