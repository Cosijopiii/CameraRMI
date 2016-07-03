package ServerSide;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class ServerMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/ServerView.fxml"));
        primaryStage.setTitle("RMISecurity Camera");
        primaryStage.setScene(new Scene(root));
      primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();

            }
        });
    }
    public static void main(String[] args) {

      /*  Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        UserEntity us=new UserEntity();
        us.setPassword("123");
        us.setUsername("Cosi");
        session.save(us);
        session.getTransaction().commit();
        session.close();*/
        launch(args);
    }
}


