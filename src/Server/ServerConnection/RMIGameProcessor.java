package server.serverconnection;

import core.connection.InfoProcessor;
import core.connection.RMIProcessor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Leonardo Arcari on 21/05/2016.
 */
public class RMIGameProcessor implements RMIProcessor {
    private InfoProcessor processor;

    public RMIGameProcessor(InfoProcessor processor) throws RemoteException {
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
