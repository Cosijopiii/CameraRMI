package ClienteSide;

/**
 * Created by COSI on 25/06/2016.
 */

import HibernateUtil.FxDialogs;
import ServerRMI.IVideoAudioData;
import ServerRMI.VideoAudioData;
import ServerSide.ControllerServer;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ControllerClient {


    @FXML
    private Button Start;
    @FXML
    private TextArea txtid;
    private IVideoAudioData iVideoAudioData;
    // a timer for acquiring the video stream
    private ScheduledExecutorService timer;

    // the OpenCV object that performs the video capture
    private VideoCapture capture;

    // a flag to change the button behavior
    private boolean cameraActive;
    public final static int FILE_SIZE = Integer.MAX_VALUE;
    void init() {
        this.capture = new VideoCapture();


    }

    private void playAudio() throws RemoteException {
        try {


                VideoAudioData v=iVideoAudioData.getAudioData();
            if (v!=null) {
                if (v.getBytesAudio() != null) {

                        Path path = Paths.get(System.getProperty("user.home") + "/a1.wav");
                        Files.write(path, v.getBytesAudio());
                        File yourFile = new File(System.getProperty("user.home") + "/a1.wav");
                        AudioInputStream stream;
                        AudioFormat format;
                        DataLine.Info info;
                        Clip clip;

                        stream = AudioSystem.getAudioInputStream(yourFile);
                        format = stream.getFormat();
                        info = new DataLine.Info(Clip.class, format);
                        clip = (Clip) AudioSystem.getLine(info);
                        clip.open(stream);
                        clip.start();
                    iVideoAudioData.SetAudioData(new VideoAudioData());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private AudioFormat getFormat() {
        return ControllerServer.getFormat();
    }

    @FXML
    void Conecting() {

        String ip = FxDialogs.showTextInput("IP del servidor", "Introdusca la ip del servidor", "localhost");
        if (ip != null) {
            try {
                iVideoAudioData = (IVideoAudioData) Naming.lookup("rmi://" + ip + ":1099/videoData");
            } catch (NotBoundException | MalformedURLException | RemoteException e) {

                FxDialogs.showInformation("Fallo", "Error al conectarse al servidor " + ip + " con exito");
                return;
            }
            FxDialogs.showInformation("Exito", "Se conecto al servidor " + ip + " con exito");


            Task<Void> tk=new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    while (true) {
                        try {
                            playAudio();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }

                }
            };

            new Thread(tk).start();

        }


    }

    @FXML
    public void Start() {

        if (!cameraActive) {
            capture.open(0);
            if (capture.isOpened()) {
                cameraActive = true;

                Runnable Grabber = this::grabFrame;

                timer = Executors.newSingleThreadScheduledExecutor();
                timer.scheduleAtFixedRate(Grabber, 0, 100, TimeUnit.MILLISECONDS);
            } else {

                System.err.println("Failed to open the camera connection...");
            }
        } else {
            cameraActive = false;
            try {
                timer.shutdown();
                timer.awaitTermination(60, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
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
    private Image grabFrame() {
        // init everything
        Image imageToShow = null;
        Mat frame = new Mat();

        // check if the capture is open
        if (this.capture.isOpened()) {
            try {
                // read the current frame
                this.capture.read(frame);

                // if the frame is not empty, process it
                if (!frame.empty()) {
                    // face detection
                    // this.detectAndDisplay(frame);
                    // convert the Mat object (OpenCV) to Image (JavaFX)
                    imageToShow = mat2Image(frame);
                }
            } catch (Exception e) {
                // log the (full) error
                System.err.println("ERROR: " + e);
            }
        }
        return imageToShow;
    }

    private Image mat2Image(Mat frame) {
        Core.flip(frame, frame, 1);
        // create a temporary buffer
        MatOfByte buffer = new MatOfByte();
        // encode the frame in the buffer, according to the PNG format
        Imgcodecs.imencode(".png", frame, buffer);
        // build and return an Image created from the image encoded in the
        // buffer
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer.toArray());
        try {
            iVideoAudioData.setVideoAudioData(new VideoAudioData(buffer.toArray(), Integer.parseInt(txtid.getText()), "TEST", "LOCALHOST"));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return new Image(byteArrayInputStream);
    }


}
