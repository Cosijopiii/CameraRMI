package ServerSide;

import ServerRMI.IVideoData;
import ServerRMI.IVideoDataimplementation;
import ServerRMI.VideoData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.PopOver;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
    private ListView<String> ListViewSnap;

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
    ObservableList<String> listFiles;
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
    @FXML
    void savePNG(){
        WritableImage image = frame.snapshot(new SnapshotParameters(), null);




        File file = new File(System.getProperty("user.home")+System.getProperty("file.separator")+"Snapshot"+System.getProperty("file.separator")+"img"+LocalDate.now()+System.nanoTime()+".png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            // TODO: handle exception here
        }

    listFiles.add(file.getAbsolutePath());
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
    void serverOFF() {
        try {
            UnicastRemoteObject.unexportObject(iVideoDataimplementation, true);
        } catch (NoSuchObjectException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void serverON() throws RemoteException {
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
        serverOFF();
        try {
            serverON();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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
    void updateList(){

        listFiles.clear();
        File[] files=new File(System.getProperty("user.home")+System.getProperty("file.separator")+"Snapshot").listFiles();

        for (File file : files) {
            if (file.isFile()) {
                listFiles.add(file.getAbsolutePath());
            }
        }
    }


    @FXML
    void initialize() {
        File[] files=new File(System.getProperty("user.home")+System.getProperty("file.separator")+"Snapshot").listFiles();
       listFiles= FXCollections.observableArrayList();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    listFiles.add(file.getAbsolutePath());
                }
            }
        }
        ListViewSnap.setItems(listFiles);
        PopOver d =new PopOver();;
        ListViewSnap.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
              String f=  ListViewSnap.getSelectionModel().getSelectedItem().toString();
                System.out.println(f.substring(2));

                try {
                    d.setContentNode(new ImageView(new Image(new File(f).toURI().toURL().toString())));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                d.show(ListViewSnap);
            }
        });
    }

}
