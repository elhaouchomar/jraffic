
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

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
        setupEnvironment(); // draw roads, intersection, and traffic lights
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

    private void setupEnvironment() {
        // --- Roads ---
        Rectangle horizontal = new Rectangle(0, 350, 800, 100); // y=350, height=100
        horizontal.setFill(Color.GRAY);
        Rectangle vertical = new Rectangle(350, 0, 100, 800); // x=350, width=100
        vertical.setFill(Color.GRAY);

        root.getChildren().addAll(horizontal, vertical);

        // --- Lane dividers ---

        // North ↔ West
        Line nwLine1 = new Line(0, 400  , 800, 400); // top-left diagonal / adjust as needed
        nwLine1.getStrokeDashArray().addAll(10.0, 10.0); // 10px dash, 10px gap
        nwLine1.setStroke(Color.WHITE);
        nwLine1.setStrokeWidth(3);

        // East ↔ South
        Line esLine1 = new Line(400, 0, 400, 800); // top-right diagonal / adjust as needed
        esLine1.getStrokeDashArray().addAll(10.0, 10.0);
        esLine1.setStroke(Color.WHITE);
        esLine1.setStrokeWidth(3);

        root.getChildren().addAll(nwLine1, esLine1);

        // --- Traffic Lights (placeholder circles) ---
        double lightSize = 20;

        // North lane
        Circle lightN = new Circle(330, 330, lightSize, Color.RED);
        // South lane
        Circle lightS = new Circle(470, 470, lightSize, Color.RED);
        // East lane
        Circle lightE = new Circle(470, 330, lightSize, Color.RED);
        // West lane
        Circle lightW = new Circle(330, 470, lightSize, Color.RED);

        root.getChildren().addAll(lightN, lightS, lightE, lightW);
    }

}
