package ServerRMI;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by COSI on 25/06/2016.
 */
public class VideoDataMain {
    public static void main(String agv[]) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(1099);
        IVideoDataimplementation iVideoDataimplementation=new IVideoDataimplementation();
        try {
            Naming.rebind("rmi://"+"localhost"+":1099/videoData",iVideoDataimplementation);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
