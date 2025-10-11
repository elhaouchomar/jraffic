
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;

// The Simulation class in your JavaFX application serves as the core game loop.
// It manages the continuous updating of the simulation's state, ensuring that
// the a
// pplication remains responsive and that animations proceed smoothly.

// https://docs.oracle.com/javase/8/javafx/api/javafx/animation/AnimationTimer.html
// The class AnimationTimer allows to create a timer, that is called in each frame while it is active.
public class Simulation {
    private Pane root;
    private AnimationTimer timer;
    private long lastTime;

    public Simulation(Pane root) {
        this.root = root;
    }

    public void start() {
        lastTime = System.nanoTime(); // https://www.tutorialspoint.com/java/lang/system_nanotime.htm
        timer = new AnimationTimer() { // anonymous class instead of extends and override and new
                                       // https://docs.oracle.com/javase/8/javafx/api/javafx/animation/AnimationTimer.html

            @Override
            public void handle(long now) {
                double dt = (now - lastTime) / 1e9;
                update(dt);
                lastTime = now;
            }
        };
        timer.start();
    }

    private void update(double dt) {
        // future: update cars, lights, etc.
    }
}
