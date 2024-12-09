package pkg.paran3xus.dou.Room;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.java_websocket.WebSocket;

import pkg.paran3xus.dou.Game.*;
import pkg.paran3xus.dou.Room.Utils.RoomState;
import pkg.paran3xus.dou.Room.Network.WSServer;
import pkg.paran3xus.dou.Room.Network.WSServer.Callback;
import pkg.paran3xus.dou.Room.Player.*;
import pkg.paran3xus.dou.Room.Player.Players.PlayerFullException;
import pkg.paran3xus.dou.Room.Network.Message.GameMessage;
import pkg.paran3xus.dou.Room.Network.Message.GameMessage.*;

public class Server {
    public interface ServerCallback {
        void onServerStart();

    }

    WSServer server;
    ServerCallback callback;

    RoomState state;
    int currentWaiting;
    Players players;
    CardCollection hiddenCards;
    Random random = new Random();

    Boolean[] bidHist;
    int bidRound;
    int firstBid;
    int landlordIndex = -1;

    int lastChallengerIndex;
    CardCollection lastCollection;

    public Server(ServerCallback callback) {
        server = new WSServer(new Callback() {
            @Override
            public void onServerMessage(GameMessage message, WebSocket conn) throws PlayerFullException {
                switch (message) {
                    case JoinData joinData -> handleJoinMessage(joinData, conn);
                    case ReadyData readyData -> handleReadyMessage(readyData);
                    case LeaveData leaveData -> handleLeaveMessage(leaveData);
                    case ChatData chatData -> handleChatMessage(chatData);
                    case MoveData moveData -> handleMoveMessage(moveData);
                    case BidData bidData -> handleBidMessage(bidData);
                    default -> {
                    }
                }
            }

            @Override
            public void onServerStart() {
                callback.onServerStart();
            }
        });
        this.callback = callback;

        state = RoomState.JOINING;
        players = new Players();
    }

    public void run() {
        CompletableFuture.runAsync(() -> {
            server.start();
        });
    }

    public void stop() throws InterruptedException {
        server.stop();
    }

    private void handleJoinMessage(JoinData joinData, WebSocket conn) throws PlayerFullException {
        Player p = new Player(joinData, conn);
        players.addPlayer(p);

        server.notifyId(p);
        server.notifyPlayers(players);

        if (players.count() == 3) {
            state = RoomState.READY;
        }
    }

    private void handleReadyMessage(ReadyData readyData) {
        players.ofId(readyData.getId()).setReady(true);
        server.notifyReady(readyData);

        if (players.countReady() == 3) {
            distCards();
            beginBid();
        }
    }

    private void distCards() {
        state = RoomState.DISTING;
        List<CardCollection> cards = Card.createAndDealCards();
        players.setCards(cards);
        hiddenCards = cards.getLast();

        server.notifyDistCards(players);
    }

    private void beginBid() {
        state = RoomState.BIDDING;
        firstBid = random.nextInt(3);
        currentWaiting = firstBid;
        bidRound = 0;
        landlordIndex = -1;
        bidHist = new Boolean[] { false, false, false };
        server.notifyAskBid(new AskBidData(players.ofIndex(firstBid).getId()));
    }

    private void beginMove() {
        System.out.println("server: begin move! game start!");
        state = RoomState.PLAYING;
        server.notifyDistHiddenCards(players.ofIndex(landlordIndex), hiddenCards);
        server.notifyAskMove(new AskMoveData(players.ofIndex(landlordIndex).getId(), false));
        lastChallengerIndex = landlordIndex;
    }

    private void handleLeaveMessage(LeaveData leaveData) {
        players.removePlayer(players.ofId(leaveData.getId()));
        state = RoomState.JOINING;

        server.notifyLeave(leaveData);
    }

    private void end() {
    }

    private void handleBidMessage(BidData bidData) {
        System.out.println("server: bid " + bidData.getId() + " " + bidData.getBid());
        bidHelper(bidData);
        if (landlordIndex != -1) {
            currentWaiting = landlordIndex;
            beginMove();
        }
    }

    private void bidHelper(BidData bidData) {
        bidHist[currentWaiting] = bidData.getBid();
        server.notifyBid(bidData);

        nextWaiting();

        int bidCount = Arrays.asList(bidHist).stream().collect(Collectors.summingInt(x -> x ? 1 : 0));
        if (currentWaiting == firstBid) {
            bidRound++;

            if (bidCount == 0) {
                distCards();
                beginBid();
                return;
            }
            if (bidCount == 1) {
                int i = 0;
                while (!bidHist[i]) {
                    i++;
                }
                landlordIndex = i;
                return;
            }

            // bid count >= 2
            while (!bidHist[currentWaiting]) {
                nextWaiting();
            }
            server.notifyAskBid(new AskBidData(players.ofIndex(currentWaiting).getId()));
            return;
        }

        if (bidRound != 0) {
            // does bid
            if (bidData.getBid()) {
                landlordIndex = players.indexOf(players.ofId(bidData.getId()));
                return;
            }

            // doesn't
            bidHist[currentWaiting] = false;
            if (bidCount == 2) {
                int i = 0;
                while (!bidHist[i]) {
                    i++;
                }
                landlordIndex = i;
                return;
            }

            // bidCount == 3
        }

        // bidRound == 0
        server.notifyAskBid(new AskBidData(players.ofIndex(currentWaiting).getId()));
    }

    private void handleMoveMessage(MoveData moveData) {
        System.out.println("server: move received from " + moveData.getId());
        if (!moveData.getCardInfo().isEmpty()) {
            CardCollection col = moveData.getCardCollection();
            players.ofIndex(currentWaiting).move(col);

            if (players.ofIndex(currentWaiting).getCards().isEmpty()) {
                end();
                server.notifyEnd(new EndData(currentWaiting == landlordIndex));
            }

            lastChallengerIndex = currentWaiting;
            lastCollection = col;
        }

        server.notifyMove(moveData);

        nextWaiting();
        server.notifyAskMove(
                new AskMoveData(players.ofIndex(currentWaiting).getId(), lastChallengerIndex != currentWaiting));
    }

    private void handleChatMessage(ChatData chatData) {
        server.notifyChat(chatData);
    }

    public void nextWaiting() {
        currentWaiting++;
        currentWaiting %= 3;
    }
}
