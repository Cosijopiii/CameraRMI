package ServerRMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by COSI on 25/06/2016.
 */
public class IVideoDataimplementation  extends UnicastRemoteObject implements IVideoData {

    VideoData videoData;

    protected IVideoDataimplementation() throws RemoteException {
    }

    @Override
    public void setVideoData(VideoData videoData) throws RemoteException {
        this.videoData=videoData;
    }

    @Override
    public VideoData getVideoData() throws RemoteException {
        return videoData;

    }
}
