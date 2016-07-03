package ServerRMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by COSI on 25/06/2016.
 */
public class IVideoAudioDataimplementation extends UnicastRemoteObject implements IVideoAudioData {

    VideoAudioData videoAudioData;
    VideoAudioData videoAudio;
    public IVideoAudioDataimplementation() throws RemoteException {
    }


    public void SetAudioData(VideoAudioData videoAudio) throws RemoteException {
    this.videoAudio=videoAudio;
    }


    public VideoAudioData getAudioData() throws RemoteException {
        return videoAudio;
    }

    public void setVideoAudioData(VideoAudioData videoAudioData) throws RemoteException {
        this.videoAudioData = videoAudioData;

    }

    public VideoAudioData getVideoAudioData() throws RemoteException {
        return videoAudioData;

    }
}
