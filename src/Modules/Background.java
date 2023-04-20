package Modules;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Background {
    private BufferedImage image;

    public Background(String imagePath) {
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            System.err.println("Error loading background image: " + e.getMessage());
        }
    }

    public BufferedImage getImage() {
        return image;
    }
}
