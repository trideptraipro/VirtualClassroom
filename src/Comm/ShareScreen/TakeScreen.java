package Comm.ShareScreen;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TakeScreen {
    static Robot rob;

    static {
        try {
            rob = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    ;
    static Dimension d= Toolkit.getDefaultToolkit().getScreenSize();;
    public TakeScreen() throws AWTException {
    }

    public static VideoPacket getScreen() throws IOException {
        BufferedImage img =rob.createScreenCapture(new Rectangle(0, 0, (int) d.getWidth(), (int) d.getHeight()));
        ByteArrayOutputStream ous = new ByteArrayOutputStream();
        ImageIO.write(img, "png", ous);
        ImageIO.setUseCache(false);
        int size=ous.size();
        return new VideoPacket(ous.toByteArray(), size);
    }
}
