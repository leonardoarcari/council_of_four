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
 * Created by Leonardo Arcari on 20/05/2016.
 */
public class RMIConnection implements Connection {
    private RMIProcessor clientRmiProcessor;
    private RMIProcessor serverRmiProcessor;

    public RMIConnection(InfoProcessor clientProcessor) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
        RMIServiceInterface serverService = (RMIServiceInterface) registry.lookup("rmi_server_service");
        clientRmiProcessor = new ClientRMIProcessor(clientProcessor);
        serverRmiProcessor = serverService.connect(clientRmiProcessor);
    }

    @Override
    public void setInfoProcessor(InfoProcessor processor) {
        try {
            clientRmiProcessor.setProcessor(processor);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendInfo(Object info) {
        try {
            serverRmiProcessor.processInfo(info);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
