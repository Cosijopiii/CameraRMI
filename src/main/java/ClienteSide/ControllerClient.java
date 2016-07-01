package ClienteSide;

/**
 * Created by COSI on 25/06/2016.
 */
import ServerRMI.IVideoData;
import ServerRMI.VideoData;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ControllerClient {


    @FXML
    private Button Start;
    private IVideoData iVideoData;
    // a timer for acquiring the video stream
    private ScheduledExecutorService timer;

    // the OpenCV object that performs the video capture
    private VideoCapture capture;

    // a flag to change the button behavior
    private boolean cameraActive;
    void init()
    {
        this.capture = new VideoCapture();
        try {
            iVideoData= (IVideoData) Naming.lookup("rmi://"+"192.168.0.3"+":1099/videoData");
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }

    }
    @FXML
    protected  void Start() {

        if (!cameraActive){
            capture.open(0);
            if (capture.isOpened()){
                cameraActive=true;

                Runnable Grabber = this::grabFrame;

                timer = Executors.newSingleThreadScheduledExecutor();
                timer.scheduleAtFixedRate(Grabber,0,33, TimeUnit.MILLISECONDS);
            }else
            {

                System.err.println("Failed to open the camera connection...");
            }
        }else{
            cameraActive=false;
            try {
                timer.shutdown();
                timer.awaitTermination(33,TimeUnit.MILLISECONDS);
            }catch (InterruptedException e)
            {
                System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
            }
            capture.release();


        }

    }

    /**
     * Get a frame from the opened video stream (if any)
     *
     * @return the {@link Image} to show
     */
    private Image grabFrame()
    {
        // init everything
        Image imageToShow = null;
        Mat frame = new Mat();

        // check if the capture is open
        if (this.capture.isOpened())
        {
            try
            {
                // read the current frame
                this.capture.read(frame);

                // if the frame is not empty, process it
                if (!frame.empty())
                {
                    // face detection
                   // this.detectAndDisplay(frame);
                    // convert the Mat object (OpenCV) to Image (JavaFX)
                    imageToShow = mat2Image(frame);
                }
            }
            catch (Exception e)
            {
                // log the (full) error
                System.err.println("ERROR: " + e);
            }
        }
        return imageToShow;
    }
    private Image mat2Image(Mat frame)
    {      Core.flip(frame,frame,1);
        // create a temporary buffer
        MatOfByte buffer = new MatOfByte();
        // encode the frame in the buffer, according to the PNG format
        Imgcodecs.imencode(".png", frame, buffer);
        // build and return an Image created from the image encoded in the
        // buffer
     ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer.toArray());
        try {
            iVideoData.setVideoData(new VideoData(buffer.toArray(),1,"TEST","LOCALHOST"));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return new Image(byteArrayInputStream);
    }




}
