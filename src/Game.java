import Modules.Background;
import Modules.Character;
import Modules.Music;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;


public class Game extends JPanel implements KeyListener, ActionListener, MouseWheelListener {
    private double cameraX, cameraY;
    private double zoomFactor = 1.0;
    private final Character character;
    private final Background background;
    private final Music music;

    @Override
    public void keyTyped(KeyEvent e) {
        // You can leave this method empty if you don't want to use it
    }

    public Game() {
        setPreferredSize(new Dimension(800, 600));
        addKeyListener(this);
        setFocusable(true);
        setDoubleBuffered(true);
        addMouseWheelListener(this);

        background = new Background("src/resources/grassy_field_tile.png");
        int centerX = (background.getImage().getWidth() * 5) / 2;
        int centerY = (background.getImage().getHeight() * 5) / 2;

        character = new Character(centerX - 10, centerY - 10, 5, "src/resources/KnightScaled.png"); // Subtract half of character size (20/2) to center it

        cameraX = centerX - (double) getWidth() / 2;
        cameraY = centerY - (double) getHeight() / 2;

        music = new Music("src/resources/Nightfall Invasion.wav");
        music.loop();

        Timer timer = new Timer(1000 / 60, this);
        timer.start();
    }

    private void updateCamera() {
        cameraX = character.getX() - getWidth() / (2 * zoomFactor);
        cameraY = character.getY() - getHeight() / (2 * zoomFactor);
    }


    public void moveCharacter() {
        character.move();
        repaint();
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_W) {
            character.setDy(-character.getSpeed());
        } else if (keyCode == KeyEvent.VK_S) {
            character.setDy(character.getSpeed());
        } else if (keyCode == KeyEvent.VK_A) {
            character.setDx(-character.getSpeed());
        } else if (keyCode == KeyEvent.VK_D) {
            character.setDx(character.getSpeed());
        }
    }

    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_W && character.getDy() == -character.getSpeed()) {
            character.setDy(0);
        } else if (keyCode == KeyEvent.VK_S && character.getDy() == character.getSpeed()) {
            character.setDy(0);
        } else if (keyCode == KeyEvent.VK_A && character.getDx() == -character.getSpeed()) {
            character.setDx(0);
        } else if (keyCode == KeyEvent.VK_D && character.getDx() == character.getSpeed()) {
            character.setDx(0);
        }
    }


    public void actionPerformed(ActionEvent e) {
        moveCharacter();
        updateCamera();
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
    } // Closing brace should be here

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        AffineTransform oldTransform = g2d.getTransform();

        g2d.scale(zoomFactor, zoomFactor);
        g2d.translate(-cameraX, -cameraY);

        // Draw the background
        int imgWidth = background.getImage().getWidth();
        int imgHeight = background.getImage().getHeight();
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                g2d.drawImage(background.getImage(), x * imgWidth, y * imgHeight, null);
            }
        }

        // Draw the character
        g2d.drawImage(character.getImage(), character.getX(), character.getY(), null);

        g2d.setTransform(oldTransform);
    }

}
