// src/Modules/Structure/Keep.java
package Modules.Structures;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Rectangle;
public class Keep {
    private int x;
    private int y;
    private BufferedImage image;

    public Keep(int x, int y, String imagePath) {
        this.x = x;
        this.y = y;

        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            System.err.println("Error loading keep image: " + e.getMessage());
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public BufferedImage getImage() {
        return image;
    }

    public Rectangle getBounds() {
        return new Rectangle(x - image.getWidth() / 2, y - image.getHeight() / 2, image.getWidth(), image.getHeight());
    }
}
