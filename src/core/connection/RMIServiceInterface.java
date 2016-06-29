package core.connection;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * A <code>RMIServiceInterface</code> specifies the behaviour of a class that sets a RMI connection between client and
 * server up.
 */
public interface RMIServiceInterface extends Remote {
    /**
     * Sets a RMI connection up by exchanging between client and server the respective {@link RMIProcessor RMIProcessors}
     * so that the server can send message to be processed on the client and the client with the server as well.
     * @param clientProcessor The client's RMIProcessor
     * @return A RMIProcessor the client will interact with to communicate with the server
     * @throws RemoteException
     */
    RMIProcessor connect(RMIProcessor clientProcessor) throws RemoteException;
}
