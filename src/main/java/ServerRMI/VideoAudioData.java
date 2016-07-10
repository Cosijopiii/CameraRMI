package ServerRMI;

import java.io.ByteArrayInputStream;
import java.io.Serializable;

/**
 * Created by COSI on 25/06/2016.
 */
public class VideoAudioData implements Serializable {

    private  byte[] byteArray;
    private int cameraClient;
    private String nameCameraClient;
    private String ip;



    private byte[] bytesAudio;

    public VideoAudioData(byte[] bytesAudio,int cameraClient) {
        this.bytesAudio = bytesAudio;
        this.cameraClient=cameraClient;

    }

    public VideoAudioData(){

    }

    public VideoAudioData(byte[] byteArray, int cameraClient, String nameCameraClient, String ip) {
        this.byteArray = byteArray;
        this.cameraClient = cameraClient;
        this.nameCameraClient = nameCameraClient;
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "VideoAudioData{" +
                "nameCameraClient='" + nameCameraClient + '\'' +
                ", ip='" + ip + '\'' +
                ", cameraClient=" + cameraClient +
                '}';
    }

    public byte[] getBytesAudio() {
        return bytesAudio;
    }

    public void setBytesAudio(byte[] bytesAudio) {
        this.bytesAudio = bytesAudio;
    }
    public byte[] getByteArray() {
        return byteArray;
    }

    public void setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }

    public int getCameraClient() {
        return cameraClient;
    }

    public void setCameraClient(int cameraClient) {
        this.cameraClient = cameraClient;
    }

    public String getNameCameraClient() {
        return nameCameraClient;
    }

    public void setNameCameraClient(String nameCameraClient) {
        this.nameCameraClient = nameCameraClient;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
