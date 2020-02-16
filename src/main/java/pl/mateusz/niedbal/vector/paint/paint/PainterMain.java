package pl.mateusz.niedbal.vector.paint.paint;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class PainterMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass()
                .getResource("/painter.fxml"));
        AnchorPane anchorPane = loader.load();
        PainterController controller = loader.getController();
        primaryStage.setTitle("Painter");
        Scene scene = new Scene(anchorPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}