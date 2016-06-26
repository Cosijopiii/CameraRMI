package ServerRMI;

import java.io.ByteArrayInputStream;
import java.io.Serializable;

/**
 * Created by COSI on 25/06/2016.
 */
public class VideoData implements Serializable {

    private ByteArrayInputStream byteArrayInputStream;
    private int cameraClient;
    private String nameCameraClient;
    private String ip;

    public VideoData(){

    }

    public VideoData(ByteArrayInputStream byteArrayInputStream, int cameraClient, String nameCameraClient, String ip) {
        this.byteArrayInputStream = byteArrayInputStream;
        this.cameraClient = cameraClient;
        this.nameCameraClient = nameCameraClient;
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "VideoData{" +
                "nameCameraClient='" + nameCameraClient + '\'' +
                ", ip='" + ip + '\'' +
                ", cameraClient=" + cameraClient +
                '}';
    }
    public ByteArrayInputStream getByteArrayInputStream() {
        return byteArrayInputStream;
    }

    public void setByteArrayInputStream(ByteArrayInputStream byteArrayInputStream) {
        this.byteArrayInputStream = byteArrayInputStream;
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
