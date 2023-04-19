import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;


public class Game extends JPanel implements KeyListener, ActionListener, MouseWheelListener {
    private double cameraX, cameraY;
    private double zoomFactor = 1.0;
    private Character character;
    private Background background;
    private Music music;

    @Override
    public void keyTyped(KeyEvent e) {
        // You can leave this method empty if you don't want to use it
    }

    public Game() {
        setPreferredSize(new Dimension(800, 600));
        addKeyListener(this);
        setFocusable(true);
        setDoubleBuffered(true);
        Timer timer = new Timer(1000 / 60, this);
        timer.start();
        addMouseWheelListener(this);

        background = new Background("src/resources/grassy_field_tile.png");
        character = new Character((background.getImage().getWidth() * 5) / 2, (background.getImage().getHeight() * 5) / 2);
        music = new Music("src/resources/Nightfall Invasion.wav");
        music.loop();
    }

    // ...

    public void moveCharacter() {
        character.move();
        repaint();
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_W) {
            character.setDy(-1);
        } else if (keyCode == KeyEvent.VK_S) {
            character.setDy(1);
        } else if (keyCode == KeyEvent.VK_A) {
            character.setDx(-1);
        } else if (keyCode == KeyEvent.VK_D) {
            character.setDx(1);
        }
    }


    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_W && character.getDy() == -1) {
            character.setDy(0);
        } else if (keyCode == KeyEvent.VK_S && character.getDy() == 1) {
            character.setDy(0);
        } else if (keyCode == KeyEvent.VK_A && character.getDx() == -1) {
            character.setDx(0);
        } else if (keyCode == KeyEvent.VK_D && character.getDx() == 1) {
            character.setDx(0);
        }
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
            g2d.setColor(Color.RED);
            g2d.fillRect(character.getX(), character.getY(), 20, 20);

            g2d.setTransform(oldTransform);
        }

        repaint();
    }
}
