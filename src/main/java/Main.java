import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import config.Resource;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        URL url = getClass().getResource(Resource.gameView);
        if (url != null) {
            Parent root = FXMLLoader.load(url);
            primaryStage.setTitle("Battle city");
            primaryStage.setScene(new Scene(root, 800, 725));
            primaryStage.setResizable(false);
            primaryStage.show();
            root.requestFocus();
        } else {
            System.out.println("Invalid resource path.");
        }
    }

    @Override
    public void stop() {
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}