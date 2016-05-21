package Core.Connection;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Leonardo Arcari on 20/05/2016.
 */
public interface RMIServiceInterface extends Remote {
    String connect(RMIProcessor clientProcessor) throws RemoteException;
}
