package server.serverconnection;

import core.Player;
import core.Subject;
import core.connection.InfoProcessor;
import core.connection.RMIProcessor;
import server.WaitingHall;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * A <code>RMIConnection</code> is a class that behaves like <code>ServerConnection</code> implemented using RMI
 * framework.
 */
public class RMIConnection implements RMIProcessor, ServerConnection {
    private RMIProcessor clientRMIProcessor;
    private InfoProcessor serverProcessor;
    private Player me;
    private Runnable onDisconnect;

    /**
     * Initializes a <code>RMIConnection</code> exporting itself as a <code>UnicastRemoteObject</code> to allow remote
     * calls from the client
     * @param clientProcessor RMIProcessor to ask for messages processing after de-serializing them
     * @throws RemoteException in case of RMI error
     */
    public RMIConnection(RMIProcessor clientProcessor) throws RemoteException {
        this.clientRMIProcessor = clientProcessor;
        serverProcessor = WaitingHall.getInstance().getInfoProcessor();
        UnicastRemoteObject.exportObject(this, 0);
    }

    /**
     * Sends the input message to the client and invokes its <code>RMIProcessor</code> to process it (using remote
     * method invocation)
     * @param info Object to send
     */
    @Override
    public void sendInfo(Object info) {
        try {
            clientRMIProcessor.processInfo(info);
        } catch (RemoteException e) {
            // client disconnected. Remove this player from game's ones.
            if (onDisconnect != null) onDisconnect.run();
        }
    }

    /**
     * Delegates the job to the game's infoProcessor
     * @see server.ServerProcessor
     * @param info Message/Information to handle
     * @throws RemoteException in case of RMI error
     */
    @Override
    public void processInfo(Object info) throws RemoteException{
        serverProcessor.processInfo(info);
    }

    @Override
    public void setOnDisconnection(Runnable runnable) {
        onDisconnect = runnable;
    }

    /**
     * Calls {@link #sendInfo(Object)} to send to the player return by {@link #getPlayer()} a copy of a game's model
     * object that notified a change
     * @param subject Game model object to send that changed its state
     */
    @Override
    public void update(Subject subject) {
        sendInfo(subject);
    }

    @Override
    public void setPlayer(Player player) {
        me = player;
    }

    @Override
    public Player getPlayer() {
        return me;
    }
}
