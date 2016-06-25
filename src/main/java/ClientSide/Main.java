package ClientSide;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.Session;
import HibernateUtil.*;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/VistaPrincipal.fxml"));
        primaryStage.setTitle("RMISecurity Camera");
        primaryStage.setScene(new Scene(root, 300, 275));
      primaryStage.show();
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


