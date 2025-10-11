
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;

public class App extends Application {

    private Pane root;
    private Simulation simulation;

    @Override
    public void start(Stage stage) {
        root = new Pane();
        root.setPrefSize(800, 800);

        Scene scene = new Scene(root);
        stage.setTitle("Jraffic Simulation");
        stage.setScene(scene);
        stage.show();

        simulation = new Simulation(root);
        simulation.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
