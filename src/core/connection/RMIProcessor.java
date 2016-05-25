package core.connection;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Leonardo Arcari on 20/05/2016.
 */
public interface RMIProcessor extends Remote {
    void processInfo(Object info) throws RemoteException;
    void setProcessor(InfoProcessor processor) throws RemoteException;
}
