package client.clientconnection;

import core.connection.Connection;
import core.connection.InfoProcessor;
import core.connection.RMIProcessor;
import core.connection.RMIServiceInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * A <code>RMIConnection</code> is a class that behaves like <code>Connection</code> implemented using RMI
 * framework.
 */
public class RMIConnection implements Connection {
    private RMIProcessor clientRmiProcessor;
    private RMIProcessor serverRmiProcessor;

    /**
     * Initializes a <code>RMIConnection</code> on the client connecting to an already online server. The current
     * implementation requires a {@link server.Server Server} running on <code>localhost</code> having RMI Registry
     * publishing a {@link RMIServiceInterface RMIServiceInterface} object bound.
     * @param clientProcessor The client's InfoProcessor
     * @throws RemoteException in case of RMI error
     * @throws NotBoundException in case of RMI binding error
     */
    public RMIConnection(InfoProcessor clientProcessor) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
        RMIServiceInterface serverService = (RMIServiceInterface) registry.lookup("rmi_server_service");
        clientRmiProcessor = new ClientRMIProcessor(clientProcessor);
        serverRmiProcessor = serverService.connect(clientRmiProcessor);
    }

    @Override
    public void sendInfo(Object info) {
        try {
            serverRmiProcessor.processInfo(info);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setOnDisconnection(Runnable runnable) {    }
}
