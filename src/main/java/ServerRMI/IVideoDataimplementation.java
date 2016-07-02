package ServerRMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by COSI on 25/06/2016.
 */
public class IVideoDataimplementation  extends UnicastRemoteObject implements IVideoData {

    VideoData videoData;

    public IVideoDataimplementation() throws RemoteException {
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
