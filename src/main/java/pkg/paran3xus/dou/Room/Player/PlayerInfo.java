package pkg.paran3xus.dou.Room.Player;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class PlayerInfo {
    public String nickname;
    public String id;
    public String avatar;
    public int pos;
    public boolean isReady;
    public static PlayerInfo blank = new PlayerInfo("", "", "", 0);

    public PlayerInfo(
            String nickname, String id,
            String avatar,
            int pos) {
        this.nickname = nickname;
        this.id = id;
        this.avatar = avatar;
        this.pos = pos;
    }

    public PlayerInfo(String nickname, String id, Image avatar, boolean isReady, int pos) {
        this.nickname = nickname;
        this.id = id;
        this.pos = pos;
        this.isReady = isReady;

        BufferedImage bImage = SwingFXUtils.fromFXImage(avatar, null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bImage, "png", outputStream);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        String b64Img = Base64.getEncoder().encodeToString(outputStream.toByteArray());
        this.avatar = b64Img;
    }

    public String getId() {
        return id;
    }

    public boolean getIsReady() {
        return isReady;
    }

    public String getNickname() {
        return nickname;
    }

    public int getPos() {
        return pos;
    }

    public Image getJFXAvatar() {
        byte[] imageBytes = Base64.getDecoder().decode(avatar);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);

        return new Image(byteArrayInputStream);
    }
}
