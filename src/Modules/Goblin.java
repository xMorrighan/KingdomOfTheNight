// src/Modules/Goblin.java
package Modules;
import java.awt.Rectangle;

public class Goblin extends Character {

    public Goblin(int x, int y, int speed, String imagePath) {
        super(x, y, speed, imagePath);
    }
    @Override
    public Rectangle getBounds() {
        return new Rectangle(getX() - getImage().getWidth() / 2, getY() - getImage().getHeight() / 2, getImage().getWidth(), getImage().getHeight());
    }
    // src/Modules/Goblin.java
    public void moveTowards(int targetX, int targetY) {
        int dx = targetX - getX();
        int dy = targetY - getY();

        double distance = Math.sqrt(dx * dx + dy * dy);
        double normalizedDx = dx / distance;
        double normalizedDy = dy / distance;

        setX(getX() + (int) (normalizedDx * getSpeed()));
        setY(getY() + (int) (normalizedDy * getSpeed()));
    }
    public void knockback(int distance, int targetX, int targetY) {
        int dx = getX() - targetX;
        int dy = getY() - targetY;

        double len = Math.sqrt(dx * dx + dy * dy);
        double normalizedDx = dx / len;
        double normalizedDy = dy / len;

        setX(getX() + (int) (normalizedDx * distance));
        setY(getY() + (int) (normalizedDy * distance));
    }

}
