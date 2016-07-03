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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

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

    @FXML
    private MenuItem mntIniciarCamaras;
    private IVideoData iVideoData;
    private ScheduledExecutorService timer;
   private IVideoDataimplementation iVideoDataimplementation;
    @FXML
    void start() {
        frame.setPreserveRatio(true);
        Runnable grab= this::getNetworkVideo;
        timer= Executors.newSingleThreadScheduledExecutor();
        timer.scheduleAtFixedRate(grab,0,60, TimeUnit.MILLISECONDS);
    }
    private void getNetworkVideo() {
        try {
        switch (iVideoData.getVideoData().getCameraClient()){
            case 1:
                Camera0.setImage( new Image(new ByteArrayInputStream(iVideoData.getVideoData().getByteArray())));
                break;
            case 2:
                Camera1.setImage( new Image(new ByteArrayInputStream(iVideoData.getVideoData().getByteArray())));
                break;
            case 3:
                Camera2.setImage( new Image(new ByteArrayInputStream(iVideoData.getVideoData().getByteArray())));
                break;
            case 4:
                Camera3.setImage( new Image(new ByteArrayInputStream(iVideoData.getVideoData().getByteArray())));
                break;
            case 5:
                Camera4.setImage( new Image(new ByteArrayInputStream(iVideoData.getVideoData().getByteArray())));
                break;
            case 6:
                Camera5.setImage( new Image(new ByteArrayInputStream(iVideoData.getVideoData().getByteArray())));
                break;
            default:
                break;
        }






        } catch (RemoteException e) {
            e.printStackTrace();
        }

        // return new Image(byteArrayInputStream);
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

        try {
            iVideoData= (IVideoData) Naming.lookup("rmi://"+"localhost"+":1099/videoData");
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
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

    }

}
