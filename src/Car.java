
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

    private boolean turning = false;
    private double targetX, targetY; // intersection point for the turn

    private boolean turned = false;

    public Car(double x, double y, String direction, double speed, String route) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.speed = speed;
        this.route = route;

        // Assign color based on route
        Color color;
        switch (route) {
            case "LEFT" -> color = Color.RED;
            case "RIGHT" -> color = Color.LIMEGREEN;
            default -> color = Color.BLUE; // STRAIGHT
        }

        shape = new Rectangle(WIDTH, HEIGHT, color);
        shape.setX(x);
        shape.setY(y);
    }

    public void update(double dt) {
        if (turning && !turned) {
            // simple linear approximation for turning
            double dx = targetX - x;
            double dy = targetY - y;
            double dist = Math.sqrt(dx * dx + dy * dy);

            if (dist < 5) {
                // Snap exactly to target to end the turn cleanly
                x = targetX;
                y = targetY;

                turning = false;

                switch (route) {
                    case "LEFT" -> direction = turnLeftDirection(direction);
                    case "RIGHT" -> direction = turnRightDirection(direction);
                }
                this.turned = true;
            } else {
                x += (dx / dist) * speed * dt;
                y += (dy / dist) * speed * dt;
            }

        } else {
            // normal movement
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

            // check if entering intersection
            if (isApproachingIntersection() && !route.equals("STRAIGHT")) {
                turning = true;
                targetX = 400; // center of intersection
                targetY = 400;
            }

        }

        shape.setX(x);
        shape.setY(y);

    }

    public boolean isApproachingIntersection() {
        int offset = 0;

        // adjust trigger offset based on route
        switch (route) {
            case "LEFT" -> offset = 50; // go deeper before turning
            case "RIGHT" -> offset = 0; // turn a bit earlier
            default -> offset = 0; // straight
        }

        System.out.println(offset);
        System.out.println(direction);
        switch (direction) {
            case "N":
                // moving up → y decreasing
                return (y - 4) < (400 - offset);
            case "S":
                // moving down → y increasing
                return (y + HEIGHT + 4) > (400 + offset); // + offset > 395 && y < 400 + offset;
            case "E":
                // moving right → x increasing
                return (x + HEIGHT + 4) > (400 + offset); // offset > 390 && x < 400 + offset;
            case "W":
                // moving left → x decreasing
                return (x - 4) < (400 - offset);// && x > 395 + offset;
            default:
                return false;
        }
    }

    // helper functions
    private String turnLeftDirection(String dir) {
        return switch (dir) {
            case "N" -> "W";
            case "S" -> "E";
            case "E" -> "N";
            case "W" -> "S";
            default -> dir;
        };
    }

    private String turnRightDirection(String dir) {
        return switch (dir) {
            case "N" -> "E";
            case "S" -> "W";
            case "E" -> "S";
            case "W" -> "N";
            default -> dir;
        };
    }

    public Rectangle getShape() {
        return shape;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return WIDTH;
    }

    public double getHeight() {
        return HEIGHT;
    }

    public String getDirection() {
        return direction;
    }

    // Check if car is off-screen
    public boolean isOffScreen(double windowSize) {
        return x < -50 || x > windowSize + 50 || y < -50 || y > windowSize + 50;
    }
}
