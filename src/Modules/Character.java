public class Character {
    private int x, y;
    private int dx, dy;

    public Character(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public int calculateMovementSpeed() {
        int movementSpeed = 5; // You can change this value or implement a different logic
        return movementSpeed;
    }

    public void move() {
        int movementSpeed = calculateMovementSpeed();
        if (dx == 0 && dy == 0) {
            return;
        }
        double length = Math.sqrt(dx * dx + dy * dy);
        x += (int) (dx * movementSpeed / length);
        y += (int) (dy * movementSpeed / length);
    }
}
