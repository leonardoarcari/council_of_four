package Client.ClientConnection;

import Core.Connection.InfoProcessor;
import Core.Connection.RMIProcessor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Leonardo Arcari on 20/05/2016.
 */
public class ClientRMIProcessor implements RMIProcessor {
    private InfoProcessor processor;


    public ClientRMIProcessor(InfoProcessor processor) throws RemoteException {
        this.processor = processor;
        UnicastRemoteObject.exportObject(this, 0);
    }

    @Override
    public void processInfo(Object info) throws RemoteException {
        processor.processInfo(info);
    }

    @Override
    public void setProcessor(InfoProcessor processor) throws RemoteException {
        this.processor = processor;
    }
}
