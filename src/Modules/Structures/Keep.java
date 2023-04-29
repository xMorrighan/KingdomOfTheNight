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
        int smallerWidth = (int) (image.getWidth() * 0.8); // 80% of the original width
        int smallerHeight = (int) (image.getHeight() * 0.8); // 80% of the original height

        int newX = x - smallerWidth / 2;
        int newY = y - smallerHeight / 2;

        return new Rectangle(newX, newY, smallerWidth, smallerHeight);
    }

}
