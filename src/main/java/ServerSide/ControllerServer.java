package ServerSide;

import ServerRMI.IVideoData;
import ServerRMI.IVideoDataimplementation;
import ServerRMI.VideoData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    private boolean[] flags = {false, false, false, false, false, false};

    @FXML
    void start() {
        frame.setPreserveRatio(true);
        Runnable grab = this::getNetworkVideo;
        timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleWithFixedDelay(grab, 0, 100, TimeUnit.MILLISECONDS);

    }

    private void getNetworkVideo() {
        try {
            VideoData data = iVideoData.getVideoData();

            switch (data.getCameraClient()) {
                case 1:
                    if (flags[0]) {
                        frame.setImage(new Image(new ByteArrayInputStream(data.getByteArray())));
                    }
                    Camera0.setImage(new Image(new ByteArrayInputStream(data.getByteArray())));
                    break;
                case 2:
                    if (flags[1]) {
                        frame.setImage(new Image(new ByteArrayInputStream(data.getByteArray())));
                    }
                    Camera1.setImage(new Image(new ByteArrayInputStream(data.getByteArray())));
                    break;
                case 3:
                    if (flags[2]) {
                        frame.setImage(new Image(new ByteArrayInputStream(data.getByteArray())));
                    }
                    Camera2.setImage(new Image(new ByteArrayInputStream(data.getByteArray())));
                    break;
                case 4:
                    if (flags[3]) {
                        frame.setImage(new Image(new ByteArrayInputStream(data.getByteArray())));
                    }
                    Camera3.setImage(new Image(new ByteArrayInputStream(data.getByteArray())));
                    break;
                case 5:
                    if (flags[4]) {
                        frame.setImage(new Image(new ByteArrayInputStream(data.getByteArray())));
                    }
                    Camera4.setImage(new Image(new ByteArrayInputStream(data.getByteArray())));
                    break;
                case 6:
                    if (flags[5]) {
                        frame.setImage(new Image(new ByteArrayInputStream(data.getByteArray())));
                    }
                    Camera5.setImage(new Image(new ByteArrayInputStream(data.getByteArray())));
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
            UnicastRemoteObject.unexportObject(iVideoDataimplementation, true);
        } catch (NoSuchObjectException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void serverON(ActionEvent event) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(1099);

        iVideoDataimplementation = new IVideoDataimplementation();
        try {
            Naming.rebind("rmi://" + "localhost" + ":1099/videoData", iVideoDataimplementation);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            iVideoData = (IVideoData) Naming.lookup("rmi://" + "localhost" + ":1099/videoData");
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }


    }

    @FXML
    void serverReset(ActionEvent event) {

    }

    void setFlags(int pos) {

        for (int i = 0; i < 6; i++) {
            if (pos == i)
                flags[i] = true;
            else
                flags[i] = false;

        }
    }

    @FXML
    void show(ActionEvent event) {
        if (event.getSource().equals(btnC1)) {
            setFlags(0);
        }
        if (event.getSource().equals(btnC2)) {
            setFlags(1);
        }
        if (event.getSource().equals(btnC3)) {
            setFlags(2);
        }
        if (event.getSource().equals(btnC4)) {
            setFlags(3);
        }
        if (event.getSource().equals(btnC5)) {
            setFlags(4);
        }
        if (event.getSource().equals(btnC6)) {
            setFlags(5);
        }
    }

    @FXML
    void initialize() {

    }

}
