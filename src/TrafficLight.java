import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class TrafficLight {
    public enum State {
        RED, GREEN
    }

    
    private final Circle shape;
    private State state;
    private double timer = 0;
    private double cycleDuration = 6.0; // seconds per state (tune as needed)

    private boolean pendingGreen = false;
    private double bufferTime = 1.0; // seconds to wait before turning green

    public TrafficLight(double x, double y, State initialState) {
        this.state = initialState;
        this.shape = new Circle(x, y, 12); // use 12 or 15 depending on look
        this.shape.setFill(getColor());
    }

    public void update(double dt) {
        timer += dt;

        switch (state) {
            case RED:
                if (timer >= cycleDuration) {
                    // start buffer before switching to green
                    pendingGreen = true;
                    timer = 0;
                }
                break;

            case GREEN:
                if (timer >= cycleDuration) {
                    switchLight(); // green → red
                    timer = 0;
                }
                break;
        }

        // handle buffer
        if (pendingGreen) {
            if (timer >= bufferTime) {
                switchLight(); // red → green after buffer
                pendingGreen = false;
                timer = 0;
            }
        }
    }

    public void switchLight() {
        state = (state == State.RED) ? State.GREEN : State.RED;
        shape.setFill(getColor());
    }

    private Color getColor() {
        return (state == State.RED) ? Color.RED : Color.LIMEGREEN;
    }

    public Circle getShape() {
        return shape;
    }

    public State getState() {
        return state;
    }

    public void setState(State s) {
        this.state = s;
        this.shape.setFill(getColor());
        this.timer = 0;
    }
}
