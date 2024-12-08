package pkg.paran3xus.dou.Room.Network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

public class NetworkUtils {
    private static final Pattern IP_PATTERN = Pattern.compile(
            "^((10\\.((1\\d{2})|(2[0-4]\\d)|(25[0-5])|([1-9]\\d)|\\d)\\.)|" +
                    "(172\\.(1[6-9]|2\\d|3[01])\\.)|" +
                    "(192\\.168\\.))((1\\d{2})|(2[0-4]\\d)|(25[0-5])|([1-9]\\d)|\\d)\\.((1\\d{2})|(2[0-4]\\d)|(25[0-5])|([1-9]\\d)|\\d)$");

    public static class NetworkInfo {
        public final String ip;
        public final String baseIP;

        public NetworkInfo(String ip, String baseIP) {
            this.ip = ip;
            this.baseIP = baseIP;
        }
    }

    public static List<NetworkInfo> getLocalNetworkIPs() {
        List<NetworkInfo> networkInfos = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (!networkInterface.isUp() || networkInterface.isLoopback()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr.getHostAddress().contains(":")) {
                        continue;
                    }

                    String ip = addr.getHostAddress();
                    if (IP_PATTERN.matcher(ip).matches()) {
                        String baseIP = ip.substring(0, ip.lastIndexOf(".") + 1);
                        networkInfos.add(new NetworkInfo(ip, baseIP));
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return networkInfos;
    }
}
