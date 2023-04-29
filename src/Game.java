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
    import java.util.Iterator;

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

            background = new Background("src/resources/grassy_field_tile_2a.png");
            int centerX = (background.getImage().getWidth() * 5) / 2;
            int centerY = (background.getImage().getHeight() * 5) / 2;

            keep = new Keep(centerX - 1000, centerY - 1000, "src/resources/Structures/KeepScaled.png");
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

                    for (Goblin goblin : goblins) {
                        goblin.moveTowards(keep.getX(), keep.getY());
                    }
                }
            });
            goblinSpawner.setRepeats(true);
            goblinSpawner.start();
        }


        private void updateCamera() {
            cameraX = character.getX() - getWidth() / (2 * zoomFactor);
            cameraY = character.getY() - getHeight() / (2 * zoomFactor);
        }


        public void moveCharacter() {
            int oldX = character.getX();
            int oldY = character.getY();

            character.move();

            if (characterKeepCollision()) {
                character.setX(oldX);
                character.setY(oldY);
            }

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

            if (e.getSource() == goblinSpawner) {
                spawnGoblin();
            }

            Iterator<Goblin> goblinIterator = goblins.iterator();
            while (goblinIterator.hasNext()) {
                Goblin goblin = goblinIterator.next();
                goblin.moveTowards(keep.getX(), keep.getY());

                // Check for goblin collision with the character
                if (goblin.getBounds().intersects(character.getBounds())) {
                    int knockbackDistance = 50;
                    goblin.knockback(knockbackDistance, character.getX(), character.getY());
                    character.knockback(knockbackDistance, goblin.getX(), goblin.getY());
                }

                // Check for collision between goblin and keep
                if (goblin.getBounds().intersects(keep.getBounds())) {
                    int knockbackDistance = 50;
                    goblin.knockback(knockbackDistance, keep.getX(), keep.getY());
                }
            }
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
            int minDistance = 2000;
            int maxDistance = 5000;
            int maxAttempts = 10; // Limit the number of attempts to find a valid position

            Goblin goblin = null;

            for (int attempt = 0; attempt < maxAttempts; attempt++) {
                int distanceX = minDistance + random.nextInt(maxDistance - minDistance);
                int distanceY = minDistance + random.nextInt(maxDistance - minDistance);

                distanceX = random.nextBoolean() ? distanceX : -distanceX;
                distanceY = random.nextBoolean() ? distanceY : -distanceY;

                int x = keep.getX() + distanceX;
                int y = keep.getY() + distanceY;

                goblin = new Goblin(x, y, 3, "src/resources/GoblinScaled.png");

                if (goblinWithinRange(goblin, minDistance, maxDistance)) {
                    break;
                }
            }

            if (goblin != null && goblinWithinRange(goblin, minDistance, maxDistance)) {
                goblins.add(goblin);
            }
        }

        public boolean goblinWithinRange(Goblin goblin, int minDistance, int maxDistance) {
            int dx = goblin.getX() - keep.getX();
            int dy = goblin.getY() - keep.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            return distance >= minDistance && distance <= maxDistance;
        }

        public boolean characterKeepCollision() {
            return character.getBounds().intersects(keep.getBounds());
        }
        public boolean goblinKeepCollision(Goblin goblin) {
            return goblin.getBounds().intersects(keep.getBounds());
        }


    }
