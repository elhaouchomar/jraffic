
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Car {
    // --- Properties ---
    private double x, y; // position
    private double speed; // pixels per second
    private String direction; // "N", "S", "E", "W"
    private Rectangle shape; // graphical representation
    private String route; // "LEFT", "RIGHT", "STRAIGHT"

    private static final double WIDTH = 40;
    private static final double HEIGHT = 40;

    public Car(double x, double y, String direction, Color color, double speed, String route) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.speed = speed;
        this.route = route;

        shape = new Rectangle(WIDTH, HEIGHT, color);
        shape.setX(x);
        shape.setY(y);
    }

    public Rectangle getShape() {
        return shape;
    }

    // --- Update car position ---
    public void update(double dt) {
        switch (direction) {
            case "N":
                y -= speed * dt;
                break;
            case "S":
                y += speed * dt;
                break;
            case "E":
                x += speed * dt;
                break;
            case "W":
                x -= speed * dt;
                break;
        }
        shape.setX(x);
        shape.setY(y);
    }

    // Check if car is off-screen
    public boolean isOffScreen(double windowSize) {
        return x < -50 || x > windowSize + 50 || y < -50 || y > windowSize + 50;
    }
}
