    import Modules.Background;
    import Modules.Character;
    import Modules.Music;
    import javax.swing.*;
    import java.awt.*;
    import java.awt.event.*;
    import java.awt.geom.AffineTransform;
    import Modules.Goblin;
    import java.util.ArrayList;
    import java.util.Random;
    import Modules.Structures.Keep;

    public class Game extends JPanel implements KeyListener, ActionListener, MouseWheelListener {
        private double cameraX, cameraY;
        private double zoomFactor = 1.0;
        private final Character character;
        private final Background background;
        private final Music music;
        private final ArrayList<Goblin> goblins;
        private final Keep keep;

        @Override
        public void keyTyped(KeyEvent e) {
            // You can leave this method empty if you don't want to use it
        }

        private Timer goblinSpawner;


        public Game() {
            setPreferredSize(new Dimension(800, 600));
            addKeyListener(this);
            setFocusable(true);
            setDoubleBuffered(true);
            addMouseWheelListener(this);

            background = new Background("src/resources/grassy_field_tile.png");
            int centerX = (background.getImage().getWidth() * 5) / 2;
            int centerY = (background.getImage().getHeight() * 5) / 2;

            character = new Character(centerX - 10, centerY - 10, 5, "src/resources/KnightScaled.png");

            cameraX = centerX - (double) getWidth() / 2;
            cameraY = centerY - (double) getHeight() / 2;

            music = new Music("src/resources/Nightfall_Invasion.wav");
            music.loop(); // Start playing the music

            goblins = new ArrayList<>();

            Timer timer = new Timer(1000 / 60, this);
            timer.start();

            goblinSpawner = new Timer(3000, null);
            goblinSpawner.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    spawnGoblin();
                    int delay = 3000 + new Random().nextInt(7000);
                    goblinSpawner.setInitialDelay(delay);
                    goblinSpawner.restart();
                }
            });
            goblinSpawner.setRepeats(true); // Change this line
            goblinSpawner.start();

            keep = new Keep(centerX - 100, centerY - 100, "src/resources/Structures/KeepScaled.png");

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
            JFrame frame = new JFrame("Kingdom of the Night");
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
            int startX = (int) Math.floor(cameraX / imgWidth) * imgWidth;
            int startY = (int) Math.floor(cameraY / imgHeight) * imgHeight;
            int numTilesX = (int) Math.ceil(getWidth() / (double) imgWidth / zoomFactor) + 1;
            int numTilesY = (int) Math.ceil(getHeight() / (double) imgHeight / zoomFactor) + 1;
            for (int x = 0; x < numTilesX; x++) {
                for (int y = 0; y < numTilesY; y++) {
                    g2d.drawImage(background.getImage(), startX + x * imgWidth, startY + y * imgHeight, null);
                }
            }

            // Draw the Character?
            int charWidth = character.getImage().getWidth();
            int charHeight = character.getImage().getHeight();
            g2d.drawImage(character.getImage(), character.getX() - charWidth / 2, character.getY() - charHeight / 2, null);

            // Draw the Keep
            int keepWidth = keep.getImage().getWidth();
            int keepHeight = keep.getImage().getHeight();
            g2d.drawImage(keep.getImage(), keep.getX() - keepWidth / 2, keep.getY() - keepHeight / 2, null);

            // Draw the goblins
            for (Goblin goblin : goblins) {
                int goblinWidth = goblin.getImage().getWidth();
                int goblinHeight = goblin.getImage().getHeight();
                g2d.drawImage(goblin.getImage(), goblin.getX() - goblinWidth / 2, goblin.getY() - goblinHeight / 2, null);
            }

            g2d.setTransform(oldTransform);
        }


        private void spawnGoblin() {
            Random random = new Random();
            int minDistance = 200;
            int maxDistance = 500;

            int distanceX = minDistance + random.nextInt(maxDistance - minDistance);
            int distanceY = minDistance + random.nextInt(maxDistance - minDistance);

            distanceX = random.nextBoolean() ? distanceX : -distanceX;
            distanceY = random.nextBoolean() ? distanceY : -distanceY;

            int x = character.getX() + distanceX;
            int y = character.getY() + distanceY;

            Goblin goblin = new Goblin(x, y, 3, "src/resources/GoblinScaled.png");
            goblins.add(goblin);
        }


    }
