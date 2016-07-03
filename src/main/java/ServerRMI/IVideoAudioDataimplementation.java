package ServerRMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by COSI on 25/06/2016.
 */
public class IVideoAudioDataimplementation extends UnicastRemoteObject implements IVideoAudioData {

    VideoAudioData videoAudioData;

    public IVideoAudioDataimplementation() throws RemoteException {
    }





    public void setVideoAudioData(VideoAudioData videoAudioData) throws RemoteException {
        this.videoAudioData = videoAudioData;

    }

    public VideoAudioData getVideoAudioData() throws RemoteException {
        return videoAudioData;

    }
}
