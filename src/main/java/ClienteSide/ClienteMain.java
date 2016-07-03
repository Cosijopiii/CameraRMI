package ClienteSide;/**
 * Created by COSI on 25/06/2016.
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.opencv.core.Core;

import java.io.IOException;

public class ClienteMain extends Application {


    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientView.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("RMISecurity Camera");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        ControllerClient controllerClient=loader.getController();
        controllerClient.init();primaryStage.setOnCloseRequest(event -> {
            controllerClient.Start();
            System.exit(0);
        });

    }

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
    }
}
