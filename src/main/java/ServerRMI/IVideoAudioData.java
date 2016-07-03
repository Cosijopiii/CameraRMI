package ServerRMI;


import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by COSI on 25/06/2016.
 */
public interface IVideoAudioData extends Remote {


         void setVideoAudioData(VideoAudioData videoAudioData)throws RemoteException;
         VideoAudioData getVideoAudioData() throws RemoteException;
}
