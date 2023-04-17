import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.geom.AffineTransform;


public class Game extends JPanel implements KeyListener, ActionListener, MouseWheelListener {
    private int x, y;
    private int dx, dy;
    private double cameraX, cameraY;
    private double zoomFactor = 1.0;

    public Game() {
        setPreferredSize(new Dimension(800, 600));
        addKeyListener(this);
        setFocusable(true);
        setDoubleBuffered(true);
        Timer timer = new Timer(1000 / 60, this);
        timer.start();
        addMouseWheelListener(this);

        // Load the background image
        try {
            backgroundImage = ImageIO.read(new File("src/resources/grassy_field_tile.png"));
        } catch (IOException e) {
            System.err.println("Error loading background image: " + e.getMessage());
        }

        // Set initial character position
        x = (backgroundImage.getWidth() * 5) / 2;
        y = (backgroundImage.getHeight() * 5) / 2;

        // Load background music and loop it
        try {
            // background music clip
            Clip bgMusic = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("src/resources/Nightfall Invasion.wav"));
            bgMusic.open(inputStream);
            bgMusic.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            System.err.println("Error loading background music: " + e.getMessage());
        }
    }

    private BufferedImage backgroundImage;

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Update the camera position
        cameraX = x - getWidth() / (2 * zoomFactor);
        cameraY = y - getHeight() / (2 * zoomFactor);

        // Apply zoom
        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(zoomFactor, zoomFactor);
        //g2d.translate(-((int) cameraX), -((int) cameraY));

        // Draw the background tileset
        if (backgroundImage != null) {
            int tileWidth = backgroundImage.getWidth();
            int tileHeight = backgroundImage.getHeight();
            int startX = (((int) cameraX) / tileWidth) * tileWidth;
            int startY = (((int) cameraY) / tileHeight) * tileHeight;
            for (int i = -tileWidth; i < getWidth() + tileWidth; i += tileWidth) {
                for (int j = -tileHeight; j < getHeight() + tileHeight; j += tileHeight) {
                    g.drawImage(backgroundImage, i + startX - ((int) cameraX), j + startY - ((int) cameraY), null);
                }
            }
        }

        // Draw the character
        g.setColor(Color.RED);
        g.fillRect(x - ((int) cameraX), y - ((int) cameraY), 50, 50);

        // Reset the transformations
        AffineTransform oldTransform = g2d.getTransform();
        g2d.setTransform(oldTransform);
    }

    private int calculateMovementSpeed() {
        return 5;
    }

    public void moveCharacter() {
        if (dx == 0 && dy == 0) {
            return;
        }
        int movementSpeed = calculateMovementSpeed();
        double length = Math.sqrt(dx * dx + dy * dy);
        x += (int) (dx * movementSpeed / length);
        y += (int) (dy * movementSpeed / length);
        repaint();
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_W) {
            dy = -1;
        } else if (keyCode == KeyEvent.VK_S) {
            dy = 1;
        } else if (keyCode == KeyEvent.VK_A) {
            dx = -1;
        } else if (keyCode == KeyEvent.VK_D) {
            dx = 1;
        }
    }

    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_W && dy == -1) {
            dy = 0;
        } else if (keyCode == KeyEvent.VK_S && dy == 1) {
            dy = 0;
        } else if (keyCode == KeyEvent.VK_A && dx == -1) {
            dx = 0;
        } else if (keyCode == KeyEvent.VK_D && dx == 1) {
            dx = 0;
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void actionPerformed(ActionEvent e) {
        moveCharacter();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Game");
        Game game = new Game();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();
        double zoomAmount = 0.1;

        if (notches < 0) {
            zoomFactor += zoomAmount;
        } else {
            zoomFactor -= zoomAmount;
            if (zoomFactor < zoomAmount) {
                zoomFactor = zoomAmount;
            }
        }

        repaint();
    }

}

