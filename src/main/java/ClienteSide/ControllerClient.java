package ClienteSide;

/**
 * Created by COSI on 25/06/2016.
 */

import HibernateUtil.FxDialogs;
import ServerRMI.IVideoAudioData;
import ServerRMI.VideoAudioData;
import ServerSide.ControllerServer;
import javafx.application.Platform;
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
import java.io.IOException;
import java.io.InputStream;
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
    @FXML
    private TextArea txtid;
    private IVideoAudioData iVideoAudioData;
    // a timer for acquiring the video stream
    private ScheduledExecutorService timer;

    // the OpenCV object that performs the video capture
    private VideoCapture capture;

    // a flag to change the button behavior
    private boolean cameraActive;

    void init() {
        this.capture = new VideoCapture();


    }

    private void playAudio() throws RemoteException {
        try {

            if (iVideoAudioData.getVideoAudioData().getBytesAudio()!=null) {
                byte audio[] = iVideoAudioData.getVideoAudioData().getBytesAudio();
                InputStream input = new ByteArrayInputStream(audio);
                final AudioFormat format = getFormat();
                final AudioInputStream ais = new AudioInputStream(input, format, audio.length / format.getFrameSize());
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
                final SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
                line.open(format);
                line.start();

                Runnable runner = new Runnable() {
                    int bufferSize = (int) format.getSampleRate()
                            * format.getFrameSize();
                    byte buffer[] = new byte[bufferSize];

                    public void run() {
                        try {
                            int count;
                            while ((count = ais.read(
                                    buffer, 0, buffer.length)) != -1) {
                                if (count > 0) {
                                    line.write(buffer, 0, count);
                                }
                            }
                            line.drain();
                            line.close();
                        } catch (IOException e) {
                            System.err.println("I/O problems: " + e);
                            System.exit(-3);
                        }
                    }
                };
                Platform.runLater(runner);
            }
        } catch (LineUnavailableException e) {
            System.err.println("Line unavailable: " + e);
            System.exit(-4);
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

            Platform.runLater(new Runnable() {
                @Override
                public void run() {

                    try {

                        playAudio();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                }
            });


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
