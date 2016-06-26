package ServerSide;

import ServerRMI.IVideoData;
import ServerRMI.VideoData;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ControllerServer {

    @FXML
    private ImageView frame;
    private IVideoData iVideoData;
    @FXML
    void start() {

        frame.setFitWidth(320);
        frame.setPreserveRatio(true);
    }
    @FXML
    void initialize() {
        try {
            iVideoData= (IVideoData) Naming.lookup("rmi://"+"localhost"+":1099/videoData");


        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }

    }
    private Image grabFrame() {
        ByteArrayInputStream byteArrayInputStream=null;
        try {
            byteArrayInputStream= iVideoData.getVideoData().getByteArrayInputStream();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return new Image(byteArrayInputStream);
    }
}
