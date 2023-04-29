package Modules;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Rectangle;
public class Character {
    private int x;
    private int y;
    private int dx;
    private int dy;
    private int speed;
    private BufferedImage image;

    public Character(int x, int y, int speed, String imagePath) {
        this.x = x;
        this.y = y;
        this.dx = 0;
        this.dy = 0;
        this.speed = speed;

        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            System.err.println("Error loading character image: " + e.getMessage());
        }
    }

    public Character(int x, int y) {
        this(x, y, 0, ""); // Provide default values
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public void move() {
        x += dx;
        y += dy;
    }
    public int getSpeed() {
        return speed;
    }
    public BufferedImage getImage() {
        return image;
    }
    public Rectangle getBounds() {
        return new Rectangle(x - image.getWidth() / 2, y - image.getHeight() / 2, image.getWidth(), image.getHeight());
    }
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
