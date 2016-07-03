package client.clientconnection;

import core.connection.InfoProcessor;
import core.connection.RMIProcessor;

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
}
