package ServerSide;

import ServerRMI.IVideoData;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ControllerServer {

    @FXML
    private ImageView frame;
    private IVideoData iVideoData;
    private ScheduledExecutorService timer;

    @FXML
    void start() {

        frame.setFitWidth(320);
        frame.setPreserveRatio(true);
        Runnable grab=()->{
          Image img= getNetworkVideo();
          frame.setImage(img);
        };
        timer= Executors.newSingleThreadScheduledExecutor();
        timer.scheduleAtFixedRate(grab,0,33, TimeUnit.MILLISECONDS);


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
