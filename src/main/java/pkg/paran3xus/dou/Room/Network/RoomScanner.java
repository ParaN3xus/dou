package pkg.paran3xus.dou.Room.Network;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.List;
import java.util.concurrent.*;

public class RoomScanner {
    private final ExecutorService executor;
    private final int timeoutMs;
    private volatile boolean isScanning = false;

    public interface ScanCallback {
        void onServerFound(String url);

        void onScanProgress(int scanned, int total);

        void onScanComplete();

        void onError(String url, Exception e);
    }

    public RoomScanner(int threadPoolSize, int timeoutMs) {
        this.executor = Executors.newFixedThreadPool(threadPoolSize);
        this.timeoutMs = timeoutMs;
    }

    public void scanLocalNetwork(int port, ScanCallback callback) {
        List<NetworkUtils.NetworkInfo> networks = NetworkUtils.getLocalNetworkIPs();
        if (networks.isEmpty()) {
            callback.onError("No valid local network found", new Exception("No valid local network found"));
            callback.onScanComplete();
            return;
        }

        NetworkUtils.NetworkInfo networkInfo = networks.get(0);
        System.out.println("Scanning network: " + networkInfo.baseIP + " (Local IP: " + networkInfo.ip + ")");
        startScan(networkInfo.baseIP, 1, 254, port, callback);
    }

    public static List<NetworkUtils.NetworkInfo> getAvailableNetworks() {
        return NetworkUtils.getLocalNetworkIPs();
    }

    public void startScan(String baseIP, int startIP, int endIP, int port, ScanCallback callback) {
        if (isScanning) {
            return;
        }
        isScanning = true;

        CompletableFuture.runAsync(() -> {
            int total = endIP - startIP + 1;
            int scanned = 0;

            for (int i = startIP; i <= endIP && isScanning; i++) {
                String ip = baseIP + i;
                String wsUrl = "ws://" + ip + ":" + port;

                try {
                    boolean isAvailable = scanAddress(wsUrl);
                    if (isAvailable) {
                        callback.onServerFound(wsUrl);
                    }
                    System.out.println("unavail:" + wsUrl);
                } catch (Exception e) {
                    callback.onError(wsUrl, e);
                }

                scanned++;
                callback.onScanProgress(scanned, total);
            }

            callback.onScanComplete();
            isScanning = false;
        }, executor);
    }

    private boolean scanAddress(String wsUrl) throws Exception {
        CompletableFuture<Boolean> connectionFuture = new CompletableFuture<>();

        WebSocketClient client = new WebSocketClient(new URI(wsUrl)) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                connectionFuture.complete(true);
                this.close();
            }

            @Override
            public void onMessage(String message) {
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                if (!connectionFuture.isDone()) {
                    connectionFuture.complete(false);
                }
            }

            @Override
            public void onError(Exception ex) {
                if (!connectionFuture.isDone()) {
                    connectionFuture.complete(false);
                }
            }
        };

        client.connectBlocking(timeoutMs, TimeUnit.MILLISECONDS);
        return connectionFuture.get(timeoutMs, TimeUnit.MILLISECONDS);
    }

    public void stopScan() {
        isScanning = false;
    }

    public void shutdown() {
        stopScan();
        executor.shutdown();
        try {
            if (!executor.awaitTermination(timeoutMs, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
}
