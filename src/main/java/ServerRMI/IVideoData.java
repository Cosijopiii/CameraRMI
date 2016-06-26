package ServerRMI;


import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by COSI on 25/06/2016.
 */
public interface IVideoData extends Remote {


         void setVideoData(VideoData videoData)throws RemoteException;
         VideoData getVideoData() throws RemoteException;
}
