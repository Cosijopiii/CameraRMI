package ServerSide;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ServerRMI.IVideoData;
import ServerRMI.IVideoDataimplementation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ControllerServer {


    @FXML
    private MenuItem mntStartServer;

    @FXML
    private MenuItem mntStopServer;

    @FXML
    private MenuItem mntResetServer;

    @FXML
    private ImageView frame;

    @FXML
    private Button btnSnapFrame;

    @FXML
    private ListView<?> ListViewSnap;

    @FXML
    private ImageView Camera1;

    @FXML
    private Button btnC2;

    @FXML
    private ImageView Camera0;

    @FXML
    private Button btnC1;

    @FXML
    private ImageView Camera2;

    @FXML
    private Button btnC3;

    @FXML
    private ImageView Camera3;

    @FXML
    private Button btnC4;

    @FXML
    private ImageView Camera4;

    @FXML
    private Button btnC5;

    @FXML
    private ImageView Camera5;

    @FXML
    private Button btnC6;

    private IVideoData iVideoData;
    private ScheduledExecutorService timer;
   private IVideoDataimplementation iVideoDataimplementation;
    @FXML
    void start() {

        frame.setFitWidth(320);
        frame.setPreserveRatio(true);
        Runnable grab=()->{
          Image img= getNetworkVideo();
          Camera0.setImage(img);
        };
        timer= Executors.newSingleThreadScheduledExecutor();
        timer.scheduleAtFixedRate(grab,0,33, TimeUnit.MILLISECONDS);
    }

    @FXML
    void serverOFF(ActionEvent event) {
        try {
            UnicastRemoteObject.unexportObject(iVideoDataimplementation,true);
        } catch (NoSuchObjectException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void serverON(ActionEvent event) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(1099);

         iVideoDataimplementation=new IVideoDataimplementation();
        try {
            Naming.rebind("rmi://"+"localhost"+":1099/videoData",iVideoDataimplementation);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void serverReset(ActionEvent event) {

    }

    @FXML
    void show(ActionEvent event) {

    }

    @FXML
    void initialize() {
        try {
            iVideoData= (IVideoData) Naming.lookup("rmi://"+"localhost"+":1099/videoData");
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }
    private Image getNetworkVideo() {
        ByteArrayInputStream byteArrayInputStream=null;
        try {
            byteArrayInputStream =new ByteArrayInputStream(iVideoData.getVideoData().getByteArray());

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return new Image(byteArrayInputStream);
    }
}
