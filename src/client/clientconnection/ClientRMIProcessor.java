package client.clientconnection;

import core.connection.InfoProcessor;
import core.connection.RMIProcessor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * A <code>ClientRMIProcessor</code> is a class behaving as a <code>RMIProcessor</code> exporting itself as a
 * UnicastRemoteObject
 */
public class ClientRMIProcessor implements RMIProcessor {
    private InfoProcessor processor;

    /**
     * Initializes a <code>ClientRMIProcessor</code> with the client's <code>InfoProcessor</code> of the game
     * @param processor InfoProcessor of the client
     * @throws RemoteException in case of RMI exception
     */
    public ClientRMIProcessor(InfoProcessor processor) throws RemoteException {
        this.processor = processor;
        UnicastRemoteObject.exportObject(this, 0);
    }

    /**
     * @param info Message/Information to handle
     * @throws RemoteException in case of RMI exception
     */
    @Override
    public void processInfo(Object info) throws RemoteException {
        processor.processInfo(info);
    }
}
